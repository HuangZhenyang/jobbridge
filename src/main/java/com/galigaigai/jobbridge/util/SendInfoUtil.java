package com.galigaigai.jobbridge.util;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by SYunk on 2017/7/22.
 */
/*
* 后台想前台发送信息
* */
public class SendInfoUtil {
    public static void render(String content,String type,HttpServletResponse response) throws Exception{
        response.setContentType(type + ";charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(content);
        out.flush();
        out.close();
    }
}
