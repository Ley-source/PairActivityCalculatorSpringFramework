package activity.pair.calculator;

import org.springframework.stereotype.Service;

import java.util.Stack;

@Service 
public class CalculatorService {

    public double evaluate(String expression) { // toh yung main method every time tatawag yung App.java, tapos cacalculate niya expression na ininput ni user
        try {
            return evaluateExpression(expression.replaceAll("\\s+", "")); // toh naman yung internal method na nagbabasa at iniintindi yung expression, tapos do the math
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid expression: " + e.getMessage());
        }
    }

    private double evaluateExpression(String expr) {
        Stack<Double> numbers = new Stack<>(); // dito naka store yung numbers habang nag eevaluate ng expression
        Stack<Character> operators = new Stack<>(); // dito naman yung operators shi and follows the order of operations

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') { // reads the number and decimals
                StringBuilder num = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    num.append(expr.charAt(i++));
                }
                i--;
                numbers.push(Double.parseDouble(num.toString()));
            } else if (c == '(') {
                operators.push(c);              // calculates lahat ng na sa loob ng parenthesis
            } else if (c == ')') {
                while (operators.peek() != '(') {
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop();
            } else if (isOperator(c)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }
        }

        while (!operators.isEmpty()) { // tinatapos niya mga natirang calculations after ng loop
            numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop(); // bigay result
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private int precedence(char op) { //applying pemdas, what operator should be calculated first
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> 0;
        };
    }

    private double applyOp(char op, double b, double a) { // performs the actual math operations
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> { // divides 2 numbers and stops pag mag didivde ng zero
                if (b == 0) throw new ArithmeticException("Division by zero");
                yield a / b;
            }
            default -> 0;
        };
    }
}
        };
    }
}
