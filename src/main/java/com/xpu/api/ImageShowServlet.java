package com.xpu.api;

import com.xpu.dao.ImageDao;
import com.xpu.model.Image;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class ImageShowServlet extends HttpServlet {
    static HashSet<String> set = new HashSet<>();

    static {
        set.add("http://localhost:8080/image_server/");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //防盗链
        String referer = req.getHeader("Referer");
        if (!set.contains(referer)){
            resp.setStatus(200);
            resp.setContentType("application/json;charset=utf8");
            resp.getWriter().write("{\"ok\":false,\"reason\":\"没有访问权限\"}");
            return;
        }

        //1.获取imageId参数
        String imageId = req.getParameter("imageId");
        if (imageId == null || imageId.equals("")){
            resp.setStatus(200);
            resp.getWriter().write("{\"ok\":false,\"reason\":\"没有选择图片\"}");
            return;
        }

        //2.使用Dao层
        ImageDao dao = new ImageDao();
        Image image = dao.selectOne(Integer.parseInt(imageId));
        if (image == null){
            resp.setStatus(200);
            resp.getWriter().write("{\"ok\":false,\"reason\":\"不存在图片\"}");
            return;
        }
        //3.根据路径打开文件，读取其中内容，写入响应对象中
        resp.setContentType(image.getContentType());
        File file = new File(image.getPath());
        //由于图片是二进制文件，应该使用字节流的形式读文件
        ServletOutputStream outputStream = resp.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        while (true){
            int len = fileInputStream.read(buffer);
            if (len == -1){
                //读取结束
                break;
            }
            //此时已经读到一部分数据，放到buffer中，吧去写到响应对象中
            outputStream.write(buffer);
        }
        fileInputStream.close();
        outputStream.close();
    }
}
