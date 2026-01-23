package org.spring.security.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Validated
@RequiredArgsConstructor
public class TestController {


    @RequestMapping("/user1")
    @PreAuthorize("hasAuthority('USER_1_ACCESS')")
    public String user1() {
        return "User 1 accessed";
    }

    @RequestMapping("/user2")
    @PreAuthorize("hasAuthority('USER_2_ACCESS')")
    public String user2() {
        return "User 2 accessed";
    }

    @RequestMapping("/user3")
    @PreAuthorize("hasAuthority('USER_3_ACCESS')")
    public String user3() {
        return "User 3 accessed";
    }

}
