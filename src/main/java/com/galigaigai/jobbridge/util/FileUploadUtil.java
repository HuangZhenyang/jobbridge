package com.galigaigai.jobbridge.util;

import com.galigaigai.jobbridge.controller.SignController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by HanrAx on 2018/3/20 0020.
 */
public class FileUploadUtil {

    public static void uploadFile(MultipartFile file) throws IOException {
        String companyLogoPath = SignController.class.getResource("").getPath();
        int index = companyLogoPath.indexOf("/jobbridge/target/") + 10;
        companyLogoPath = companyLogoPath.substring(0, index)
                + "/src/main/resources/static/img/comlogo/"
                .replace("/", "\\");

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(
                new File(companyLogoPath + file.getOriginalFilename())));

        out.write(file.getBytes());
        out.flush();
        out.close();
    }

}
