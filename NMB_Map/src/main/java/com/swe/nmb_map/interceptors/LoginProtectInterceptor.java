package com.swe.nmb_map.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swe.nmb_map.utils.JwtHelper;
import com.swe.nmb_map.utils.Result;
import com.swe.nmb_map.utils.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
* @program: spring-headline-part-new
*
* @description: 登录包含拦截器，检查请求头是否包含有效token    有效放行    无效返回504
*
* @author: Xavier
*
* @create: 2025-03-18 21:15
**/
@Component
public class LoginProtectInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtHelper jwtHelper;

    /**
     * 在请求处理之前进行拦截处理
     *
     * @param request  请求对象，用于获取请求头中的token
     * @param response 响应对象，用于向客户端返回处理结果
     * @param handler  处理请求的处理器对象
     * @return boolean 返回值用于指示是否应该继续执行请求处理链
     * @throws Exception 抛出异常表示处理过程中遇到错误
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从请求头中获取token
        String token = request.getHeader("token");
        //检查是否有效
        boolean expiration = jwtHelper.isExpiration(token);
        //有效，放行
        if (!expiration) {
            return true;
        }
        //无效，返回504
        Result<Object> result = Result.build(null, ResultCodeEnum.NOTLOGIN);
        //用response写回
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().print(json);

        return false;
    }
}
