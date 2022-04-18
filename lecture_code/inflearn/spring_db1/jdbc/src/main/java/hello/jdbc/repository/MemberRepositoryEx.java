package hello.jdbc.repository;

import hello.jdbc.domain.Member;

import java.sql.SQLException;

public interface MemberRepositoryEx {

    public Member save(Member member) throws SQLException;

    public Member findById(String memberId) throws SQLException;

    public void update(String memberId, int money) throws SQLException;

    public void delete(String memberId) throws SQLException;

}
