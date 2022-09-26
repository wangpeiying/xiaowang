package com.itheima.reggie.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

//文件的上传和下载
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String path;

    //文件上传
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //file是一个临时文件，需要转存，否则本次请求结束后就消失
        //使用UUID随机生成文件名
        //获取文件后缀
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //截取后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

        String FileName = UUID.randomUUID().toString() + substring;

        //需要判断path是否存在
        File dir = new File(path);
        if (!dir.exists()) {
            //目录不存在，需要创建
            dir.mkdirs();
        }
        try {
            //将临时文件存储到指定位置
            file.transferTo(new File(path + FileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //需要返回文件名，以便保存到数据库
        return R.success(FileName);
    }

    //文件下载
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        //输入流，读取文件内容
        try {
            FileInputStream fileInputStream=new FileInputStream(new File(path+name));

            //输出流，将文件写回浏览器
            //设置写回浏览器的文件格式
            response.setContentType("image/jpeg");

            byte[] bytes=new byte[1024];
            int len=0;
            ServletOutputStream outputStream = response.getOutputStream();
            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭流
            fileInputStream.close();
            outputStream.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

