package hello.login.domain.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Member {

    private Long id;

    @NotEmpty
    private String loginId; // 사용자가 입력하는 로그인 ID
    @NotEmpty
    private String name;   // 사용자이름
    @NotEmpty
    private String password;
}
