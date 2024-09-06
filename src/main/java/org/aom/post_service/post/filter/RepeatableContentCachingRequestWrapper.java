package org.aom.post_service.post.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

public class RepeatableContentCachingRequestWrapper extends ContentCachingRequestWrapper {
    public RepeatableContentCachingRequestWrapper (HttpServletRequest request) {
        super(request);
    }
}
