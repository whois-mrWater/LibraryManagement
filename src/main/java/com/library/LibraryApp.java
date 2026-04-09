package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * DIEM KHOI DONG CHINH CUA TOAN BO DU AN
 * Chay class nay la chay ca web server
 */
@SpringBootApplication
public class LibraryApp {
    public static void main(String[] args) {
        SpringApplication.run(LibraryApp.class, args);
        System.out.println("=== Thư viện đang chạy tại đây: http://localhost:8080 ===");
    }
}
