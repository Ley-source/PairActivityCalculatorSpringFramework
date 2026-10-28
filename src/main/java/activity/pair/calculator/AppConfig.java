package activity.pair.calculator; 

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration; // ito ibig sabihin na ang class na ito ay Spring

@Configuration // ito naman sinasabe sa spring yung configucation information, 
@ComponentScan(basePackages = "activity.pair.calculator")
public class AppConfig {
}

// ito bali yung entry point ng calculator, mag aactivate yung spring component scanning and gagana rin yung dependency injection 
