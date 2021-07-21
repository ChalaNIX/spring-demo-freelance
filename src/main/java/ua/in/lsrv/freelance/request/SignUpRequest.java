package ua.in.lsrv.freelance.request;

import lombok.Data;
import ua.in.lsrv.freelance.annotation.PasswordMatches;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignUpRequest {
    @NotBlank(message = "Please enter your username")
    private String username;

    @NotEmpty(message = "Password is required")
    @Size(min = 8)
    private String password;

    private String confirmPassword;
}
