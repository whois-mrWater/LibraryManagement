package com.library.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.library.model.User;
import com.library.repository.UserRepository;

// Phải có đủ annotation này
@Component // ← bắt buộc
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("LIBRARIAN");
            admin.setEmail("admin@library.com");
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println("✅ Tạo tài khoản admin thành công!");
        } else {
            System.out.println("ℹ️ Tài khoản admin đã tồn tại.");
        }
    }
}
