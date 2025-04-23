package com.newsvision.global.Utils;

public class JasoUtils {
    public static String splitJaso(String text) {
        StringBuilder sb = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (ch >= 0xAC00 && ch <= 0xD7A3) {
                int base = ch - 0xAC00;
                int chosung = base / (21 * 28);
                int jungsung = (base % (21 * 28)) / 28;
                int jongsung = base % 28;

                sb.append((char) (0x3131 + chosung)); // 초성
                sb.append((char) (0x314F + jungsung)); // 중성

                if (jongsung != 0) {
                    sb.append((char) (0x3131 + jongsung)); // 종성 (0이면 없음)
                }
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static String extractChosung(String text) {
        final char[] CHO = {
                'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ',
                'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ',
                'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
        };

        StringBuilder sb = new StringBuilder();
        for (String word : text.split("\\s+")) {
            for (char ch : word.toCharArray()) {
                if (ch >= 0xAC00 && ch <= 0xD7A3) {
                    int code = ch - 0xAC00;
                    int choIndex = code / (21 * 28);
                    sb.append(CHO[choIndex]);
                }
            }
            sb.append(" "); // 단어 단위로 공백
        }
        return sb.toString().trim(); // 끝 공백 제거
    }


}