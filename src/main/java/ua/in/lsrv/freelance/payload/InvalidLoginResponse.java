package ua.in.lsrv.freelance.payload;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {
    private final String username;
    private final String password;

    public InvalidLoginResponse() {
        username = "Invalid username";
        password = "Invalid password";
    }
}
