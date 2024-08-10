package hellospring.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void shouldSignUpMember() {
        // Given
        Member member = createMember("hello");

        // When
        Long saveId = memberService.join(member);

        // Then
        Member findMember = memberRepository.findById(saveId)
                .orElseThrow(() -> new AssertionError("Member not found"));
        assertEquals(member.getName(), findMember.getName());
    }

    @Test
    void shouldThrowExceptionWhenDuplicateMember() {
        // Given
        Member member1 = createMember("spring");
        Member member2 = createMember("spring");

        // When
        memberService.join(member1);

        // Then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));

        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        return member;
    }
}
