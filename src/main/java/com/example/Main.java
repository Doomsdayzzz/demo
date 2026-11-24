package com.example;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/calc")
public class Main extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");

        String op1 = request.getParameter("operand1");
        String op2 = request.getParameter("operand2");
        String operation = request.getParameter("op");

        HttpSession s = request.getSession();
        if (s.getAttribute("count") == null) {
            s.setAttribute("count", 1);
        } else {
            var c = s.getAttribute("count");
            int count = Integer.parseInt(c.toString());
            count++;
            s.setAttribute("count", count);
        }
        String result;
        try {
            result = op1 + " " + operation + " " + op2 + " = " + calc(op1, op2, operation);
        } catch (NumberFormatException e) {
            result = "Ошибка: введите целые числа";
        } catch (ArithmeticException e) {
            result = "Ошибка: деление на ноль";
        } catch (IllegalArgumentException e) {
            result = "Ошибка: неизвестная операция " + operation;
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Servlet Calculator</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Result</h1>");
            out.println("<p>" + result + "</p>");
            out.println("<p><a href=\"" + request.getContextPath() + "/\">Назад</a></p>");
            out.println("</br></br>");
            out.println("<p>" +"Счетчик операций: "+ s.getAttribute("count") + "</p>");

            out.println("</body>");
            out.println("</html>");
        }
    }

    public double calc(String op1, String op2, String operation) {
        double a = Double.parseDouble(op1);
        double b = Double.parseDouble(op2);
        IO.println(a + "," + b);
        return switch (operation) {
            case "+" ->
                a + b;
            case "-" ->
                a - b;
            case "*" ->
                a * b;
            case "/" -> {
                if (b == 0) {
                    throw new ArithmeticException("division by zero");
                }
                yield a / b;
            }
            default ->
                throw new IllegalArgumentException("Unknown operation: " + operation);
        };
    }
}
