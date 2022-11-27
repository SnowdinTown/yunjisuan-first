package flinkJob.sink;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import flinkJob.datatype.Contribution;


public class LogSink implements SinkFunction<Contribution> {
    private static final Logger LOG = LoggerFactory.getLogger(LogSink.class);

    public LogSink() {
    }

    public void invoke(Contribution value, Context context) {
        LOG.info(value.toString(), "test");
    }
}
