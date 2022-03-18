package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return null일 경우 로그인 실패
     */
    public Member login(String loginId, String password) {
//        Optional<Member> foundMemberOptional = memberRepository.findByLoginId(loginId);
//        Member member = foundMemberOptional.get();  // 실제로는 get 말고 다른 것 쓰는 편이 나음
//        if (member.getPassword().equals(password)) {
//            return member;
//        } else {
//            return null;
//        }

        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}