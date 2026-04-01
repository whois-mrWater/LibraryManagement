package com.library.service;

import com.library.model.Member;
import com.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * PERSON B - Member Management Service
 * Chức năng: đăng ký, đăng nhập, quản lý đối tượng thành viên
 */
@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    /** Lấy danh sách tất cả thành viên */
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    /** Lấy danh sách thành viên hoạt động */
    public List<Member> getActiveMembers() {
        return memberRepository.findByActive(true);
    }

    /**
     * Đăng ký thành viên mới - tự động sinh mã thẺ
     */
    public Member registerMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Thông tin thành viên không được null");
        }
        
        if (member.getFullName() == null || member.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được trống");
        }
        
        if (member.getPhone() == null || member.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được trống");
        }
        
        // Kiểm tra số điện thoại đã tồn tại
        Optional<Member> existingMember = memberRepository.findByPhone(member.getPhone());
        if (existingMember.isPresent()) {
            throw new IllegalArgumentException("Số điện thoại đã được đăng ký");
        }
        
        // Tự động sinh mã thẺ dựa trên UUID
        String memberCode = "MEM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        member.setMemberCode(memberCode);
        member.setJoinDate(LocalDate.now());
        member.setActive(true);
        
        return memberRepository.save(member);
    }

    /**
     * Đăng nhập thành viên bằng số điện thoại và mật khẩu
     * Lưu ý: Mật khẩu nên được mã hóa trên client hoặc đã mã hóa được ưu tiên
     */
    public Member login(String phone, String password) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được trống");
        }
        
        Optional<Member> member = memberRepository.findByPhone(phone);
        if (!member.isPresent()) {
            throw new IllegalArgumentException("Số điện thoại không tản tại");
        }
        
        Member foundMember = member.get();
        if (!foundMember.isActive()) {
            throw new IllegalArgumentException("Tài khoản đã bị khóa");
        }
        
        // TODO: Kiểm tra mật khẩu sử dụng BCrypt khi có
        // Nên sử dụng Spring Security và BCryptPasswordEncoder
        
        return foundMember;
    }

    /**
     * Cập nhật thông tin thành viên
     */
    public Member updateMember(Long id, Member updatedMember) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID thành viên không hợp lệ");
        }
        
        Optional<Member> existingMember = memberRepository.findById(id);
        if (!existingMember.isPresent()) {
            throw new IllegalArgumentException("Thành viên không tìm thấy");
        }
        
        Member member = existingMember.get();
        
        if (updatedMember.getFullName() != null && !updatedMember.getFullName().trim().isEmpty()) {
            member.setFullName(updatedMember.getFullName());
        }
        
        if (updatedMember.getEmail() != null && !updatedMember.getEmail().trim().isEmpty()) {
            member.setEmail(updatedMember.getEmail());
        }
        
        return memberRepository.save(member);
    }

    /**
     * Kích hoạt / Khóa tài khoản thành viên
     */
    public Member toggleMemberStatus(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID thành viên không hợp lệ");
        }
        
        Optional<Member> member = memberRepository.findById(id);
        if (!member.isPresent()) {
            throw new IllegalArgumentException("Thành viên không tìm thấy");
        }
        
        Member foundMember = member.get();
        foundMember.setActive(!foundMember.isActive());
        return memberRepository.save(foundMember);
    }

    /**
     * Tậm thời deactivate tài khoản
     */
    public void deactivateMember(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID thành viên không hợp lệ");
        }
        
        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent()) {
            Member foundMember = member.get();
            foundMember.setActive(false);
            memberRepository.save(foundMember);
        }
    }

    /**
     * Kích hoạt lại tài khoản
     */
    public void reactivateMember(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID thành viên không hợp lệ");
        }
        
        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent()) {
            Member foundMember = member.get();
            foundMember.setActive(true);
            memberRepository.save(foundMember);
        }
    }

    /**
     * Tìm thành viên theo mã thẺ
     */
    public Member findByMemberCode(String memberCode) {
        if (memberCode == null || memberCode.trim().isEmpty()) {
            return null;
        }
        Optional<Member> member = memberRepository.findByMemberCode(memberCode);
        return member.orElse(null);
    }

    /**
     * Tìm thành viên theo email
     */
    public Member findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.orElse(null);
    }

    /**
     * Tìm thành viên theo số điện thoại
     */
    public Member findByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        Optional<Member> member = memberRepository.findByPhone(phone);
        return member.orElse(null);
    }

    /**
     * Lấy thành viên theo ID
     */
    public Member getMemberById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID thành viên không hợp lệ");
        }
        Optional<Member> member = memberRepository.findById(id);
        return member.orElse(null);
    }
}
