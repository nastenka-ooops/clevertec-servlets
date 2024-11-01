package ru.clevertec.calculator;

import jakarta.servlet.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.logging.Logger;

public class LoggingFilter implements Filter {
    private static final Logger LOG = Logger.getLogger(LoggingFilter.class.getName());

    protected void logRequestParameter(String paramName, String paramValues) {
        LOG.info("Request parameter " + paramName + ": " + paramValues);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValues = Arrays.toString(request.getParameterValues(paramName));
            logRequestParameter(paramName, paramValues);
        }
        chain.doFilter(request, response);
    }
}
