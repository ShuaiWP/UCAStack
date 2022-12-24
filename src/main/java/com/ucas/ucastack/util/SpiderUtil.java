package com.ucas.ucastack.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class SpiderUtil {
    public HashMap<String, String> parseCookieString(String cookie) {
        HashMap<String, String> myCookie = new HashMap<>();
        String[] parts = cookie.trim().split(";");
        for (String part: parts) {
            String[] kv;
            String key, value;
            if (part.contains("\"")) {    // which means the valve may contain '='
                kv = part.trim().split("\"");
            } else {    // which means the valve must not contain '='
                kv = part.trim().split("=");
            }
            key = kv[0];
            value = kv[1];
            myCookie.put(key, value);
        }

        return myCookie;
    }

    public static void main(String[] args) throws IOException {
        SpiderUtil util = new SpiderUtil();
        String url = "https://gkder.ucas.ac.cn/";
        String cookie = "_abfpc=0062d41a0ac87a0c837f2a91546043b3af70760a_2.0; cna=efc73112ebefda929cbd400e16986e3e; sepuser=\"bWlkPWExMGViNGQyLWI1MGEtNGU2Mi04YWUyLWRiYTc4ZTA4YjVjNA==  \"; flarum_remember=UPk8qXxO8Fst7eDH9rv9hIWvPMk7PUGuhpntCZvz; flarum_session=znDPKjFskuffohqlzOb8aJiqgfvLCOV9gQsHPcQt";
        // cookie map
        HashMap<String, String> myCookies = util.parseCookieString(cookie);
        System.out.println(myCookies);

        Document document = Jsoup.connect(url).cookies(myCookies).get();
        System.out.println(document);
    }
}
