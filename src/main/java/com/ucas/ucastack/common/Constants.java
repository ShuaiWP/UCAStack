package com.ucas.ucastack.common;

import us.codecraft.webmagic.Site;

/**
 * 常量
 */
public class Constants {
    public final static String FILE_UPLOAD_DIC = "D:\\upload\\";//上传文件的默认url前缀，根据部署设置自行修改

    public final static String USER_SESSION_KEY = "myBBSUser";//session中user的key

    public final static String VERIFY_CODE_KEY = "userVerifyCode";//验证码key

    // cookies for spider
    public static final String COOKIE_KEY1 = "_abfpc";
    public static final String COOKIE_VALUE1 = "0062d41a0ac87a0c837f2a91546043b3af70760a_2.0";
    public static final String COOKIE_KEY2 = "cna";
    public static final String COOKIE_VALUE2 = "efc73112ebefda929cbd400e16986e3e";
    public static final String COOKIE_KEY3 = "sepuser";
    public static final String COOKIE_VALUE3 = "\"bWlkPWExMGViNGQyLWI1MGEtNGU2Mi04YWUyLWRiYTc4ZTA4YjVjNA==  \"";
    public static final String COOKIE_KEY4 = "flarum_remember";
    public static final String COOKIE_VALUE4 = "UPk8qXxO8Fst7eDH9rv9hIWvPMk7PUGuhpntCZvz";
    public static final String COOKIE_KEY5 = "flarum_session";
    public static final String COOKIE_VALUE5 = "znDPKjFskuffohqlzOb8aJiqgfvLCOV9gQsHPcQt";

    // base api url for list of posts
    public static final String BASE_LIST_URL = "https://gkder.ucas.ac.cn/api/discussions?include=user%2ClastPostedUser%2Ctags%2Ctags.parent%2CfirstPost%2CrecipientUsers%2CrecipientGroups&sort&page%5Boffset%5D=";
    // base api url for detailed information of a post
    public static final String BASE_POST_URL = "https://gkder.ucas.ac.cn/api/discussions/";
}
