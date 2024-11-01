package ru.clevertec.calculator;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Pattern pattern;

    @Override
    public void init() {
        String regex = getServletContext().getInitParameter("expression-regex");
        pattern = Pattern.compile(regex);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.getSession().setAttribute("error", "Please use the POST method for calculations");
        response.sendRedirect("error.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String action = request.getParameter("action");
        if ("clearHistory".equals(action)) {
            request.getSession().removeAttribute("history");
            response.sendRedirect("result.jsp");
            return;
        }

        String expression = request.getParameter("expression");
        Matcher matcher = pattern.matcher(expression);
        if (!matcher.matches()) {
            request.getSession().setAttribute("error", "Invalid expression: \"" + expression + "\"");
            response.sendRedirect("error.jsp");
            return;
        }

        try {
            String operator = matcher.group("operation");
            double operand1 = Double.parseDouble(matcher.group("operand1"));
            double operand2 = Double.parseDouble(matcher.group("operand2"));
            double result = evaluateExpression(operand1, operand2, operator);

            addToHistory(request.getSession(), expression + "=" + result);
            request.getSession().removeAttribute("error");
            response.sendRedirect("result.jsp");
        } catch (Exception e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect("error.jsp");
        }
    }

    private double evaluateExpression(double operand1, double operand2, String operator) {
        return switch (operator) {
            case "+" -> operand1 + operand2;
            case "-" -> operand1 - operand2;
            case "*" -> operand1 * operand2;
            case "/" -> {
                if (operand2 == 0) throw new ArithmeticException("Division by zero!");
                yield operand1 / operand2;
            }
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };
    }

    private void addToHistory(HttpSession session, String result) {
        List<String> history = (List<String>) session.getAttribute("history");
        if (history == null) {
            history = new ArrayList<>();
        }
        history.add(result);
        session.setAttribute("history", history);
    }
}