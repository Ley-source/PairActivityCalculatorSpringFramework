package activity.pair.calculator;

import org.springframework.stereotype.Service; // imports @service annotation sa spring para pwede magamit

import java.util.Stack;

@Service // ginawa niya yung class as a Spring bean
public class CalculatorService {

    public double evaluate(String expression) {  // public method called by the app, tinatanggal niya yung spaces then calculates the expression
        try {
            return evaluateExpression(expression.replaceAll("\\s+", "")); // private method, parses and calculates the expressionn
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid expression: " + e.getMessage());
        }
    }

    private double evaluateExpression(String expr) {
        Stack<Double> numbers = new Stack<>(); // evaluate and stores numbers
        Stack<Character> operators = new Stack<>(); // stores operators and order of operationss

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') { // can identify whole numbers and decimals
                StringBuilder num = new StringBuilder();
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    num.append(expr.charAt(i++));
                }
                i--;
                numbers.push(Double.parseDouble(num.toString()));
            } else if (c == '(') { // yung opening parenthesis pinapasok sa operator stack
                operators.push(c); // then calculates lahat ng na sa loob ng parenthesis
            } else if (c == ')') {
                while (operators.peek() != '(') {
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop();
            } else if (isOperator(c)) { // checks the characters if + - * / or ^
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            } else if (expr.startsWith("sqrt", i)) { // checks if the current part ng expression ay yung word na sqrt
                i += 4; // skips the 4 letters of sqrt
                if (expr.charAt(i) != '(') throw new RuntimeException("Expected ( after sqrt");
                int start = ++i;
                int paren = 1;
                while (paren > 0) {
                    if (expr.charAt(i) == '(') paren++;
                    else if (expr.charAt(i) == ')') paren--;
                    i++;
                }
                double val = evaluateExpression(expr.substring(start, i - 1)); // calculates the value inside sqrt
                numbers.push(Math.sqrt(val)); // calculates the square root and reveals the result 
                i--; // ito aayusin yung position so that the main loop continues
            }
        }
// methods that help the calculator follow the correct order of operations, yung PEMDAS
        while (!operators.isEmpty()) { // reads the whole expression, then calculates any remaining operations
            numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop(); // binalik niya lang final result nung expression na nilagay ng user
    }

    private boolean isOperator(char c) { // checks if the characters are math operators
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private int precedence(char op) { // decides the priority of operators 
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> 0;
        };
    }

    private double applyOp(char op, double b, double a) { // performs the actual math operation 
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) throw new ArithmeticException("Division by zero"); // stops the calculation and shows an error if the user tries to divide 0
                yield a / b; // returns division result 
            }
            case '^' -> Math.pow(a, b); // calculates the power, like 2^2 = 4 
            default -> 0; // balik lang 0 kung di mabasa yung operator
        };
    }
}
