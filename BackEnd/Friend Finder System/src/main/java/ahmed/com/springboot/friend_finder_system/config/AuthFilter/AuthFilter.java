package ahmed.com.springboot.friend_finder_system.config.AuthFilter;

import ahmed.com.springboot.friend_finder_system.dto.UserDto;
import ahmed.com.springboot.friend_finder_system.jwt.TokenHandler;
import ahmed.com.springboot.friend_finder_system.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {




    private final TokenHandler tokenHandler;



    // TODO _________________________ shouldNotFilter _________________________
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
    {
        return request.getRequestURI().contains("/api/auth");
    }


    // TODO _________________________ doFilterInternal _________________________
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String token = request.getHeader("Authorization");
        if (Objects.isNull(token) || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        token = token.substring(7);

        UserDto userDto = tokenHandler.validateToken(token);

        List<SimpleGrantedAuthority> roles = userDto.getRoles().stream().map(rolesDto ->
            new SimpleGrantedAuthority("Role_" + rolesDto.getName())).collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDto, null, roles);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
