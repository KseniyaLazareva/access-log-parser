public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE;

    public static HttpMethod fromString(String method) throws IllegalArgumentException {
        return HttpMethod.valueOf(method.toUpperCase());
    }
}