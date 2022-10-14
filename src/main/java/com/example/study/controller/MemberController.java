package com.example.study.controller;

import com.example.study.domain.Member;
import com.example.study.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController( MemberService memberService ) {
        this.memberService = memberService;
    }

    @GetMapping(value = "/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping( value = "/members/new")
    public String create( MemberForm memberForm ) {

        Member member = new Member();
        member.setName( memberForm.getName() );

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping(value="/members")
    public String list(Model model) {
        List<Member> memberList = memberService.findMembers();
        model.addAttribute("members", memberList);
        return "members/memberList";
    }
}
