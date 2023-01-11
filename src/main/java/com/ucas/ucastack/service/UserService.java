package com.ucas.ucastack.service;

import com.ucas.ucastack.entity.User;

import javax.servlet.http.HttpSession;

public interface UserService {
	
    // 注册
    String register(String loginName, String password, String nickName);

    // 登录
    String login(String loginName, String passwordMD5, HttpSession httpSession);
    
    /**
     *  获取用户信息
     */
    User getUserById(Long publishUserId);

    /**
     * 修改用户信息
     */
    Boolean updateUserInfo(User user, HttpSession httpSession);

    /**
     * 修改头像
     */
    Boolean updateUserHeadImg(User user, HttpSession httpSession);
    
    /**
     * 修改密码
     */
    Boolean updatePassword(Long userId, String originPWD, String newPWD);
}
