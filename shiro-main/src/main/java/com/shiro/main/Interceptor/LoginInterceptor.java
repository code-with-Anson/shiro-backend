//package com.shiro.main.Interceptor;
//
//import com.shiro.main.utils.JwtUtil;
//import com.shiro.main.utils.UserContext;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//TODO 已弃用，现在改用网关进行拦截，同时使用GatewayIdInterceptor进行网关传入的id拦截
//@RequiredArgsConstructor
//public class LoginInterceptor implements HandlerInterceptor {
//
//    private final JwtUtil jwtUtil;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        // 1.获取请求头中的 token
//        String token = request.getHeader("authorization");
//        // 2.校验token
//        Long userId = jwtUtil.parseToken(token);
//        // 3.存入上下文
//        UserContext.setUser(userId);
//        // 4.放行
//        return true;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        // 清理用户
//        UserContext.removeUser();
//    }
//}
