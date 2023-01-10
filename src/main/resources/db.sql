/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.6.21-log : Database - my_bbs_db
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`my_bbs_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `my_bbs_db`;

/*Table structure for table `tb_bbs_post` */

DROP TABLE IF EXISTS `tb_bbs_post`;

CREATE TABLE `tb_bbs_post` (
                               `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '帖子主键id',
                               `publish_user_id` bigint(20) NOT NULL COMMENT '发布者id',
                               `post_title` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '帖子标题',
                               `post_content` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '帖子内容',
                               `post_category_id` int(11) NOT NULL COMMENT '帖子分类id',
                               `post_category_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '帖子分类(冗余字段)',
                               `post_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0-未审核 1-审核通过 2-审核失败 (默认审核通过)',
                               `post_views` bigint(20) NOT NULL DEFAULT '0' COMMENT '阅读量',
                               `post_comments` bigint(20) NOT NULL DEFAULT '0' COMMENT '评论量',
                               `post_collects` bigint(20) NOT NULL DEFAULT '0' COMMENT '收藏量',
                               `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最新修改时间',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
                               PRIMARY KEY (`post_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

/*Table structure for table `tb_bbs_user` */

DROP TABLE IF EXISTS `tb_bbs_user`;

CREATE TABLE `tb_bbs_user` (
                               `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户主键id',
                               `login_name` varchar(32) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '登陆名称(默认为邮箱号码)',
                               `password_md5` varchar(32) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT 'MD5加密后的密码',
                               `nick_name` varchar(8) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '昵称',
                               `head_img_url` varchar(128) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '头像',
                               `gender` varchar(4) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '性别',
                               `location` varchar(4) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '居住地',
                               `introduce` varchar(32) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '个人简介',
                               `user_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户状态 0=正常 1=禁言',
                               `last_login_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最新登录时间',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
                               PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `tb_bbs_user` */

insert  into `tb_bbs_user`(`user_id`,`login_name`,`password_md5`,`nick_name`,`head_img_url`,`gender`,`location`,`introduce`,`user_status`,`last_login_time`,`create_time`) values
    (1,'coder13@qq.com','e10adc3949ba59abbe56e057f20f883e','游客','/images/avatar/default.png','未知','未知','这个人很懒，什么都没留下~',0,'2021-08-10 16:33:20','2021-08-10 14:49:38');

/*Table structure for table `tb_post_category` */

DROP TABLE IF EXISTS `tb_post_category`;

CREATE TABLE `tb_post_category` (
                                    `category_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类表主键',
                                    `category_name` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类的名称',
                                    `category_rank` int(11) NOT NULL DEFAULT '1' COMMENT '分类的排序值 被使用的越多数值越大',
                                    `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0=否 1=是',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `tb_post_category` */

insert  into `tb_post_category`(`category_id`,`category_name`,`category_rank`,`is_deleted`,`create_time`) values (1,'提问',10,0,'2021-08-10 14:47:38'),(2,'分享',9,0,'2021-08-10 14:47:38'),(3,'建议',8,0,'2021-08-10 14:47:38'),(4,'讨论',7,0,'2021-08-10 14:47:38'),(5,'动态',6,0,'2021-08-10 14:47:38'),(6,'其它',5,0,'2021-08-10 14:47:38');

/*Table structure for table `tb_post_collect_record` */

DROP TABLE IF EXISTS `tb_post_collect_record`;

CREATE TABLE `tb_post_collect_record` (
                                          `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                          `post_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '收藏帖子主键',
                                          `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '收藏者id',
                                          `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
                                          PRIMARY KEY (`record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

/*Table structure for table `tb_post_comment` */

DROP TABLE IF EXISTS `tb_post_comment`;

CREATE TABLE `tb_post_comment` (
                                   `comment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                   `post_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '关联的帖子主键',
                                   `comment_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '评论者id',
                                   `comment_body` varchar(10000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '评论内容',
                                   `parent_comment_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所回复的上一级评论的userId',
                                   `comment_create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
                                   `is_deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除 0-未删除 1-已删除',
                                   PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
