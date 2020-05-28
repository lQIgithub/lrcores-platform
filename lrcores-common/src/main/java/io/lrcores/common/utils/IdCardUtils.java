package io.lrcores.common.utils;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName IdCardUtils
 * @Description TODO
 * 身份证号码校验规则功能实现
 * 身份证验证的工具（支持15位或18位省份证）
 * 身份证号码结构：
 * <p>
 * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
 * 排列顺序从左至右依次为：6位数字地址码，8位数字出生日期码，3位数字顺序码和1位数字校验码。
 * <p>
 * 地址码（前6位）：表示对象常住户口所在县（市、镇、区）的行政区划代码，按GB/T2260的规定执行。
 * <li>前1、2位数字表示：所在省份的代码；</li>
 * <li>第3、4位数字表示：所在城市的代码；</li>
 * <li>第5、6位数字表示：所在区县的代码；</li>
 * <p>
 * 出生日期码，（第7位 - 14位）：表示编码对象出生年、月、日，按GB按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
 * <p>
 * 顺序码（第15位至17位）：表示在同一地址码所标示的区域范围内，对同年、同月、同日出生的人编订的顺序号，顺序码的奇数分配给男性，偶数分配给女性。
 * <li>第15、16位数字表示：所在地的派出所的代码；</li>
 * <li>第17位数字表示性别：奇数表示男性，偶数表示女性；</li>
 * <li>第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示。</li>
 * <p>
 * 校验码（第18位数）：
 * <p>
 * 十七位数字本体码加权求和公式 s = sum(Ai*Wi), i = 0..16，先对前17位数字的权求和；
 * Ai:表示第i位置上的身份证号码数字值.Wi:表示第i位置上的加权因子.Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2；
 * 计算模 Y = mod(S, 11)
 * 通过模得到对应的模 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
 * <p>
 * 计算步骤：
 * 1.将前17位数分别乘以不同的系数。从第1位到第17位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
 * 2.将这17位数字和系数相乘的结果相加。
 * 3.用加出来和除以11，看余数是多少
 * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字，分别对应的最后一位身份证的号码为：1 0 X 9 8 7 6 5 4 3
 * <p>
 * @Date 2020/5/28 14:48
 * @Version 1.0
 */
public class IdCardUtils {
    //行政区编码
    private static final Map<Integer, String> provinceAndCitys = new HashMap() {{
        put(11, "北京");
        put(12, "天津");
        put(13, "河北");
        put(14, "山西");
        put(15, "内蒙古");
        put(21, "辽宁");
        put(22, "吉林");
        put(23, "黑龙江");
        put(31, "上海");
        put(32, "江苏");
        put(33, "浙江");
        put(34, "安徽");
        put(35, "福建");
        put(36, "江西");
        put(37, "山东");
        put(41, "河南");
        put(42, "湖北");
        put(43, "湖南");
        put(44, "广东");
        put(45, "广西");
        put(46, "海南");
        put(50, "重庆");
        put(51, "四川");
        put(52, "贵州");
        put(53, "云南");
        put(54, "西藏");
        put(61, "陕西");
        put(62, "甘肃");
        put(63, "青海");
        put(64, "宁夏");
        put(65, "新疆");
        put(71, "台湾");
        put(81, "香港");
        put(82, "澳门");
        put(91, "国外");
    }};


    /*每位加权因子*/
    private static final Integer[] powers = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    /*第18位校验码*/
    private static final char[] parityBit = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    //定义身份证号码长度验证规则
    private static final Pattern IDCARD_LENGTH_PATTERN = Pattern.compile("^\\d{15}|(\\d{17}(\\d|x|X))$");
    //15位身份证号码的基本校验
    private static final Pattern IDCARD15_PATTERN = Pattern.compile("^[1-9]\\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\\d{3}$");
    //18位身份证号码的基本校验
    private static final Pattern IDCARD18_PATTERN = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\\d{3}(\\d|x|X)$");


    /**
     * 校验身份证号码是否正确
     *
     * @param idCard
     * @return true/false  true:表示身份证号码正确；false：表示身份证号码有误
     */
    public static boolean isValid(String idCard) {
        //obj在此方法中获取参数值，待校验的身份证号码
        if (StringUtils.isEmpty(idCard)) {
            return true;
        }
        Matcher m = IDCARD_LENGTH_PATTERN.matcher(idCard);
        if (!m.matches()) return false;
        //判断长度为15位或18位
        if (idCard.length() == 15) {
            return check15IdCardNo(idCard);
        } else if (idCard.length() == 18) {
            return check18IdCardNo(idCard);
        }
        return true;
    }

    /*15位身份证校验*/
    private static boolean check15IdCardNo(String idCard) {
        Matcher m = IDCARD15_PATTERN.matcher(idCard);
        if (!m.matches()) return false;
        //校验地址码
        String addressCode = idCard.substring(0, 6);
        boolean check = checkAddressCode(addressCode);
        if (!check) return false;
        //校验日期码
        String birDayCode = "19" + idCard.substring(6, 12);
        return checkBirthDayCode(birDayCode);
    }

    /*18位身份证校验*/
    private static boolean check18IdCardNo(String idCard) {
        Matcher m = IDCARD18_PATTERN.matcher(idCard);
        if (!m.matches()) return false;
        //校验地址码
        String addressCode = idCard.substring(0, 6);
        boolean check = checkAddressCode(addressCode);
        if (!check) return false;
        //校验日期码
        String birDayCode = idCard.substring(6, 14);
        check = checkBirthDayCode(birDayCode);
        if (!check) return false;
        //验证校检码
        return checkParityBit(idCard);
    }

    /*校验日期码*/
    private static boolean checkBirthDayCode(String birDayCode) {
        Matcher matcher = Pattern.compile("^[1-9]\\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$").matcher(birDayCode);
        boolean check = matcher.matches();
        if (!check) return false;
        int yyyy = Integer.parseInt(birDayCode.substring(0, 4), 10);
        int mm = Integer.parseInt(birDayCode.substring(4, 6), 10);
        int dd = Integer.parseInt(birDayCode.substring(6), 10);
        //身份证生日日期
        LocalDate birth = LocalDate.of(yyyy, mm - 1, dd);
        //当前日期
        LocalDate now = LocalDate.now();
        if (birth.isAfter(now)) {
            return false;//生日不能大于当前日期
        }
        return true;
    }

    /*校验地址码*/
    private static boolean checkAddressCode(String addressCode) {
        Matcher matcher = Pattern.compile("^[1-9]\\d{5}$").matcher(addressCode);
        boolean check = matcher.matches();
        if (!check) return false;
        String address = provinceAndCitys.get(Integer.parseInt(addressCode.substring(0, 2)));
        if (!StringUtils.isEmpty(address)) {
            return true;
        } else {
            return false;
        }
    }


    /*验证校检码*/
    private static boolean checkParityBit(String idCard) {
        char parityBit = idCard.charAt(17);
        if (getParityBit(idCard) == parityBit) {
            return true;
        } else {
            return false;
        }
    }

    /*计算校检码*/
    private static char getParityBit(String idCard) {
        /*加权 */
        int power = 0;
        for (int i = 0; i < 17; i++) {
            power += Integer.parseInt(String.valueOf(idCard.charAt(i)), 10) * powers[i];
        }
        /*取模*/
        int mod = power % 11;
        return parityBit[mod];
    }

}
