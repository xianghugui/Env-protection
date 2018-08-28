package com.environment.program.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static final String URL="jdbc:mysql://118.31.250.119:3306/environment-protection?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&useSSL=false&autoReconnect=true";
    private static final String NAME="root";
    private static final String PASSWORD="root";
    private static Connection conn=null;
   //静态代码块（将加载驱动、连接数据库放入静态块中）
     static{
         try {
             //1.加载驱动程序
             Class.forName("com.mysql.jdbc.Driver");
             //2.获得数据库的连接
             conn = DriverManager.getConnection(URL, NAME, PASSWORD);
         } catch (ClassNotFoundException e) {
             e.printStackTrace();
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }
     //对外提供一个方法来获取数据库连接
     public static Connection getConnection(){
         return conn;
     }
}
