package com.example.springdatajpa.dto;

import com.example.springdatajpa.entity.Member;

public record MemberDto(Long id, String username, String teamName) {

    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName());
    }
}
