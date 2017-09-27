package org.spearhead.residency;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ResidencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResidencyApplication.class, args);
    }

    @Bean
    CommandLineRunner init() {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                System.out.println(this.getClass().getName());
            }
        };
    }
}
