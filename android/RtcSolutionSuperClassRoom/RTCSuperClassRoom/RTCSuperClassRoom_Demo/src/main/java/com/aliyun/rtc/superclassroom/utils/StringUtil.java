package com.aliyun.rtc.superclassroom.utils;

public class StringUtil {

    /**
     * @param str
     * @return 三个字添加一个空格
     */
    public static String getRoomCode(String str) {
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length ; i += 3) {
//            if (length - i <= length - 4 ) {      //防止ArrayIndexOutOfBoundsException
//                sb.append(str.substring(i, i + 3)).append("#");
//                sb.append(str.substring(i + 3));
//                break;
//            }
            sb.append(str.substring(i, i + 3)).append(" ");
        }
        return sb.toString().trim();

    }
}
