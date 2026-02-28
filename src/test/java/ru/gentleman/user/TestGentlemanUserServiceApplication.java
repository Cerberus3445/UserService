package ru.gentleman.user;

import org.springframework.boot.SpringApplication;

public class TestGentlemanUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(GentlemanUserServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
