package com.library.repository;

import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Security dùng cái này để đăng nhập
    Optional<User> findByUsername(String username);

    // Kiểm tra trùng khi tạo tài khoản mới
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // Tìm theo email (nếu sau này thêm "quên mật khẩu")
    Optional<User> findByEmail(String email);
}
