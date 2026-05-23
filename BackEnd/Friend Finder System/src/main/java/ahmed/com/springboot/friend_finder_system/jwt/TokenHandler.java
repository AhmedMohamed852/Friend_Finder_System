package ahmed.com.springboot.friend_finder_system.jwt;

import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.helper.JwtToken;
import ahmed.com.springboot.friend_finder_system.service.User_Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenHandler {


    private String secretKey;

    private Duration duration;

    private JwtBuilder jwtBuilder;

    private JwtParser jwtParser;

    private User_Service user_service;


   public TokenHandler(JwtToken jwtToken)
    {
        this.secretKey = jwtToken.getSecret();
        this.duration = jwtToken.getExpiration();

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        jwtBuilder = Jwts.builder().signWith(key);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }



    public String generateToken(UserDto user)
    {
        Date issuedDate = new Date();
        Date expirationDate = Date.from(issuedDate.toInstant().plus(duration));

        return jwtBuilder
                .setSubject(user.getUsername())
                  .setIssuedAt(issuedDate)
                    .setExpiration(expirationDate)
                      .claim("roles" , user.getRoles().stream().map(rolesDto -> rolesDto.getName().toString()).collect(Collectors.toSet()))
                        .compact();
    }


    public UserDto validateToken (String token)
    {
        try {
            if(!jwtParser.isSigned(token))
            {
                return null;
            }

            Claims claims = jwtParser.parseClaimsJws(token).getBody();

            UserDto user =  user_service.getUserByUserName(claims.getSubject());

            boolean tokenIsValid = claims.getExpiration().after(new Date()) && claims.getExpiration().after(claims.getIssuedAt());

            if(tokenIsValid)
            {
                return user;
            }

            return null;

        }
            catch (Exception ex)
        {
            return null;
        }
    }
}
