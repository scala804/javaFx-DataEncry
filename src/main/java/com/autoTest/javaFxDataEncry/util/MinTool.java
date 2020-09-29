package com.autoTest.javaFxDataEncry.util;


import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinTool {




    public static boolean canParseInt(String string){
        if(StringUtils.isEmpty(string)){
            return false;
        }
        return string.matches("\\d+");
    }


    public static Map<String, Object> abandanNull(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val = new Object();
            try {
                val = f.get(obj);
                // 得到此属性的值
                map.put(f.getName(), val);// 设置键值
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        System.out.println("单个对象的所有键值==反射==" + map.toString());
        return map;
    }


    public static int getNumberDecimalDigits(String number) {
        String[] num = number.split("\\.");
        if (num.length == 2) {
            for (;;){
                if (num[1].endsWith("0")) {
                    num[1] = num[1].substring(0, num[1].length() - 1);
                }else {
                    break;
                }
            }
            return num[1].length();
        }else {
            return 0;
        }
    }


    public static boolean isLegalDate(String sDate) {
        int legalLen = 10;
        if ((sDate == null) || (sDate.length() != legalLen)) {
            return false;
        }

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isLegalTime(String time) {
        int legalLen = 8;
        if ((time == null) || (time.length() != legalLen)) {
            return false;
        }

        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = formatter.parse(time);
            return time.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean dateCompareResult(String dateStringOne,String  dateStringTwo){
        boolean booleanDate;
        DateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
        try {
            Date dateOne = formatter.parse(dateStringOne);
            Date dateTwo = formatter.parse(dateStringTwo);
           int compareTo=dateOne.compareTo(dateTwo);
           if(compareTo == -1){
               booleanDate=false;
               System.out.println(dateStringOne+"比"+dateStringTwo+"早");
           }else {
               booleanDate=true;
           }

        } catch (ParseException e) {
            booleanDate=false;
        }
        return booleanDate;
    }


    public static boolean timeCompareResult(String s1,String s2){
        try {
            if (s1.indexOf(":")<0||s1.indexOf(":")<0) {
                System.out.println("格式不正确");
            }else{
                String[]array1 = s1.split(":");
                int total1 = Integer.valueOf(array1[0])*3600+Integer.valueOf(array1[1])*60+Integer.valueOf(array1[2]);
                String[]array2 = s2.split(":");
                int total2 = Integer.valueOf(array2[0])*3600+Integer.valueOf(array2[1])*60+Integer.valueOf(array2[2]);
                return total1-total2>=0?true:false;
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            return true;
        }
        return false;

    }

    public static boolean isDecimal(String orginal){
      return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
      }

    private static boolean isMatch(String regex, String orginal){
       if (orginal == null || orginal.trim().equals("")) {
          return false;
       }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
      }



}
