package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        //save
        var member = new Member("memberV1", 10000);
        Member savedMember = repository.save(member);

        //findById
        Member foundMember = repository.findById(member.getMemberId());
        log.info("foundMember={}", foundMember);
        log.info("member == foundMember {} = {}", member == foundMember);
        log.info("member equals foundMember {} = {}", member.equals(foundMember));

        assertThat(foundMember).isEqualTo(member);

        //update: money: 10000 -> 20000
        repository.update(member.getMemberId(), 20000);
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        repository.delete(member.getMemberId());
        assertThrows(NoSuchElementException.class,
                () -> repository.findById(member.getMemberId()));
    }
}