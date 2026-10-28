package activity.pair.calculator;

import org.springframework.stereotype.Service;

import java.util.Stack;

@Service
public class CalculatorService {

    public double evaluate(String expression) {
        try {
            return evaluateExpression(expression.replaceAll("\\s+", ""));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid expression: " + e.getMessage());
        }
    }

    private double evaluateExpression(String expr) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    num.append(expr.charAt(i++));
                }
                i--;
                numbers.push(Double.parseDouble(num.toString()));
            } else if (c == '(') {
                operators.push(c);
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
            } else if (expr.startsWith("sqrt", i)) {
                i += 4;
                if (expr.charAt(i) != '(') throw new RuntimeException("Expected ( after sqrt");
                int start = ++i;
                int paren = 1;
                while (paren > 0) {
                    if (expr.charAt(i) == '(') paren++;
                    else if (expr.charAt(i) == ')') paren--;
                    i++;
                }
                double val = evaluateExpression(expr.substring(start, i - 1));
                numbers.push(Math.sqrt(val));
                i--;
            }
        }

        while (!operators.isEmpty()) {
            numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private int precedence(char op) {
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> 0;
        };
    }

    private double applyOp(char op, double b, double a) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) throw new ArithmeticException("Division by zero");
                yield a / b;
            }
            case '^' -> Math.pow(a, b);
            default -> 0;
        };
    }
}