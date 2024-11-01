package ru.clevertec.calculator;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationFilter implements Filter {

    private Pattern pattern;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String regex = filterConfig.getServletContext().getInitParameter("expression-regex");
        pattern = Pattern.compile(regex);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        String expression = httpRequest.getParameter("expression");
        if (expression != null) {
            Matcher matcher = pattern.matcher(expression);

            if (!matcher.matches()) {
                session.setAttribute("error", "Invalid expression format.");
                httpResponse.sendRedirect("error.jsp");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
