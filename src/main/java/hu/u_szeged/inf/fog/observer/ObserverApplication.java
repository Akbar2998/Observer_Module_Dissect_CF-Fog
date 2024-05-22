package hu.u_szeged.inf.fog.observer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"hu.u_szeged.inf.fog.simulator.executor", "hu.u_szeged.inf.fog.observer"})
public class ObserverApplication {
    public static void main(String[] args) {
        SpringApplication.run(ObserverApplication.class, args);
    }
}

