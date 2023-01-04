package com.ucas.ucastack.controller.rest;

import com.ucas.ucastack.common.Constants;
import com.ucas.ucastack.entity.Post;
import com.ucas.ucastack.entity.PostCategory;
import com.ucas.ucastack.entity.User;
import com.ucas.ucastack.service.*;
import com.ucas.ucastack.util.PageResult;
import com.ucas.ucastack.util.Result;
import com.ucas.ucastack.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class PostController {
    @Resource
    private PostService postService;
    @Resource
    private PostCategoryService postCategoryService;
    @Resource
    private UserService userService;
    @Resource
    private PostCollectService postCollectService;
    @Resource
    private PostCommentService postCommentService;

    @GetMapping("detail/{postId}")
    public String postDetail(HttpServletRequest request, @PathVariable(value = "postId") Long postId,
                             @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
        List<PostCategory> postCategories = postCategoryService.getPostCategories();
        if (CollectionUtils.isEmpty(postCategories)) {
            return "error/error_404";
        }
        //将分类数据封装到request域中
        request.setAttribute("postCategories", postCategories);

        // 帖子内容
        Post post = postService.getPostForDetail(postId);
        if (post == null) {
            return "error/error_404";
        }
        request.setAttribute("post", post);
        // 发帖用户信息
        User user = userService.getUserById(post.getPublishUserId());
        if (user == null) {
            return "error/error_404";
        }
        request.setAttribute("user", user);

        // 是否收藏了本贴 暂时性注释，测试
//        User currentUser = (User) request.getSession().getAttribute(Constants.USER_SESSION_KEY);
//        request.setAttribute("currentUserCollectFlag", postCollectService.validUserCollect(currentUser.getUserId(), postId));
        request.setAttribute("currentUserCollectFlag", false);

        // 本周热议的帖子
        request.setAttribute("hotTopicPostList", postService.getHotTopicPostList());

        // 评论数据
        PageResult commentsPage = postCommentService.getCommentsByPostId(postId, commentPage);
        request.setAttribute("commentsPage", commentsPage);

        return "postPages/detail";
    }

    @GetMapping("editPostPage/{postId}")
    public String editPostPage(HttpServletRequest request, @PathVariable(value = "postId") Long postId) {
//        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION_KEY);
        List<PostCategory> postCategories = postCategoryService.getPostCategories();
        if (CollectionUtils.isEmpty(postCategories)) {
            return "error/error_404";
        }
        //将分类数据封装到request域中
        request.setAttribute("postCategories", postCategories);
        if (null == postId || postId < 0) {
            return "error/error_404";
        }
        Post post = postService.getPostById(postId);
        if (post == null) {
            return "error/error_404";
        }
//        if (!user.getUserId().equals(post.getPublishUserId())) {
//            request.setAttribute("message", "非本人发帖，无权限修改");
//            return "error/error";
//        }
        request.setAttribute("post", post);
        request.setAttribute("postId", postId);
        return "postPages/edit";
    }

    @PostMapping("/editPost")
    @ResponseBody
    public Result editPost(@RequestParam("postId") Long postId,
                           @RequestParam("postTitle") String postTitle,
                           @RequestParam("postCategoryId") Integer postCategoryId,
                           @RequestParam("postContent") String postContent,
                           HttpSession httpSession) {
//        User user = (User) httpSession.getAttribute(Constants.USER_SESSION_KEY);
        if (null == postId || postId < 0) {
            return ResultGenerator.genFailResult("postId参数错误");
        }
        Post temp = postService.getPostById(postId);
        if (temp == null) {
            return ResultGenerator.genFailResult("postId参数错误");
        }
//        if (!user.getUserId().equals(temp.getPublishUserId())) {
//            return ResultGenerator.genFailResult("非本人发帖，无权限修改");
//        }
        if (!StringUtils.hasLength(postTitle)) {
            return ResultGenerator.genFailResult("postTitle参数错误");
        }
        if (null == postCategoryId || postCategoryId < 0) {
            return ResultGenerator.genFailResult("postCategoryId参数错误");
        }
        PostCategory postCategory = postCategoryService.selectById(postCategoryId);
        if (null == postCategory) {
            return ResultGenerator.genFailResult("postCategoryId参数错误");
        }
        if (!StringUtils.hasLength(postContent)) {
            return ResultGenerator.genFailResult("postContent参数错误");
        }
        if (postTitle.trim().length() > 32) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (postContent.trim().length() > 100000) {
            return ResultGenerator.genFailResult("内容过长");
        }
//        String kaptchaCode = httpSession.getAttribute(Constants.VERIFY_CODE_KEY) + "";
//        if (!StringUtils.hasLength(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
//            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
//        }
        temp.setPostTitle(postTitle);
        temp.setPostContent(postContent);
        temp.setPostCategoryId(postCategoryId);
        temp.setPostCategoryName(postCategory.getCategoryName());
        temp.setLastUpdateTime(new Date());
        if (postService.updatePost(temp) > 0) {
//            httpSession.removeAttribute(Constants.VERIFY_CODE_KEY);//清空session中的验证码信息
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("请求失败，请检查参数及账号是否有操作权限");
        }
    }

    @GetMapping("/addPostPage")
    public String addPostPage(HttpServletRequest request) {
        List<PostCategory> postCategories = postCategoryService.getPostCategories();
        if (CollectionUtils.isEmpty(postCategories)) {
            return "error/error_404";
        }
        //将分类数据封装到request域中
        request.setAttribute("postCategories", postCategories);
        return "postPages/add";
    }

    @PostMapping("/addPost")
    @ResponseBody
    public Result addPost(@RequestParam("postTitle") String postTitle,
                          @RequestParam("postCategoryId") Integer postCategoryId,
                          @RequestParam("postContent") String postContent,
                          HttpSession httpSession) {
        if (!StringUtils.hasLength(postTitle)) {
            return ResultGenerator.genFailResult("postTitle参数错误");
        }
        if (null == postCategoryId || postCategoryId < 0) {
            return ResultGenerator.genFailResult("postCategoryId参数错误");
        }
        PostCategory postCategory = postCategoryService.selectById(postCategoryId);
        if (null == postCategory) {
            return ResultGenerator.genFailResult("postCategoryId参数错误");
        }
        if (!StringUtils.hasLength(postContent)) {
            return ResultGenerator.genFailResult("postContent参数错误");
        }
        if (postTitle.trim().length() > 32) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (postContent.trim().length() > 100000) {
            return ResultGenerator.genFailResult("内容过长");
        }
//        String kaptchaCode = httpSession.getAttribute(Constants.VERIFY_CODE_KEY) + "";
//        if (!StringUtils.hasLength(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
//            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
//        }
//        User user = (User) httpSession.getAttribute(Constants.USER_SESSION_KEY);
        Post post = new Post();
//        post.setPublishUserId(user.getUserId());
        post.setPublishUserId((long)4);
        post.setPostTitle(postTitle);
        post.setPostContent(postContent);
        post.setPostCategoryId(postCategoryId);
        post.setPostCategoryName(postCategory.getCategoryName());
        if (postService.savePost(post) > 0) {
//            httpSession.removeAttribute(Constants.VERIFY_CODE_KEY);//清空session中的验证码信息
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("请求失败，请检查参数及账号是否有操作权限");
        }
    }

    /**
     * 删除功能在用户中心界面的我发的帖里面
     * @param postId
     * @param httpSession
     * @return
     */
    @PostMapping("/delPost/{postId}")
    @ResponseBody
    public Result delPost(@PathVariable("postId") Long postId,
                          HttpSession httpSession) {
        if (null == postId || postId < 0) {
            return ResultGenerator.genFailResult("postId参数错误");
        }
        User user = (User) httpSession.getAttribute(Constants.USER_SESSION_KEY);
        if (postService.delPost(user.getUserId(), postId) > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("请求失败，请检查参数及账号是否有操作权限");
        }
//        if (postService.delPost((long)4, (long)20) > 0) {
//            return ResultGenerator.genSuccessResult();
//        } else {
//            return ResultGenerator.genFailResult("请求失败，请检查参数及账号是否有操作权限");
//        }
    }


}
