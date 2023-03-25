package ru.marilka888.jeweller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JewellerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JewellerApplication.class, args);
    }

}
