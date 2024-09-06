package org.aom.post_service.post.filter;

public class HttpRequestDto {
    private String requestURI;
    private String method;
    private String body;

    private int status;

    public HttpRequestDto() {
    }

    public HttpRequestDto(String requestURI, String method, String body, int status) {
        this.requestURI = requestURI;
        this.method = method;
        this.body = body;
        this.status = status;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
