package flinkJob.datatype;

public class Contribution {
    public String author;
    public String project;
    public Double score;
    public Double rate;

    public Contribution(){}

    public Contribution(String author, String project)
    {
        this.author = author;
        this.project = project;
    }

    public String toString()
    {
        return project + ", " + author + ", " + String.format("%.5f", rate);
    }

}
