package ds.microservice.chat.security.util;


public final class SecurityConstants {
    public static final Integer PASSWORD_STRENGTH = 10;
    public static final String AUTH_PATHS_TO_SKIP = "/auth/**";
    public static final String LOGIN_URL = "/auth/login";
    public static final String REGISTER_URL = "/auth/register";
    public static final String JWT_TOKEN = "jwt-token";
    public static final String[] GENERIC_PATHS_TO_SKIP = {
            "/show/v1/message",
            "/user/v1/message",
            "/comment/v1/message",
            "/order/v1/message",
            "/show/v1/all-paged",
            "/ws/**",

    };
}