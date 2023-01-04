package com.ucas.ucastack.service;

import com.ucas.ucastack.entity.User;

import javax.servlet.http.HttpSession;

public interface UserService {
    /**
     *  获取用户信息
     */
    User getUserById(Long publishUserId);

    /**
     * 修改用户信息
     */
    Boolean updateUserInfo(User User, HttpSession httpSession);

    /**
     * 修改头像
     */
    Boolean updateUserHeadImg(User User, HttpSession httpSession);

    /**
     * 修改密码
     */
    Boolean updatePassword(Long userId, String originPWD, String newPWD);
}
