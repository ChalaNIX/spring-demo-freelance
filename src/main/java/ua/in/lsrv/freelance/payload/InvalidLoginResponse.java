package ua.in.lsrv.freelance.payload;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {
    private String username;
    private String password;

    public InvalidLoginResponse() {
        username = "Invalid username";
        password = "Invalid password";
    }
}
