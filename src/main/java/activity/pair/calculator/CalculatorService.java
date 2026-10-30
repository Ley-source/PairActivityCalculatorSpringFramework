package activity.pair.calculator;

import org.springframework.stereotype.Service;

import java.util.Stack;

@Service
public class CalculatorService {

    public double evaluate(String expression) {
        try {
            String cleaned = expression.replaceAll("\\s+", "");
            if (cleaned.isEmpty()) {
                throw new IllegalArgumentException("Empty expression");
            }
            return evaluateExpression(cleaned);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid expression");
        }
    }

    private double evaluateExpression(String expr) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        boolean lastWasNumber = false;
        boolean lastWasCloseParen = false;

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                if (lastWasCloseParen) {
                    throw new IllegalArgumentException("Missing operator");
                }
                StringBuilder num = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    num.append(expr.charAt(i++));
                }
                i--;
                numbers.push(Double.parseDouble(num.toString()));
                lastWasNumber = true;
                lastWasCloseParen = false;
            } else if (c == '(') {
                if (lastWasNumber || lastWasCloseParen) {
                    throw new IllegalArgumentException("Missing operator");
                }
                operators.push(c);
                lastWasNumber = false;
                lastWasCloseParen = false;
            } else if (c == ')') {
                if (!lastWasNumber && !lastWasCloseParen) {
                    throw new IllegalArgumentException("Invalid parentheses");
                }
                while (!operators.isEmpty() && operators.peek() != '(') {
                    if (numbers.size() < 2) throw new IllegalArgumentException("Invalid expression");
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
                }
                if (operators.isEmpty() || operators.peek() != '(') {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
                operators.pop();
                lastWasNumber = false;
                lastWasCloseParen = true;
            } else if (isOperator(c)) {
                if (!lastWasNumber && !lastWasCloseParen) {
                    throw new IllegalArgumentException("Invalid operator position");
                }
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    if (numbers.size() < 2) throw new IllegalArgumentException("Invalid expression");
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
                lastWasNumber = false;
                lastWasCloseParen = false;
            } else {
                throw new IllegalArgumentException("Invalid character");
            }
        }

        if (!lastWasNumber && !lastWasCloseParen) {
            throw new IllegalArgumentException("Incomplete expression");
        }

        while (!operators.isEmpty()) {
            if (operators.peek() == '(') {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            if (numbers.size() < 2) throw new IllegalArgumentException("Invalid expression");
            numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
        }

        if (numbers.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }

        return numbers.pop();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private int precedence(char op) {
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
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
            default -> 0;
        };
    }
}
