package flinkJob.datatype;

public class CommitRecord {
    public String projectName;
    public String overview;
    public String author;
    public String date;
    public Integer changedFiles;
    public Integer additions;
    public Integer deletions;

    public CommitRecord(){}
    public CommitRecord(String[] fields)
    {
        projectName = fields[0];
        overview = fields[1];
        author = fields[2];
        date = fields[3];
        changedFiles = Integer.parseInt(fields[4].replace(",", "").trim());
        additions = Integer.parseInt(fields[5].replace(",", "").trim());
        deletions = Integer.parseInt(fields[6].replace(",", "").trim());
    }
}
