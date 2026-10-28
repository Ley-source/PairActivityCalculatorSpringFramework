package activity.pair.calculator;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        CalculatorService calculator = context.getBean(CalculatorService.class);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("     CALCULATOR");
        System.out.println("     Spring Framework");
        System.out.println("=========================================");
        System.out.println("Type expressions (example: 2 + 3 * 4)");
        System.out.println("Supports: + - * / ^ ( ) sqrt()");
        System.out.println("Type 'exit' to quit\n");

        while (true) {
            System.out.print(">> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }

            if (input.isEmpty()) continue;

            try {
                double result = calculator.evaluate(input);
                System.out.println("= " + formatResult(result));
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
        context.close();
    }

    // itong function pag yung result ay decimal (Ex.: 2 / 52 = 0.03846153846) pero pag whole number, edi whole number
    private static String formatResult(double result) {
        if (result == Math.floor(result) && !Double.isInfinite(result)) {
            return String.valueOf((long) result);
        }
        return String.valueOf(result);
    }
}
