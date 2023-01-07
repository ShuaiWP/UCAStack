package com.ucas.ucastack.controller.rest;

import com.ucas.ucastack.common.Constants;
import com.ucas.ucastack.entity.Post;
import com.ucas.ucastack.entity.User;
import com.ucas.ucastack.service.PostCollectService;
import com.ucas.ucastack.service.PostService;
import com.ucas.ucastack.service.UserService;
import com.ucas.ucastack.util.Result;
import com.ucas.ucastack.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @Resource
    private PostCollectService postCollectService;

    @GetMapping("/userCenter/{userId}")
//    @ResponseBody
    public String userCenterPage(HttpServletRequest request, @PathVariable("userId") Long userId) {
        //基本用户信息
        User bbsUser = userService.getUserById(userId);
        if (bbsUser == null) {
            return "error/error_404";
        }

        //近期发布的帖子
        List<Post> recentPostList = postService.getRecentPostListByUserId(userId);

        //近期回复的内容
//        List<RecentCommentListEntity> recentCommentList = bbsPostCommentService.getRecentCommentListByUserId(userId);

        request.setAttribute("bbsUser", bbsUser);
        request.setAttribute("recentPostList", recentPostList);
//        request.setAttribute("recentCommentList", recentCommentList);
        return "user/home";
    }

    @GetMapping("/userSet")
//    @ResponseBody
    public String userSetPage(HttpServletRequest request) {
        //假数据
//        User currentUser = (User) request.getSession().getAttribute(Constants.USER_SESSION_KEY);
        User currentUser = userService.getUserById(2L);
        request.setAttribute("bbsUser", currentUser);

        return "user/set";
//        System.out.println(currentUser);
//        return currentUser.getLoginName();
    }

    @GetMapping("/myCenter")
//    @ResponseBody
    public String myCenterPage(HttpServletRequest request) {

        //基本用户信息
//        User currentUser = (User) request.getSession().getAttribute(Constants.USER_SESSION_KEY);
        //假数据
        User currentUser = userService.getUserById(2L);

        //我发的贴
        List<Post> myBBSPostList = postService.getMyPostList(currentUser.getUserId());
        int myBBSPostCount = 0;
        if (!CollectionUtils.isEmpty(myBBSPostList)) {
            myBBSPostCount = myBBSPostList.size();
        }

        //我收藏的贴
//        List<Post> collectRecords = postCollectService.getCollectRecordsByUserId(currentUser.getUserId());
//        int myCollectBBSPostCount = 0;
//        if (!CollectionUtils.isEmpty(collectRecords)) {
//            myCollectBBSPostCount = collectRecords.size();
//        }

        request.setAttribute("myBBSPostList", myBBSPostList);
        request.setAttribute("myBBSPostCount", myBBSPostCount);
//        request.setAttribute("collectRecords", collectRecords);
//        request.setAttribute("myCollectBBSPostCount", myCollectBBSPostCount);
        request.setAttribute("bbsUser", currentUser);
        return "user/index";
    }

    @PostMapping("/updateUserInfo")
    @ResponseBody
    public Result updateInfo(@RequestParam("nickName") String nickName,
                             @RequestParam("userGender") int userGender,
                             @RequestParam("location") String location,
                             @RequestParam("introduce") String introduce,
                             HttpSession httpSession) {

        if (!StringUtils.hasLength(nickName)) {
            return ResultGenerator.genFailResult("nickName参数错误");
        }
        if (userGender < 1 || userGender > 2) {
            return ResultGenerator.genFailResult("userGender参数错误");
        }
        if (!StringUtils.hasLength(location)) {
            return ResultGenerator.genFailResult("location参数错误");
        }
        if (!StringUtils.hasLength(introduce)) {
            return ResultGenerator.genFailResult("introduce参数错误");
        }


//        User user = (User) httpSession.getAttribute(Constants.USER_SESSION_KEY);
        //假数据
        User user = userService.getUserById(2L);

        user.setNickName(nickName);
        if (userGender == 1) {
            user.setGender("男");
        }
        if (userGender == 2) {
            user.setGender("女");
        }
        user.setLocation(location);
        user.setIntroduce(introduce);
        if (userService.updateUserInfo(user, httpSession)) {
            Result result = ResultGenerator.genSuccessResult();
            return result;
        } else {
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        }
    }

    @PostMapping("/updateHeadImg")
    @ResponseBody
    public Result updateHeadImg(@RequestParam("userHeadImg") String userHeadImg,
                                HttpSession httpSession) {

        if (!StringUtils.hasLength(userHeadImg)) {
            return ResultGenerator.genFailResult("userHeadImg参数错误");
        }
//        User user = (User) httpSession.getAttribute(Constants.USER_SESSION_KEY);
        //假数据
        User user = userService.getUserById(2L);

        user.setHeadImgUrl(userHeadImg);
        if (userService.updateUserHeadImg(user, httpSession)) {
            Result result = ResultGenerator.genSuccessResult();
            return result;
        } else {
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        }
    }

    @PostMapping("/updatePassword")
    @ResponseBody
    public Result passwordUpdate(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
        if (!StringUtils.hasLength(originalPassword) || !StringUtils.hasLength(newPassword)) {
            return ResultGenerator.genFailResult("参数不能为空");
        }
        //假数据
//        User currentUser = (User) request.getSession().getAttribute(Constants.USER_SESSION_KEY);
        User currentUser = userService.getUserById(1L);

        if (userService.updatePassword(currentUser.getUserId(), originalPassword, newPassword)) {
            //修改成功后清空session中的数据，前端控制跳转至登录页
            request.getSession().removeAttribute(Constants.USER_SESSION_KEY);
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }


}
