/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.ssystem.common.util;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 *  FileWriter
 *
 * @version 0.1
 *
 * @since 2015.03.18
 *
 * @author shin.
 */

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private FileOutputStream fos;

    public String writeFile(String path, String fileName, MultipartFile file) {
        return writeFile(file, path, fileName);
    }

    public String writeFile(MultipartFile file, String path, String fileName){
        try {
            byte[] fileData = file.getBytes();
            fos = new FileOutputStream(path + fileName);
            if(!fileName.endsWith(".exe") && !fileName.endsWith(".sh") 
               && !fileName.endsWith(".bat")){
                fos.write(fileData);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e2) {
                    logger.info(e2.toString());
                }
            }
        }
        return fileName;
    }

    /**
     * 파일로부터 byte 배열 읽어오기
     */
    public static byte[] readFile(String path, String fileName) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path + fileName));
        int length = bis.available();
        byte[] bytes = new byte[length];
        bis.read(bytes);
        bis.close();
        return bytes;
    }

    /**
     * 파일로부터 byte 배열 읽어오기
     */
    public static byte[] readFile(Map<String, Object> parameter) throws IOException {
        String atcFileDir = String.valueOf(parameter.get("atcFileDir"));
        String atcFileId = String.valueOf(parameter.get("atcFileId"));
        return readFile(atcFileDir, atcFileId);
    }


    public static boolean isDirectory(String path) {
        return new File(path).isDirectory();
    }
}

