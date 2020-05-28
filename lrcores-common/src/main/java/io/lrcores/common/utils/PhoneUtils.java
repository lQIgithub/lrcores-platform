package io.lrcores.common.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName PhoneUtils
 * @Description TODO 手机号码检验工具类
 * 手机号码验证
 * 三大电信运营商手机号段管理验证
 * 移动 134（0-8）,135，136,137,138,139 电信  133,1349                          联通 130,131,132
 * 移动 147,148,1440                    电信 145,146                            联通 149,1410
 * 移动 150,151,152,157,158,159         电信 155,156                            联通 153
 * 移动                                 电信                                    联通 166
 * 移动 178，1703，1705,1706            电信 171,175,176,1707,1708,1709         联通 1700,1701,1702,173,177,17400-17405
 * 移动 182,183,184,187,188             电信 185,186                            联通 180,181,189
 * 移动 198                             电信                                    联通 199
 *
 * @Date 2020/5/28 14:39
 * @Version 1.0
 */
public class PhoneUtils {

    /*定义验证规则*/
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(13[0-9]\\d{8})|(14[56789]\\d{8})|(14[14]0\\d{7})|(15[012356789]\\d{8})" +
                    "|(16[6]\\d{8})|(170[012356789]\\d{7})|(17[135678]\\d{8})" +
                    "|(1740[0-5]\\d{6})|(18[0-9]\\d{8})|(19[89]\\d{8})$"
    );


    /**
     * 校验手机号码是否正确
     *
     * @param phone
     * @return true/false  true:表示手机号码正确；false：表示手机号码有误
     */
    public static boolean isValid(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        Matcher m = PHONE_PATTERN.matcher(phone);
        return m.matches();
    }

}
