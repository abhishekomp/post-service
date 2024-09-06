package org.aom.post_service.post.config;

import org.aom.post_service.post.interceptor.PostControllerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private PostControllerInterceptor postControllerInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(postControllerInterceptor)
                .addPathPatterns("/post-api/*")
                .excludePathPatterns("/post-api/updatePost/*");
    }

//    @Override
//    public void addInterceptors(final InterceptorRegistry registry) {
//        registry.addInterceptor(postControllerInterceptor)
//                .addPathPatterns("/post-api/*")
//                .excludePathPatterns("/post-api/createPost", "/post-api/updatePost/*");
//    }
}
