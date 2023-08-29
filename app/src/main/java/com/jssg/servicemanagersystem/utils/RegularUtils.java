package com.jssg.servicemanagersystem.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则判断
 * Created by gongdongyang on 2018/9/25.
 */

public class RegularUtils {

    /**
     * 用户名是否合法，规则：字母开头，允许5-16字节，允许字母、数字、下划线
     *
     * @param username 用户名
     * @return 是否合法
     */
    public static boolean isValidUsername(String username) {
        Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{4,15}$");
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean isValidLoginName(String loginName) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_/-]{6,16}");
        Matcher matcher = pattern.matcher(loginName);
        return matcher.matches();
    }

    public static boolean isValidReportPassword(String password) {
        /*Pattern pattern = Pattern.compile("[a-zA-Z0-9]{6,20}");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();*/
        String regex = "^(?![A-Za-z]+$)(?!\\d+$)(?![\\W_]+$)\\S{6,16}$";
        if (!password.matches(regex)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidReportAuthCode(String authCode) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]{6}");
        Matcher matcher = pattern.matcher(authCode);
        return matcher.matches();
    }

    /**
     * 手机号是否合法
     * 13号段：130、131、132、133、134、135、136、137、138、139
     * 14号段：145、147
     * 15号段：150、151、152、153、154、155、156、157、158、159
     * 17号段：170、171、173、175、176、177、178
     * 18号段：180、181、182、183、184、185、186、187、188、189
     *
     * @param mobileNumber 手机号
     * @return 是否合法
     */
    public static boolean isValidMobileNumber(String mobileNumber) {
        //"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        /*Pattern pattern = Pattern.compile("[1][34578]\\d{9}");*/
        Pattern pattern = Pattern.compile("^[1]\\d{10}");
        Matcher matcher = pattern.matcher(mobileNumber);
        return matcher.matches();
    }

    public static boolean isValidContact(String contact) {
        Pattern pattern = Pattern.compile("^(((0\\d{2,3}-)?|(0\\d{2,3})?\\d{7,8})|(1[3584]\\d{9}))$");
        Matcher matcher = pattern.matcher(contact);
        return matcher.matches();
    }

    /**
     * 短信验证码是否合法，规则：6位数字
     *
     * @param smsCode 短信验证码
     * @return 是否合法
     */
    public static boolean isValidSmsCode(String smsCode) {
        Pattern pattern = Pattern.compile("^\\d{6}$");
        Matcher matcher = pattern.matcher(smsCode);
        return matcher.matches();
    }

    /**
     * 密码是否合法，规则：字母、数字和符号
     * "^(?![A-Za-z]+$)(?!\\d+$)(?![\\W_]+$)\\S{6,16}$"
     *
     * @param password 密码
     * @return 是否合法
     */
    public static boolean isValidPassword(String password) {
        //Pattern pattern = Pattern.compile("^(?![A-Za-z]+$)(?!\\\\d+$)(?![\\\\W_]+$)\\\\S{6,16}$");
        /*Pattern pattern = Pattern.compile("^(?![A-Za-z]+$)(?!\\\\d+$)(?![\\\\W_]+$)\\\\S{6,16}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();*/
        String regex = "^(?![A-Za-z]+$)(?!\\d+$)(?![\\W_]+$)\\S{6,16}$";
        if (!password.matches(regex)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 邀请码是否合法，规则：字母、数字
     *
     * @param inviteCode 密码
     * @return 是否合法
     */
    public static boolean isValidInviteCode(String inviteCode) {
        if (null == inviteCode || "".equals(inviteCode)) {
            return false;
        }
        String regex = "^([a-zA-Z0-9]+)$";
        if (!inviteCode.matches(regex)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 姓名是否合法，规则：汉字
     *
     * @param name 姓名
     * @return 是否合法
     */
    public static boolean isValidName(String name) {
        Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]+((·|•)+[\u4e00-\u9fa5]+)*$");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * 身份证号是否合法
     *
     * @param cid 身份证号
     * @return 是否合法
     */
    public static boolean isValidCid(String cid) {
        Pattern pattern_15 = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
        Matcher matcher_15 = pattern_15.matcher(cid);
        Pattern pattern_18 = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$");
        Matcher matcher_18 = pattern_18.matcher(cid);
        return matcher_15.matches() || matcher_18.matches();
    }

    /**
     * 银行卡号是否合法，规则：16或19位数字
     *
     * @param cardNumber 银行卡号
     * @return 是否合法
     */
    public static boolean isValidCardNumber(String cardNumber) {
        Pattern pattern = Pattern.compile("^\\d{16}|\\d{19}$");
        Matcher matcher = pattern.matcher(cardNumber);
        return matcher.matches();
    }

    /**
     * 邮箱验证
     * 注册、绑定邮箱用：/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/
     *
     * @param email 邮箱
     * @return 是否合法
     */
    public static boolean isValidEmailByRegister(String email) {
        if (null == email || "".equals(email)) {
            return false;
        }

        String regEx1 = "^([a-zA-Z0-9_.\\-])+@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";

        //正则表达式的模式
        Pattern p = Pattern.compile(regEx1);
        //正则表达式的匹配器
        Matcher m = p.matcher(email);
        //进行正则匹配
        return m.matches();
    }



    /**
     * 邮箱验证
     * 2021.2.9
     * 登录，修改密码用：/^([a-zA-Z0-9_\.\-\+\'])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/
     *
     * @param email 邮箱
     * @return 是否合法
     */
    public static boolean isValidEmailByLogin(String email) {
        if (null == email || "".equals(email)) {
            return false;
        }

        String regEx1 = "^([a-zA-Z0-9_.\\-+'])+@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";

        //正则表达式的模式
        Pattern p = Pattern.compile(regEx1);
        //正则表达式的匹配器
        Matcher m = p.matcher(email);
        //进行正则匹配
        return m.matches();
    }

    public static boolean isNumeric(String numberStr) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher matcher = pattern.matcher(numberStr);
        return matcher.matches();
    }


    public static boolean isEnglish(String str) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isChinese(String str) {
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }


    public static boolean isValidTaxCode(String taxCode) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{15,20}$");
        Matcher matcher = pattern.matcher(taxCode);
        return matcher.matches();
    }




    public static boolean isPhone(String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }
}
