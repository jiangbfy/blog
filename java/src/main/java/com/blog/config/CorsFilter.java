package com.blog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CorsFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest resq = (HttpServletRequest) request;
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        resp.addHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers,Content-Length,Origin, Host, Connection, Accept-Encoding, Accept-Language, Keep-Alive, User-Agent, Content-Type, token");
        resp.addHeader("Access-Control-Expose-Headers", "verify");

        if (resq.getMethod().equals("OPTIONS")) {
            return;
        }
        filterChain.doFilter(resq, resp);
    }
}
