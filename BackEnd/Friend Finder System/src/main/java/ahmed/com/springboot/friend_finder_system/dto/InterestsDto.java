package ahmed.com.springboot.friend_finder_system.dto;

import ahmed.com.springboot.friend_finder_system.eNum.InterestCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Interests Data Transfer Object")
public class InterestsDto {

    @Schema(description = "Interest ID", example = "1")
    private Long id;

    @NotNull(message = "Name is required")
    @Schema(
            description = "Interest category",
            example = "SPORTS",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private InterestCategory category;

    @Schema(
            description = "Icon URL or icon name for the interest",
            example = "football.png"
    )
    private String icon;

    // _______________relations__________________________________
}