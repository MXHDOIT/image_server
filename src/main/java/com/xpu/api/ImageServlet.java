package com.xpu.api;

import com.google.gson.Gson;
import com.xpu.dao.ImageDao;
import com.xpu.model.Image;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//@WebServlet("/imageServlet")
public class ImageServlet extends HttpServlet {

    /**
     * 查看图片属性：既可以查看所有，也可以查看局部
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //考虑所有图片信息或指定属性
        //获取请求参数:如果有这是指定属性，没有则是所有图片信息
        String imageId = req.getParameter("imageId");
        ImageDao dao = new ImageDao();
        //操作json
        Gson gson = new Gson();
        String res = null;
        if (imageId == null || imageId.equals("")){
            //查看所有图片信息
            List<Image> images = dao.selectAll();
            res = gson.toJson(images);
        }else{
            //查看指定图片
            Image image = dao.selectOne(Integer.parseInt(imageId));
            res = gson.toJson(image);
        }
        resp.setContentType("application/json;charset=utf8");
        resp.getWriter().write(res);
    }


    /***
     * 上传文件
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取图片的属性信息，存入数据库
        //1.1 创建factory和upload对象，为获取到图片信息做准备工作
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> fileItems = null;
        //获取图片属性存入数据库
        try {
            fileItems = upload.parseRequest(req);
        } catch (FileUploadException e) {
            e.printStackTrace();
            resp.setContentType("application/json;charset=utf8");
            resp.getWriter().write("{\"ok\":false,\"reason\":\"文件解析出错\"}");
        }
        FileItem fileItem = fileItems.get(0);
        //封装图片属性
        Image image = new Image();
        image.setImageName(fileItem.getName());
        image.setSize((int) fileItem.getSize());
        image.setUploadTime(new SimpleDateFormat().format(new Date()));
        image.setMd5(DigestUtils.md5Hex(fileItem.get()));
        image.setContentType(fileItem.getContentType());
        image.setPath("./image/"+image.getMd5());

        ImageDao dao = new ImageDao();
        //注意查询与插入顺序
        Image existMd5 = dao.selectMd5(image.getMd5());
        dao.insert(image);

        //2.上传图片内容
        if (existMd5 == null) {
            File file = new File(image.getPath());
            if(!file.exists()){
                file.getParentFile().mkdir();
                file.createNewFile();
            }
            try {
                fileItem.write(file);
            } catch (Exception e) {
                e.printStackTrace();
                resp.setContentType("application/json;charset=utf8");
                resp.getWriter().write("{\"ok\":false,\"reason\":\"图片内容上传出错\"}");
            }
        }
        //3. 返回结果
        resp.setContentType("application/json;charset=utf8");
        resp.getWriter().write("{\"ok\":true}");
    }

    /**
     * 删除图片
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取图片id
        String imageId = req.getParameter("imageId");
        if (imageId == null || imageId.equals("")){
            resp.setStatus(200);
            resp.getWriter().write("{\"ok\":false,\"reason\":\"没有选择图片\"}");
            return;
        }
        //调用Dao层
        ImageDao dao = new ImageDao();
        Image image = dao.selectOne(Integer.parseInt(imageId));
        if (image == null){
            resp.setStatus(200);
            resp.getWriter().write("{\"ok\":false,\"reason\":\"不存在图片\"}");
            return;
        }
        //删除数据库文件
        dao.deleteOne(Integer.parseInt(imageId));
        Image existMd5 = dao.selectMd5(image.getMd5());
        //删除磁盘文件
        if(existMd5 == null){
            File file = new File(image.getPath());
            file.delete();
            resp.setStatus(200);
            resp.getWriter().write("{\"ok\":true}");
        }
    }
}
