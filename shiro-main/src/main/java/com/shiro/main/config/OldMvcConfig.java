//package com.shiro.main.config;
//
//import cn.hutool.core.collection.CollUtil;
//import com.shiro.main.Interceptor.LoginInterceptor;
//import com.shiro.main.utils.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.List;
//
//TODO 已弃用，现在改用网关进行拦截，同时使用GatewayIdInterceptor进行网关传入的id拦截
//TODO 现在使用 NewMvcConfig 进行网关拦截
//@Configuration
//@RequiredArgsConstructor
//@EnableConfigurationProperties(AuthProperties.class)
//public class OldMvcConfig implements WebMvcConfigurer {
//
//    private final JwtUtil jwtUtil;
//    private final AuthProperties authProperties;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 1.添加拦截器
//        LoginInterceptor loginInterceptor = new LoginInterceptor(jwtUtil);
//        InterceptorRegistration registration = registry.addInterceptor(loginInterceptor);
//        // 2.配置拦截路径 默认会拦截所有路径
//        List<String> includePaths = authProperties.getIncludePaths();
//        if (CollUtil.isNotEmpty(includePaths)) {
//            registration.addPathPatterns(includePaths);
//        }
//        // 3.配置放行路径
//        List<String> excludePaths = authProperties.getExcludePaths();
//        if (CollUtil.isNotEmpty(excludePaths)) {
//            registration.excludePathPatterns(excludePaths);
//        }
//        registration.excludePathPatterns(
//                "/error",
//                "/favicon.ico",
//                "/v2/**",
//                "/v3/**",
//                "/swagger-resources/**",
//                "/webjars/**",
//                "/doc.html"
//        );
//
//    }
//}
