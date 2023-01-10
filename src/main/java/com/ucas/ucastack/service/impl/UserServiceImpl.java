package com.ucas.ucastack.service.impl;

import com.ucas.ucastack.common.Constants;
import com.ucas.ucastack.common.ServiceResultEnum;  
import com.ucas.ucastack.dao.UserMapper;
import com.ucas.ucastack.entity.User;
import com.ucas.ucastack.service.UserService;
import com.ucas.ucastack.util.MD5Util;
import com.ucas.ucastack.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
	public String register(String loginName, String password, String nickName) {
        if (userMapper.selectByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        
        //注册用户
        User registerUser = new User();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(nickName);
        //默认头像
        registerUser.setHeadImgUrl("/images/avatar/default.png");
        //默认介绍
        registerUser.setIntroduce("这个人很懒，什么都没留下~");
        //居住地
        registerUser.setLocation("未知");
        registerUser.setGender("未知");
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
        if (userMapper.insertSelective(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public String login(String loginName, String passwordMD5, HttpSession httpSession) {
        User user = userMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
        if (user != null && httpSession != null) {
            httpSession.setAttribute(Constants.USER_SESSION_KEY, user);
            //修改最近登录时间
            user.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public User getUserById(Long publishUserId) {
        return userMapper.selectByPrimaryKey(publishUserId);
    }

    @Override
    public Boolean updateUserInfo(User user, HttpSession httpSession) {
        User userTemp = (User) httpSession.getAttribute(Constants.USER_SESSION_KEY);
        User userFromDB = userMapper.selectByPrimaryKey(userTemp.getUserId());
        //当前用户非空且状态正常才可以进行更改
        if (userFromDB != null && userFromDB.getUserStatus().intValue() == 0) {
            userFromDB.setIntroduce(SystemUtil.cleanString(user.getIntroduce()));
            userFromDB.setLocation(SystemUtil.cleanString(user.getLocation()));
            userFromDB.setGender(SystemUtil.cleanString(user.getGender()));
            userFromDB.setNickName(SystemUtil.cleanString(user.getNickName()));
            if (userMapper.updateByPrimaryKeySelective(userFromDB) > 0) {
                httpSession.setAttribute(Constants.USER_SESSION_KEY, userFromDB);
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean updateUserHeadImg(User user, HttpSession httpSession) {
        User userFromDB = userMapper.selectByPrimaryKey(user.getUserId());
        //当前用户非空且状态正常才可以进行更改
        if (userFromDB != null && userFromDB.getUserStatus().intValue() == 0) {
            userFromDB.setHeadImgUrl(user.getHeadImgUrl());
            if (userMapper.updateByPrimaryKeySelective(userFromDB) > 0) {
                httpSession.setAttribute(Constants.USER_SESSION_KEY, userFromDB);
                return true;
            }
        }
        return false;
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
