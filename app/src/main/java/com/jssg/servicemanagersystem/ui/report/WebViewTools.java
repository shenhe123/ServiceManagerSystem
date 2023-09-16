package com.jssg.servicemanagersystem.ui.report;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.jssg.servicemanagersystem.core.AccountManager;

public class WebViewTools {

    public static String setWebviewUserAgent(String ua) {
        StringBuilder userAgentString = new StringBuilder(ua);
        if (userAgentString.toString().contains("/Authorization")) {
            int index = userAgentString.toString().indexOf("/Authorization");
            userAgentString = new StringBuilder(userAgentString.subSequence(0, index));
        }

        return userAgentString.toString();
    }

    /**
     * 将cookie同步到WebView
     *
     * @param url WebView要加载的url
     * @return true 同步cookie成功，false同步cookie失败
     * @Author JPH
     */
    public static boolean syncCookie(Context context, String url) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        String cookie = AccountManager.Companion.getInstance().getCookie();
        if (TextUtils.isEmpty(cookie)) {
            cookie = "";
        }
        StringBuilder cookieBuild = new StringBuilder(cookie);
//        User user = AccountManger.getInstance().getUser();
        //如果cookies里面已经有authorization和accountGroup信息,先删除，再添加最新的
        if (cookieBuild.toString().contains("Authorization=")) {
            int index = cookieBuild.toString().indexOf("Authorization=");
            cookieBuild = new StringBuilder(cookieBuild.toString().subSequence(0, index));
        }

        //先清掉之前的cookies
        CookieManager.getInstance().removeAllCookie();
        //设置cookies需要根据";"一条一条设置否则失败
        String[] strings = cookieBuild.toString().split(";");
        for (int i = 0; i < strings.length; i++) {
            CookieManager.getInstance().setCookie(url, strings[i] + ";Path = /");
        }
        cookieManager.flush();
        String newCookie = cookieManager.getCookie(url);
        return TextUtils.isEmpty(newCookie) ? false : true;

    }
}
