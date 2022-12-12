from graphframes import graphframe
from pyspark import SQLContext
from pyspark.sql import SparkSession, functions
from pyspark.sql.functions import desc
import pandas as pd


def toString(df, s):
    res = []
    for r in df.select(s).dropDuplicates().collect():
        res.append(r[s])
    return res


def toList(pList, tList):
    res = []
    for i in range(0, len(pList) - 1):
        res.append(["p" + str(i), pList[i]])
    for i in range(0, len(tList) - 1):
        res.append(["t" + str(i), tList[i]])
    return res


def trans(x, projectList, topicList):
    i = projectList.index(x["name"])
    j = topicList.index(x["topic"])
    # print(['p' + str(i), 't' + str(j)])
    return ['p' + str(i), 't' + str(j)]


def create(language, topic, projectList, df, sqlContext):
    df = df.select("topic","name")
    projectDF = df.filter(df.name.isin(projectList))
    topicList = toString(projectDF, "topic")
    # print(topicList)
    totalList = toList(projectList, topicList)
    vertices = sqlContext.createDataFrame(totalList, ["id", "name"])
    # vertices.show()
    edgesList = []
    # print(projectDF.count())
    for p in projectDF.collect():
        edgesList.append(trans(p, projectList, topicList))
    edgesDF = sqlContext.createDataFrame(edgesList, schema=["src", "dst"])
    edgesDF = edgesDF.withColumn("action", functions.lit("have"))
    g = graphframe.GraphFrame(vertices, edgesDF)
    m = g.find("(a)-[e]->(b);(c)-[e2]->(b)").filter("a<c").dropDuplicates()
    res = m.groupBy('a', 'c').count()
    res = res.toDF("node1", "node2", "weight")
    res = res.select("node1", "node2", (res.weight-2).alias("weight"))
    res = res.filter("weight>0")
    resP = res.select(res.node1["name"], res.node2["name"], "weight").toPandas()
    resP.to_csv('output/'+language+"/"+topic+"-re.csv", index=False)
    results = g.pageRank(resetProbability=0.15, maxIter=10)
    resP = results.vertices.sort(desc("pagerank")).toPandas()
    bool = resP.id.str.contains("t")
    resP = resP[bool]
    resP = resP[["name", "pagerank"]]
    resP.to_csv('output/'+language+"/"+topic+"-rank.csv", index=False)
    projectDic = {"names": projectList}
    resD = pd.DataFrame(projectDic)
    resD.to_csv('output/'+language+"/"+topic+"-node.csv", index=False)
    print("---------------------------End Of Topic "+topic+"---------------------------")


if __name__ == '__main__':
    create('java', 'spring-boot',['spring-boot', 'mall', 'jeecg-boot', 'spring-boot-examples', 'xkcoding', 'jhipster', 'spring-boot-admin',
            'apereo', 'newbee-mall', 'spring-boot-api-project-seed', 'hsweb-framework', 'Netflix', 'FEBS-Shiro',
            'springfox', 'JavaBooks', 'spring-cloud-netflix', 'Spring-Boot-In-Action', 'spring-boot-projects',
            'microservices-platform', 'spring-cloud-gateway', 'spring-boot-starter', 'atmosphere', 'jetlinks-community',
            'TelegramBots', 'microservices-demo', 'spring-cloud-kubernetes', 'jwt-spring-security-demo', 'ZHENFENG13',
            'dgs-framework', 'jasypt-spring-boot', 'springdoc-openapi', 'speedment', 'grpc-spring-boot-starter',
            'speedment', 'grpc-spring-boot-starter', 'spring-cloud-config', 'thinking-in-spring-boot-samples', 'Ward',
            'javahongxi', 'xxl-sso', 'smart-admin', 'spring-boot-demo', 'jeesite4', 'spring-cloud-sleuth',
            'weixin-java-mp-demo', 'genie', 'iBase4J', 'spring-microservices', 'lets-mica', 'spring-boot-samples',
            'segmentfault-lessons', 'techa03', 'retrofit-spring-boot-starter', 'metasfresh', 'logbook', 'tmobile',
            'jbot', 'spring-boot-jwt', 'mycollab', 'journaldev', 'jsql-injection', 'mogu_blog_v2', 'riskscanner'], '',
           '')
