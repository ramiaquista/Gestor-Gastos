package com.egg.ProyectoFinal;

import com.egg.ProyectoFinal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProyectoFinalApplication {
    
    public static void main(String[] args) {
	SpringApplication.run(ProyectoFinalApplication.class, args);
    }

}
