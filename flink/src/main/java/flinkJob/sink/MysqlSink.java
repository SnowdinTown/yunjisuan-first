package flinkJob.sink;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import flinkJob.datatype.Contribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MysqlSink extends RichSinkFunction<Contribution> {

    private static final Logger log = LoggerFactory.getLogger(MysqlSink.class);
    private Connection conn = null;
    private PreparedStatement insertStmt = null;
    private PreparedStatement updateStmt = null;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(
                "jdbc:mysql://172.19.240.39:3306/contribution",
                "root",
                "123456");
        insertStmt = conn.prepareStatement(
                "insert into flink(project,author,score,rate) values (?,?,?,?)");
        updateStmt = conn.prepareStatement(
                "update flink set score=?,rate=? where project=? and author=?");

        PreparedStatement initial = conn.prepareStatement("TRUNCATE TABLE flink");
        initial.execute();
        initial.close();
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (null != conn) {
            conn.close();
        }
        if (null != insertStmt)
        {
            insertStmt.close();
        }
        if (null != updateStmt)
        {
            updateStmt.close();
        }
    }

    @Override
    public void invoke(Contribution value, Context context) throws Exception {
        updateStmt.setDouble(1, value.score);
        updateStmt.setDouble(2, value.rate);
        updateStmt.setString(3, value.project);
        updateStmt.setString(4, value.author);
        updateStmt.execute();

        if (0 == updateStmt.getUpdateCount())
        {
            insertStmt.setString(1, value.project);
            insertStmt.setString(2, value.author);
            insertStmt.setDouble(3, value.score);
            insertStmt.setDouble(4, value.rate);
            insertStmt.execute();
        }
    }

}
