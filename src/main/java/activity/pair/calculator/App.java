package activity.pair.calculator;

import org.springframework.context.annotation.AnnotationConfigApplicationContext; // imports the class needed to start the spring conatianer, pure spring framework

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = // Starts the spring loC container and loads the configuration from Appconfig
                new AnnotationConfigApplicationContext(AppConfig.class);

        CalculatorService calculator = context.getBean(CalculatorService.class); // gets the calculatorservice bean from spring
        Scanner scanner = new Scanner(System.in); // sysmpre scanner uwu

        System.out.println("========================================="); // header ng calculaotr 
        System.out.println("     CALCULATOR");
        System.out.println("     Spring Framework");
        System.out.println(" Prepared by: Harley G. Balotro & Ara Roxas");
        System.out.println("=========================================");
        System.out.println("Type expressions (example: 2 + 3 * 4)");
        System.out.println("Supports: + - * / ^ ( ) sqrt()");
        System.out.println("Type 'exit' to quit\n");

        while (true) { // keeps the calculator runningg
            System.out.print(">> "); // dito mag i-input yung user
            String input = scanner.nextLine().trim(); // babasahin ng scanner yung user input, at tinatanggal yung mga extra spaces

            if (input.equalsIgnoreCase("exit")) { // pag nag quit na user
                System.out.println("Goodbye!");
                break; // stops the program
            }

            if (input.isEmpty()) continue; // skips empty input and ask again

            try { 
                double result = calculator.evaluate(input); // calls the spring bean to calculate the results 
                System.out.println("= " + formatResult(result));
            } catch (Exception e) { // shows an error message if the expression is invalid 
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
        }                                         // basically tatanggalin niya yung .0 kung result ay whole number
        return String.valueOf(result);
    }
}
