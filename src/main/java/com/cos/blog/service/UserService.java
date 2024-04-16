package com.cos.blog.service;

import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌. IoC를 해줌.
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public int join(User user) {
        try {
            userRepository.save(user);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("UserService: 회원가입(): " + e.getMessage());
        }
        return -1;
    }

}
