package com.galigaigai.jobbridge.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/24 0024.
 */
public class ParseStringUtil {
    public static String[] parseString(String str){
        //String intentionCity = "[\"上海\",\"北京\"]";
        str = str.replace("[","");
        str = str.replace("]","");
        str = str.replace("\"","");
        //String intentionCity = "\"上海\",\"北京\"";
        return str.split(",");

    }
    public static Date parseStringToDate(String str) throws ParseException {

        SimpleDateFormat sdf = null;
        Date date = null;
        if (str == null || "".equals(str)){
            return date;
        }
        if (str.contains("-")){
            sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
            date = sdf.parse(str);
        }else if(str.contains("/")){
            sdf = new SimpleDateFormat("yyyy/MM/dd");//小写的mm表示的是分钟
            date = sdf.parse(str);
        }else if(str.contains(".")){
            sdf = new SimpleDateFormat("yyyy.MM.dd");//小写的mm表示的是分钟
                    date = sdf.parse(str);
        }
        return date;

    }
    public static String parseDateToString(Date date){
        String time = DateFormat.getDateInstance().format(date);
        return time;

    }
}
