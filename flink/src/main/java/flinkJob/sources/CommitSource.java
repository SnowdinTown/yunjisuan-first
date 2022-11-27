package flinkJob.sources;

import com.opencsv.CSVReader;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import flinkJob.datatype.CommitRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommitSource implements SourceFunction<CommitRecord> {

    public static final int SLEEP_MILLIS_PER_EVENT = 100;
    private static final int BATCH_SIZE = 100;
    private volatile boolean running = true;

    public String filePath;

    public CommitSource(String in)
    {
        filePath = in;
    }


    @Override
    public void run(SourceContext<CommitRecord> ctx) throws Exception {
        try (CSVReader cr = new CSVReader(new FileReader(filePath)))
        {
            // read property name
            cr.readNext();
            int batchSize = 1;
            while (running)
            {
                List<CommitRecord> commits = new ArrayList<>(BATCH_SIZE);

                // get commits
                for (int i = 1; i <= BATCH_SIZE; i++) {
                    String[] line = cr.readNext();
                    if (null == line)
                    {
                        running = false;
                        break;
                    }
                    commits.add(new CommitRecord(line));
                }

                commits.iterator().forEachRemaining(ctx::collect);

                if (batchSize < BATCH_SIZE) batchSize++;
                // don't go too fast
                Thread.sleep(SLEEP_MILLIS_PER_EVENT);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
