package org.example.Mapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

public class SparkSQL {
    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://172.19.240.39:3306/contribution";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "123456";

    static int clock = 0;

    public void excuteUpdate(String[] list) {
        System.out.println(Arrays.toString(list));
        clock++;
        ArrayList<String> sqlList = new ArrayList<>();
        ArrayList<Double> scoreList = new ArrayList<>();
        System.out.println(Arrays.toString(list));
        Connection conn = null;
        Statement stmt = null;
        String add = list[2].split(",")[0];
        int addition = Integer.parseInt(add);
        String del = list[3].split(",")[0];
        int deletion = Integer.parseInt(del);
        System.out.println(addition);
        try{
            String sql;
            sql = String.format("SELECT * FROM spark WHERE project = '%s' and author = '%s'",list[0],list[1]);
            sqlList.add(sql);
            double score = addition * 0.5 + deletion * 0.1;
            scoreList.add(score);
            if (clock == 100){
                conn = SQLInit();
                stmt = conn.createStatement();
                clock = 0;
                for (int i = 0; i < sqlList.size(); i++){
                    excuteEach(sqlList.get(i),conn,scoreList.get(i));
                }
                updateRate(conn);
                sqlList = new ArrayList<>();
                scoreList = new ArrayList<>();
                conn.close();
            }
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
    public void excuteEach(String sql, Connection conn, Double scoreChange) throws SQLException {

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        String project = sql.split("'")[1];
        String author = sql.split("'")[3];
//        System.out.println(project);
//        System.out.println(author);

        double score;
        if (rs.next()) {
            // 通过字段检索
            score = rs.getDouble("score");
            // 输出数据
            score = score + scoreChange;
//            System.out.println(score);
            sql = String.format("UPDATE spark SET project = '%s',author = '%s',score = %f,rate = 0 WHERE project = '%s' and author = '%s';",project,author,score,project,author);
//            System.out.println(sql);
            stmt.executeUpdate(sql);
        }
        else {
            score = 0 + scoreChange;
            sql = String.format("INSERT INTO spark(project,author,score,rate) VALUES ('%s','%s',%f,0)", project,author,score);
//            System.out.println(sql);
            stmt.executeUpdate(sql);
        }
        rs.close();
    }

    public static Connection SQLInit() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection(DB_URL,USER,PASS);
    }

    public static void updateRate(Connection conn) throws SQLException {
        Statement stmtTemp = conn.createStatement();
        String sql = "SELECT s.project,author,score,score / score_total * 100 as rate " +
                "FROM spark s " +
                "INNER JOIN(SELECT project,SUM(score) as score_total FROM spark GROUP BY project) a " +
                "ON s.project = a.project;";
        ResultSet rsTemp = stmtTemp.executeQuery(sql);
        Statement stmt = conn.createStatement();
        while (rsTemp.next()){
            sql = String.format("UPDATE spark set rate = %f where project = '%s' and author = '%s'",rsTemp.getDouble("rate"),rsTemp.getString("project"),rsTemp.getString("author"));
            stmt.executeUpdate(sql);
        }
        rsTemp.close();
        stmt.close();
        stmtTemp.close();
    }
}

