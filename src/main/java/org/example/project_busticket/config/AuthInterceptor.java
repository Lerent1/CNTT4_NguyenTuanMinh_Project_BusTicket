package org.example.project_busticket.config;

import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);

        String role = (session != null)
                ? (String) session.getAttribute("currentRole")
                : null;

        // Public paths
        if (uri.startsWith("/auth")
                || uri.equals("/")
                || uri.startsWith("/css")
                || uri.startsWith("/js")
                || uri.startsWith("/images")) {
            return true;
        }

        // Not logged in
        if (role == null) {
            response.sendRedirect("/auth/login");
            return false;
        }

        // ADMIN only
        if (uri.startsWith("/admin")
                && !uri.startsWith("/admin/tickets")) {

            if (!"ADMIN".equals(role)) {
                response.sendRedirect("/home");
                return false;
            }
        }

        return true;
    }
}