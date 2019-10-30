//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zren.platform.common.util.tool;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UsernameGenUtils {
    private static final String[] surnames = new String[]{"wang", "li", "zhang", "liu", "chen", "yang", "huang", "zhao", "zhou", "wu", "xu", "sun", "ma", "hu", "zhu", "guo", "he", "luo", "gao", "lin", "zheng", "liang", "xie", "tang", "xu", "feng", "song", "han", "deng", "peng", "cao", "zeng", "tian", "yu", "xiao", "pan", "yuan", "dong", "ye", "du", "ding", "jiang", "cheng", "yu", "lv", "wei", "cai", "su", "ren", "lu", "shen", "jiang", "yao", "zhong", "cui", "lu", "tan", "wang", "shi", "fu", "jia", "fan", "jin", "fang", "wei", "xia", "liao", "hou", "bai", "meng", "zou", "qin", "yin", "jiang", "xiong", "xue", "qiu", "yan", "duan", "lei", "ji", "shi", "tao", "mao", "he", "long", "wan", "gu", "guan", "hao", "kong", "xiang", "gong", "shao", "qian", "wu", "yang", "li", "tang", "dai", "yan", "wen", "chang", "niu", "mo", "hong", "mi", "kang", "wen", "dai"};
    private static final String[] maleFirstNameStart = new String[]{"zi", "zi", "hao", "yi", "yu", "hao", "jun", "rui", "hao", "bo"};
    private static final String[] maleFirstNameEnd = new String[]{"xuan", "yu", "ze", "hao", "chen", "rui", "hang", "yang", "ming", "chen"};
    private static final String[] femaleFirstNameStart = new String[]{"zi", "yu", "ruo", "shi", "si", "yu", "xin", "yi", "ke", "meng"};
    private static final String[] femaleFirstNameEnd = new String[]{"han", "xuan", "yi", "tong", "qi", "xin", "yan", "nuo", "xin", "tong"};
    private static final String[] someLetters = new String[]{"a", "b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "w", "x", "y", "z"};

    private UsernameGenUtils() {
    }

    public static List<String> generateRandomUsernames(int numberOfNames) {
        return IntStream.range(0, numberOfNames).mapToObj((x) -> {
            return generateRandomUsername();
        }).collect(Collectors.toList());
    }

    public static String generateRandomUsername() {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        int randomValue = current.nextInt(0, 100);
        return randomValue < 40?generateUsernameWithFirstRule():(randomValue < 60?generateUsernameWithSecondRule():(randomValue < 70?generateUsernameWithThirdRule():(randomValue < 85?generateUsernameWithFourthRule():generateUsernameWithFifthRule())));
    }

    static List<String> generateRandomChineseName() {
        List<String> name = new ArrayList();
        int index = ThreadLocalRandom.current().nextInt(0, surnames.length);
        String surname = surnames[index];
        name.add(surname);
        int randomIntSmallerThan10 = ThreadLocalRandom.current().nextInt(0, 10);
        if(randomIntSmallerThan10 < 7) {
            name.addAll(generateRandomFirstNameWithTwoWords());
        } else {
            name.add(generateRandomFirstNameWithOneWord());
        }

        return name;
    }

    static List<String> generateRandomFirstNameWithTwoWords() {
        List<String> givenName = new ArrayList();
        if(ThreadLocalRandom.current().nextInt(0, 2) < 1) {
            givenName.add(maleFirstNameStart[ThreadLocalRandom.current().nextInt(0, maleFirstNameStart.length)]);
            givenName.add(maleFirstNameEnd[ThreadLocalRandom.current().nextInt(0, maleFirstNameEnd.length)]);
        } else {
            givenName.add(femaleFirstNameStart[ThreadLocalRandom.current().nextInt(0, femaleFirstNameStart.length)]);
            givenName.add(femaleFirstNameEnd[ThreadLocalRandom.current().nextInt(0, femaleFirstNameEnd.length)]);
        }

        return givenName;
    }

    static String generateRandomFirstNameWithOneWord() {
        return ThreadLocalRandom.current().nextInt(0, 2) < 1?maleFirstNameEnd[ThreadLocalRandom.current().nextInt(0, maleFirstNameEnd.length)]:femaleFirstNameEnd[ThreadLocalRandom.current().nextInt(0, femaleFirstNameEnd.length)];
    }

    static String generateRandomNumberAsString(int min, int max) {
        int length = ThreadLocalRandom.current().nextInt(min, max + 1);
        return generateFixedLengthRandomNumberAsString(length);
    }

    private static String generateFixedLengthRandomNumberAsString(int length) {
        int origin = (int)Math.pow(10.0D, length - 1);
        int bound = (int)Math.pow(10.0D, length);
        return String.valueOf(ThreadLocalRandom.current().nextInt(origin, bound));
    }

    static String generateUsernameWithFirstRule() {
        List<String> chineseName = generateRandomChineseName();
        List<String> initialChars = chineseName.stream().map((x) -> {
            return x.substring(0, 1);
        }).collect(Collectors.toList());
        String initial = String.join("", initialChars);
        int nameWords = chineseName.size();
        String randomNumber = generateRandomNumberAsString(6 - nameWords, 10 - nameWords);
        return ThreadLocalRandom.current().nextInt(0, 10) < 7?initial + randomNumber:randomNumber + initial;
    }

    static String generateUsernameWithSecondRule() {
        List<String> chineseName = generateRandomChineseName();
        String surname = chineseName.get(0);
        int nameWords = surname.length();
        String randomNumber = generateRandomNumberAsString(6 - nameWords, 10 - nameWords);
        return ThreadLocalRandom.current().nextInt(0, 10) < 9?surname + randomNumber:randomNumber + surname;
    }

    static String generateUsernameWithThirdRule() {
        List<String> chineseName = generateRandomChineseName();
        String name = String.join("", chineseName);
        String randomNumber = generateRandomNumberAsString(1, 2);
        return name + randomNumber;
    }

    static String generateUsernameWithFourthRule() {
        String randomLetter = generateRandomLetter();
        String randomNumberAsString = generateRandomNumberAsString(6, 9);
        return ThreadLocalRandom.current().nextInt(0, 10) < 7?randomLetter + randomNumberAsString:randomNumberAsString + randomLetter;
    }

    static String generateUsernameWithFifthRule() {
        String oneRandomLetter = generateRandomLetter();
        String randomNumberAsString = generateRandomNumberAsString(4, 6);
        String anotherRandomLetter = generateRandomLetter();
        return oneRandomLetter + randomNumberAsString + anotherRandomLetter;
    }

    private static String generateRandomLetter() {
        return someLetters[ThreadLocalRandom.current().nextInt(0, someLetters.length)];
    }

    /**
     * 输出指定长度随机字符串（字母+数字）
     */
    public static String randomAlphanumeric(int length) {
        String randomStr;
        do {
            randomStr = RandomStringUtils.random(length, '0', 'z', true, true).toLowerCase();
        } while (length > 1 && (randomStr.matches("^[a-zA-Z]*") || randomStr.matches("^[0-9]*")));
        return randomStr;
    }
}
