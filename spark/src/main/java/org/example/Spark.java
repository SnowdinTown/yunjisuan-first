package org.example;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.example.Mapper.SparkSQL;

import java.sql.SQLException;
import java.util.List;

public class Spark {

//    String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//    String DB_URL = "jdbc:mysql://172.19.240.39:3306/contribution";
//    String USER = "root";
//    String PASS = "123456";
    String hostname = "localhost";

    SparkSQL sparkSQL = new SparkSQL();
    public void connection() throws InterruptedException, SQLException, ClassNotFoundException {// Create a local StreamingContext with two working thread and batch interval of 1 second
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("SparkGetSocket");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.milliseconds(1));
        // Create a DStream that will connect to hostname:port, like localhost:9999
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream(hostname, 9999);
        dataManage(lines);
        jssc.start();
        jssc.awaitTermination();
    }

    public void dataManage(JavaReceiverInputDStream<String> lines) {
        lines.foreachRDD(stringJavaRDD -> update(stringJavaRDD.collect()));
    }

    public void update(List<String> list){
        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list);
            String[] tempList = list.get(i).split(";");
            sparkSQL.excuteUpdate(tempList);
        }
    }
}
