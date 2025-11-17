package com.theshireofpaws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TheshireofpawsApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TheshireofpawsApplication.class, args);
        System.out.println("\n" +
            "╔═══════════════════════════════════════════════════════╗\n" +
            "║                                                       ║\n" +
            "║         Dog Adoption Backend Running!                 ║\n" +
            "║                                                       ║\n" +
            "║  API:           http://localhost:8080/api             ║\n" +
            "║  H2 Console:    http://localhost:8080/h2-console      ║\n" +
            "║                                                       ║\n" +
            "║  Admin Login:                                         ║\n" +
            "║  Email:    admin@theshireofpaws.com                   ║\n" +
            "║  Password: admin123                                   ║\n" +
            "║                                                       ║\n" +
            "╚═══════════════════════════════════════════════════════╝\n"
        );
    }
}