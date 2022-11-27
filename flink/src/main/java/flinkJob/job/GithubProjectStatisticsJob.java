package flinkJob.job;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import flinkJob.datatype.CommitRecord;
import flinkJob.datatype.Contribution;
import flinkJob.sink.MysqlSink;
import flinkJob.sources.CommitSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GithubProjectStatisticsJob {
    private static Logger logger  = LoggerFactory.getLogger(GithubProjectStatisticsJob.class);

    public static void main(String[] args) throws Exception {
        if (0 == args.length) throw new IllegalArgumentException();

        String filePath = args[0];

        logger.info("Start Job from source : " + filePath);
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<CommitRecord> commits = env
                .addSource(new CommitSource(filePath))
                .name("commits");

        DataStream<Contribution> contributes = commits
                .keyBy(commit -> commit.projectName)
                .process(new ContributionCounter())
                .name("contribution-counter");

        contributes
                .addSink(new MysqlSink())
                .name("mysql");

        env.execute("Github Project Statistics");
    }
}
