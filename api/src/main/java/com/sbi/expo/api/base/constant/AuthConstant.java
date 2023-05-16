package com.sbi.expo.api.base.constant;

public class AuthConstant {
    private AuthConstant() {}

    public static final String ADMIN_ROLE_NAME = "ADMIN";

    public static class EntryPoint {
        private EntryPoint() {}

        public static final String ALL = "/**";
        public static final String LOGIN = "/api/v1/login";
        public static final String LOGOUT = "/api/v1/logout";
        public static final String API = "/api/**";
        public static final String REFRESH_TOKEN = "/api/v1/auth/token/refresh";

        public static final String EXAMPLE_ENDPOINT = "/api/v1/example";
        public static final String[] ENTRY_POINT_WHITE_LIST =
                new String[] {LOGIN, REFRESH_TOKEN, Swagger.API, Swagger.DOCS, EXAMPLE_ENDPOINT};
    }

    public static class Field {
        private Field() {}

        public static final String SCOPES = "scopes";
        public static final String ACCESS_TOKEN = "token";
        public static final String REFRESH_TOKEN = "refreshToken";
    }

    public static class Swagger {
        private Swagger() {}

        public static final String API = "/swagger**/**";
        public static final String DOCS = "/v3/api-docs";
    }
}
