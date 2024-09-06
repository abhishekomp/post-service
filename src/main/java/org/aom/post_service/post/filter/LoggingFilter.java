package org.aom.post_service.post.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

//@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    String requestBody = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        logger.info("LoggingFilter::doFilterInternal() was invoked");
        HttpRequestDto requestDto = new HttpRequestDto();

        try {
            //if (RequestMethod.POST.name().equalsIgnoreCase(request.getMethod()) && requestBody != null)
            if (RequestMethod.POST.name().equalsIgnoreCase(request.getMethod())) { //This line and validation is useful for me [requestBody != null]
                requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                //requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            }
            //Do all JWT control
            String requestURI = request.getRequestURI();
            String method = request.getMethod();
            requestDto.setRequestURI(requestURI);
            requestDto.setMethod(method);
            requestDto.setBody(requestBody);
        }catch (IOException ie) {
            responseError("302", response, ie);
        } finally {
            try {
                ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
                filterChain.doFilter(request, responseWrapper);
                saveResponse(responseWrapper, requestDto);
            } catch (ServletException | IOException se) {
                responseError("302", response, se);
            }
        }
    }


    private void saveResponse(ContentCachingResponseWrapper responseWrapper, HttpRequestDto requestDto) {
        try {
            HttpRequestDto responseDto = new HttpRequestDto();
            responseDto.setStatus(responseWrapper.getStatus());
            byte[] responseArray = responseWrapper.getContentAsByteArray();
            String responseBody = new String(responseArray, responseWrapper.getCharacterEncoding());
            responseDto.setBody(responseBody);
            responseWrapper.copyBodyToResponse();
        } catch (Exception e) {
            logger.error("Error ServletException | IOException in CustomAuthorizationFilter.saveResponse", e);
        }
    }

    private void responseError(String code, HttpServletResponse response, Exception e) {
        try {
            Map<String, Object> error = new HashMap<>();
            error.put("log", code);
            error.put("message", e.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
            e.printStackTrace();
        } catch (IOException ie) {
            logger.error("Error IOException in HttpLoggingFilter.responseError:", ie);
        }
    }


    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
}
