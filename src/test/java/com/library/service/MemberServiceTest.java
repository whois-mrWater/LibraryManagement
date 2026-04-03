package com.library.service;

import com.library.model.Member;
import com.library.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * PERSON B - Member Service Unit Tests
 */
@DisplayName("Member Service Tests")
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testMember = new Member();
        testMember.setId(1L);
        testMember.setFullName("Test Member");
        testMember.setPhone("01234567890");
        testMember.setEmail("test@example.com");
        testMember.setJoinDate(LocalDate.now());
        testMember.setActive(true);
    }

    @Test
    @DisplayName("TC001 - Lấy tất cả thành viên")
    void testGetAllMembers() {
        List<Member> members = Arrays.asList(testMember);
        when(memberRepository.findAll()).thenReturn(members);

        List<Member> result = memberService.getAllMembers();

        assertEquals(1, result.size());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("TC002 - Lấy thành viên hoạt động")
    void testGetActiveMembers() {
        List<Member> activeMembers = Arrays.asList(testMember);
        when(memberRepository.findByActive(true)).thenReturn(activeMembers);

        List<Member> result = memberService.getActiveMembers();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("TC003 - Đăng ký thành viên mới")
    void testRegisterMember() {
        when(memberRepository.findByPhone("01234567890")).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        Member result = memberService.registerMember(testMember);

        assertNotNull(result);
        assertNotNull(result.getMemberCode());
        assertTrue(result.isActive());
    }

    @Test
    @DisplayName("TC004 - Cập nhật thành viên")
    void testUpdateMember() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        Member updated = new Member();
        updated.setFullName("Updated Name");
        Member result = memberService.updateMember(1L, updated);

        assertNotNull(result);
    }

    @Test
    @DisplayName("TC005 - Đăng nhập bằng SĐT")
    void testLogin() {
        when(memberRepository.findByPhone("01234567890")).thenReturn(Optional.of(testMember));

        Member result = memberService.login("01234567890", "password");

        assertNotNull(result);
        assertEquals("01234567890", result.getPhone());
    }

    @Test
    @DisplayName("TC006 - Tìm thành viên theo mã")
    void testFindByMemberCode() {
        testMember.setMemberCode("MEM-12345");
        when(memberRepository.findByMemberCode("MEM-12345")).thenReturn(Optional.of(testMember));

        Member result = memberService.findByMemberCode("MEM-12345");

        assertNotNull(result);
    }

    @Test
    @DisplayName("TC007 - Tìm thành viên theo SĐT")
    void testFindByPhone() {
        when(memberRepository.findByPhone("01234567890")).thenReturn(Optional.of(testMember));

        Member result = memberService.findByPhone("01234567890");

        assertNotNull(result);
    }

    @Test
    @DisplayName("TC008 - Vô hiệu hóa thành viên")
    void testDeactivateMember() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        memberService.deactivateMember(1L);

        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("TC009 - Kích hoạt lại thành viên")
    void testReactivateMember() {
        testMember.setActive(false);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        memberService.reactivateMember(1L);

        verify(memberRepository, times(1)).save(any(Member.class));
    }
}
