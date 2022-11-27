package flinkJob.job;

import org.apache.flink.api.common.state.*;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import flinkJob.datatype.CommitRecord;
import flinkJob.datatype.Contribution;

import java.util.ArrayList;


public class ContributionCounter extends
        KeyedProcessFunction<String, CommitRecord, Contribution>
{
    static final double addRate = 0.5;
    static final double deleteRate = 0.1;
    static final long interval = 1000;

    private transient ListState<Contribution> contributionState;
    private transient ValueState<Double> tScoreState;
    private transient ValueState<Long> timeState;

    @Override
    public void open(Configuration parameters)
    {
        ListStateDescriptor<Contribution> contributionDesc =
                new ListStateDescriptor<>(
                        "contribution",
                        Contribution.class
                );
        contributionState = getRuntimeContext().getListState(contributionDesc);

        ValueStateDescriptor<Double> tScoreDesc = new ValueStateDescriptor<>(
                "total score",
                Types.DOUBLE);
        tScoreState = getRuntimeContext().getState(tScoreDesc);

        ValueStateDescriptor<Long> timeDesc = new ValueStateDescriptor<>(
                "time stamp",
                Types.LONG);
        timeState = getRuntimeContext().getState(timeDesc);
    }

    @Override
    public void processElement(
            CommitRecord commits,
            Context context,
            Collector<Contribution> collector) throws Exception
    {
        Double tScore = tScoreState.value();
        if (null == tScore)
        {
            tScoreState.update(0.0);
            contributionState.update(new ArrayList<>());
            timeState.update(context.timerService().currentProcessingTime());
        }

        double addScore = commits.additions * addRate +
                commits.deletions * deleteRate;
        tScoreState.update(tScoreState.value() + addScore);
        tScore = tScoreState.value();

        boolean newContributor = true;
        for (Contribution contribution : contributionState.get()) {
            if (commits.author.equals(contribution.author)) {
                contribution.score += addScore;
                newContributor = false;
            }
            contribution.rate = contribution.score / tScore * 100;
        }

        if (newContributor)
        {
            Contribution cont =
                    new Contribution(commits.author, commits.projectName);
            cont.score = addScore;
            cont.rate = addScore / tScore * 100;
            contributionState.add(cont);
        }

        long timer = context.timerService().currentProcessingTime() - interval;
        if (timer > timeState.value())
        {
            contributionState.get().forEach(collector::collect);
            timeState.update(context.timerService().currentProcessingTime());
        }
    }
}
