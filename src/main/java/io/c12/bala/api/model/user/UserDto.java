package io.c12.bala.api.model.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    private String id;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    private String email;
    private String status;
}
