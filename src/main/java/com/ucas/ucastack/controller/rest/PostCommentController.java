package com.ucas.ucastack.controller.rest;

import com.ucas.ucastack.common.Constants;
import com.ucas.ucastack.common.ServiceResultEnum;
import com.ucas.ucastack.entity.PostComment;
import com.ucas.ucastack.entity.User;
import com.ucas.ucastack.service.PostCommentService;
import com.ucas.ucastack.service.UserService;
import com.ucas.ucastack.util.Result;
import com.ucas.ucastack.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class PostCommentController {

    @Resource
    private PostCommentService PostCommentService;
    @Resource
    private UserService userService;

    // 评论帖子
    @PostMapping("/replyPost")
    @ResponseBody
    public Result replyPost(@RequestParam("postId") Long postId,
                            @RequestParam(value = "parentCommentUserId", required = false) Long parentCommentUserId,
                            @RequestParam("commentBody") String commentBody,
                            @RequestParam("verifyCode") String verifyCode,
                            HttpSession httpSession) {
        // 检查各个参数
        if (null == postId || postId < 0) {
            return ResultGenerator.genFailResult("postId参数错误");
        }
        if (!StringUtils.hasLength(commentBody)) {
            return ResultGenerator.genFailResult("commentBody参数错误");
        }
        if (commentBody.trim().length() > 200) {
            return ResultGenerator.genFailResult("评论内容过长");
        }
        // 验证码
        String kaptchaCode = httpSession.getAttribute(Constants.VERIFY_CODE_KEY) + "";
        if (!StringUtils.hasLength(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
        }
        User user = (User) httpSession.getAttribute(Constants.USER_SESSION_KEY);
        //假数据
        //User User = userService.getUserById(2L);

        // 创造评论
        PostComment PostComment = new PostComment();
        PostComment.setCommentBody(commentBody);
        PostComment.setCommentUserId(user.getUserId());
        PostComment.setParentCommentUserId(parentCommentUserId);
        PostComment.setPostId(postId);

        // 添加进数据库
        if (PostCommentService.addPostComment(PostComment)) {
            httpSession.removeAttribute(Constants.VERIFY_CODE_KEY);
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("请求失败，请检查参数及账号是否有操作权限");
        }
    }


    // 根据评论ID删除评论
    @PostMapping("/delReply/{commentId}")
    @ResponseBody
    public Result delReply(@PathVariable("commentId") Long commentId,
                           HttpSession httpSession) {

        if (null == commentId || commentId < 0) {
            return ResultGenerator.genFailResult("commentId参数错误");
        }

        User User = (User) httpSession.getAttribute(Constants.USER_SESSION_KEY);
        //假数据
        //User User = userService.getUserById(2L);

        if (PostCommentService.delPostComment(commentId,User.getUserId())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("请求失败，请检查参数及账号是否有操作权限");
        }
    }
}