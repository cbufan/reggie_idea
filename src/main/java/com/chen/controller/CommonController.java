package com.chen.controller;

import com.chen.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * description:
 * className:ComonController
 * author: chenqifan
 * date:2023/3/412:31
 **/

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private Environment environment;

    @GetMapping("/download")
    public void download(HttpServletResponse response,String name){
        response.setContentType("image/jpeg");
        //输入流，通过输入流读取文件内容
        try (FileInputStream fileInputStream = new FileInputStream(new File(environment.getProperty("reggie.picUploadPath")+name));
             ServletOutputStream outputStream = response.getOutputStream()
        ){
            byte[] bytes = new byte[1024];
            int len;
            while((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @description: 文件上传
     * @author: chenqifan
     * @date: 12:49 2023/3/4
     **/


    @PostMapping("/upload")
    public Result<String> upload(@RequestBody MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString()+suffix;
        log.info(environment.getProperty("reggie.picUploadPath")+fileName);
        File dir = new File(environment.getProperty("reggie.picUploadPath"));
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(environment.getProperty("reggie.picUploadPath")+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success(fileName);
    }
}
