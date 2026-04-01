package com.library.repository;

import com.library.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * PERSON B - Member Repository
 * Quản lý lưu trữ mầu dữ liệu Thành viên
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberCode(String memberCode);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByPhone(String phone); // Đăng nhập bằng số điện thoại
    List<Member> findByActive(boolean active);
}
