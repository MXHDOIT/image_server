package com.xpu.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/image_server?characterEncoding=utf8&useSSL=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static volatile DataSource dataSource = null;

    //双重校验锁
    public static DataSource  getDataSource(){
        if(dataSource == null){
            synchronized (DBUtil.class){
                if(dataSource == null){
                    dataSource = new MysqlDataSource();
                    ((MysqlDataSource) dataSource).setURL(URL);
                    ((MysqlDataSource) dataSource).setUser(USERNAME);
                    ((MysqlDataSource) dataSource).setPassword(PASSWORD);
                }
            }
        }
        return dataSource;
    }

    //获取连接
    public static Connection getConnection(){
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //关闭连接
    //关闭顺序：先创建后关闭
    public static void close(Connection c, PreparedStatement p, ResultSet r){
        try {
            if (r!=null){
                r.close();
            }

            if(p != null){
                p.close();
            }

            if (c != null){
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
