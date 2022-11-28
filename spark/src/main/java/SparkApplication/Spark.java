package SparkApplication;

import SparkApplication.Mapper.SparkSQL;
import org.apache.spark.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Spark {
    String hostname = "localhost";
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
            SparkSQL sparkSQL = new SparkSQL();
            sparkSQL.excuteUpdate(tempList);
        }
    }
}
