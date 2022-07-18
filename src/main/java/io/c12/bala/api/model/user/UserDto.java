package io.c12.bala.api.model.user;

import io.c12.bala.api.model.constant.UserStatus;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UserDto {

    @Schema(name = "id", title = "Unique Identifier for User", description = "Unique Identifier generated suing NanoId with prefix 'usr_'", readOnly = true, maxLength = 30, example = "usr_wYg611rE_lU4deIRvpodsrsWZ")
    private String id;

    @Schema(name = "firstName", title = "User First Name", required = true, example = "John")
    @NotBlank(message = "First Name is required")
    private String firstName;

    @Schema(name = "lastName", title = "User Last Name", required = true, example = "Doe")
    @NotBlank(message = "Last Name is required")
    private String lastName;

    @Schema(name = "email", title = "User email address", required = true, example = "john.doe@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    private String email;

    @Schema(name = "status", title = "User current status", readOnly = true, example = "ACTIVE")
    private UserStatus status;

    @Schema(name = "birthDate", title = "Date of birth", description = "User date of birth in YYYY-MM-dd format", required = true, example = "1997-05-18")
    @NotNull(message = "User Date of Birth is required")
    private LocalDate birthDate;
}
