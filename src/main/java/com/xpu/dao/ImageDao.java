package com.xpu.dao;

import com.xpu.common.ImageServerException;
import com.xpu.model.Image;
import com.xpu.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageDao {

    /**
     * 插入一个image
     * @param image
     */
    public void insert(Image image){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.创建并拼装SQL
        String sql = "insert into image_table values(null,?,?,?,?,?,?)";
        PreparedStatement p = null;
        try {
            p = c.prepareStatement(sql);
            p.setString(1,image.getImageName());
            p.setInt(2,image.getSize());
            p.setString(3,image.getUploadTime());
            p.setString(4,image.getContentType());
            p.setString(5,image.getPath());
            p.setString(6,image.getMd5());
            int ret = p.executeUpdate();
            //3.执行SQL
            if (ret != 1){
                //程序出现问题
                throw new ImageServerException("插入数据库出错");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ImageServerException e) {
            e.printStackTrace();
        }finally {
            //4.关闭连接
            DBUtil.close(c,p,null);
        }
    }

    /**
     * 查找数据库中的所有图片的信息
     *  考虑数据过多，使用分页
     * @return
     */
    public List<Image> selectAll(){
        List<Image> res = new ArrayList<>();
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.构造SQL
        String sql = "select * from image_table";
        PreparedStatement p = null;
        ResultSet r = null;
        //3.执行SQL
        try {
            p = c.prepareStatement(sql);
            //4.处理结果集
            r = p.executeQuery();
            while (r.next()){
                Image image = new Image();
                image.setImageId(r.getInt("imageId"));
                image.setImageName(r.getString("imageName"));
                image.setSize(r.getInt("size"));
                image.setUploadTime(r.getString("uploadTime"));
                image.setContentType(r.getString("contentType"));
                image.setPath(r.getString("path"));
                image.setMd5(r.getString("md5"));
                res.add(image);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //5.关闭连接
            DBUtil.close(c,p,r);
        }

        return res;
    }

    /**
     * 根据imageId查找指定的图片信息
     * @param imageId
     * @return
     */
    public Image selectOne(int imageId){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.构造SQL
        String sql = "select * from image_table where imageId = ?";
        PreparedStatement p = null;
        ResultSet r = null;
        //3.执行SQL
        try {
            p = c.prepareStatement(sql);
            p.setInt(1,imageId);
            r = p.executeQuery();
            //4.处理结果集
            while (r.next()){
                Image image = new Image();
                image.setImageId(r.getInt("imageId"));
                image.setImageName(r.getString("imageName"));
                image.setSize(r.getInt("size"));
                image.setUploadTime(r.getString("uploadTime"));
                image.setContentType(r.getString("contentType"));
                image.setPath(r.getString("path"));
                image.setMd5(r.getString("md5"));
                return image;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(c,p,r);
        }

        return null;
    }

    /**
     * 根据imageId删除图片
     * @param imageId
     */
    public void deleteOne(int imageId){
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.构造SQL
        String sql = "delete from image_table where imageId = ?";
        PreparedStatement p = null;
        //3.执行SQL
        try {
            p = c.prepareStatement(sql);
            p.setInt(1,imageId);
            int ret = p.executeUpdate();
            if (ret != 1){
                throw new ImageServerException("删除出现异常");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ImageServerException e) {
            e.printStackTrace();
        }finally {
            //4.关闭连接
            DBUtil.close(c,p,null);
        }
    }


    /**
     * md5
     * @param md5
     * @return
     */
    public Image selectMd5(String md5) {
        //1.获取连接
        Connection c = DBUtil.getConnection();
        //2.构造SQL
        String sql = "select * from image_table where md5 = ?";
        PreparedStatement p = null;
        ResultSet r = null;
        //3.执行SQL
        try {
            p = c.prepareStatement(sql);
            p.setString(1,md5);
            r = p.executeQuery();
            //4.处理结果集
            if (r.next()){
                Image image = new Image();
                image.setImageId(r.getInt("imageId"));
                image.setImageName(r.getString("imageName"));
                image.setSize(r.getInt("size"));
                image.setUploadTime(r.getString("uploadTime"));
                image.setContentType(r.getString("contentType"));
                image.setPath(r.getString("path"));
                image.setMd5(r.getString("md5"));
                return image;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(c,p,r);
        }

        return null;
    }
}
