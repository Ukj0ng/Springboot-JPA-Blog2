package com.cos.blog.controller.api;

import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/user")
    public ResponseDto<Integer> save(@RequestBody User user) {
        System.out.println("UserApiController: save 호출됨");
        // 실제로 DB에 insert를 하고 아래에서 return이 되면 됨.
        user.setRole(RoleType.USER);
        int result = userService.join(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), result); // JavaObject를 Json으로 변환해서 리턴(Jackson)
    }


//    @PostMapping("/api/user/login")
//    public ResponseDto<Integer> login(@RequestBody User user, HttpSession session) {
//        System.out.println("UserApiController: login호출");
//        User principal = userService.login(user); // principal(접근주체)
//
//        if (principal != null) {
//            session.setAttribute("principal", principal);
//        }
//        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//    }
}
