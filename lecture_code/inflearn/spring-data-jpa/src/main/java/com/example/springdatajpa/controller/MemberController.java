package com.example.springdatajpa.controller;

import com.example.springdatajpa.dto.MemberDto;
import com.example.springdatajpa.entity.Member;
import com.example.springdatajpa.repository.MemberDataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberDataJpaRepository memberRepository;

    @PostConstruct
    protected void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save((new Member("user" + i, 10 + i)));
        }
    }

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberDto::from);
    }
}
