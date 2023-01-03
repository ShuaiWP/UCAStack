package com.ucas.ucastack.service.impl;

import com.ucas.ucastack.entity.User;
import com.ucas.ucastack.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class UserServiceImpl implements UserService {

    @Override
    public User getUserById(Long publishUserId) {
        User user = new User((long)4, "3840@qq.com", "e10adc3949ba59abbe56e057f20f883e", "lucy", "/images/avatar/default.png", "未知", "未知", "这个人很懒，什么都没留下~", (byte)0, new Date("Tue Jan 03 22:13:53 CST 2023"), new Date("Wed Nov 16 19:01:38 CST 2022"));
        return user;
    }
}
