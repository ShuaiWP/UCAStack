package com.ucas.ucastack.service.impl;

import com.ucas.ucastack.dao.UserMapper;
import com.ucas.ucastack.entity.User;
import com.ucas.ucastack.service.UserService;
import com.ucas.ucastack.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(Long publishUserId) {
        return userMapper.selectByPrimaryKey(publishUserId);
    }

    @Override
    public Boolean updateUserInfo(User User, HttpSession httpSession) {
        return null;
    }

    @Override
    public Boolean updateUserHeadImg(User User, HttpSession httpSession) {
        return null;
    }

    @Override
    public Boolean updatePassword(Long userId, String originPWD, String newPWD) {
        User user = userMapper.selectByPrimaryKey(userId);
        //当前用户非空且状态正常才可以进行更改
        if (user != null && user.getUserStatus().intValue() == 0) {
            String originalPasswordMd5 = MD5Util.MD5Encode(originPWD, "UTF-8");
            String newPasswordMd5 = MD5Util.MD5Encode(newPWD, "UTF-8");
            //比较原密码是否正确
            if (originalPasswordMd5.equals(user.getPasswordMd5())) {
                //设置新密码并修改
                user.setPasswordMd5(newPasswordMd5);
                if (userMapper.updateByPrimaryKeySelective(user) > 0) {
                    //修改成功则返回true
                    return true;
                }
            }
        }
        return false;
    }
}
