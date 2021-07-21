package ua.in.lsrv.freelance.security;

public class SecurityConstants {
    public static final String SIGN_UP_ULR = "/api/auth/**";

    public static final String SECRET = "SecretKeyGenJWT";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
    public static final Integer EXPIRATION_TIME = 3600_000;//60 min
}
