package com.ucas.ucastack;

import com.ucas.ucastack.util.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UcastackApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    void testPWDToMD5(){
        String originPWD = "123456";
        String s = MD5Util.MD5Encode(originPWD, "UTF-8");
        System.out.println(s);
    }
}
