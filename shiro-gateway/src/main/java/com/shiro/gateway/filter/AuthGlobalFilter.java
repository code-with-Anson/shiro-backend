package com.shiro.gateway.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.AntPathMatcher;
import com.shiro.gateway.config.AuthProperties;
import com.shiro.gateway.exception.UnauthorizedException;
import com.shiro.gateway.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private final JwtUtil jwtUtil;

    private final AuthProperties authProperties;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取Request
        ServerHttpRequest request = exchange.getRequest();

        //2.判断是否需要拦截
        if (isExclude(request.getURI().getPath())) {
            //不需要拦截，放行
            System.out.println("网关放行请求: " + request.getURI().getPath());
            return chain.filter(exchange);
        }

        //3.获取请求头的token
        String token = null;
        List<String> headers = request.getHeaders().get("authorization");
        if (CollUtil.isNotEmpty(headers)) {
            token = headers.get(0);
        }
        // 4.校验并解析token
        Long userId = null;
        try {
            userId = jwtUtil.parseToken(token);

        } catch (UnauthorizedException e) {
            // token无效，抛出异常
            ServerHttpResponse response = exchange.getResponse();
            response.setRawStatusCode(401);
            return response.setComplete();
        }
        // 5.如果有效，传递用户信息
        String userInfo = String.valueOf(userId);
        ServerWebExchange ex = exchange.mutate()
                .request(request.mutate().header("user-info", userInfo).build())
                .build();
        // 6.放行
        System.out.println("网关放行请求: " + request.getURI().getPath() + ", 用户ID: " + userInfo);
        return chain.filter(ex);

    }

    private boolean isExclude(String antPath) {
        for (String pathPattern : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(pathPattern, antPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
