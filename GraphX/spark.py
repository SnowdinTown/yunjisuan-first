import os.path
from os import mkdir

from pyspark import SparkContext
from pyspark.sql import SQLContext, functions, DataFrame
from graphframes import GraphFrame
from pyspark.sql import SparkSession
import pandas as pd
from pyspark.sql.functions import desc

import graph
import similatiry


def startSpark(language):
    # Start Connect
    spark = SparkSession.builder.appName("graph") \
        .master("local[*]") \
        .getOrCreate()
    sc = spark.sparkContext
    sc.setLogLevel("WARN")
    # read
    lines = sc.textFile("resources/" + language + ".csv")
    rows = lines.map(lambda x: x.split(','))
    sqlContext = SQLContext(sc)
    df = sqlContext.createDataFrame(rows, schema=["name", "topic"])
    # topic filter & get useful project
    topicDF = df.groupby(df["topic"]).count()
    topicDF = topicDF.filter(topicDF["count"] >= 50).orderBy(desc("count"))
    topicList = []
    for r in topicDF.select("topic").collect():
        topicList.append(r["topic"])
    projectDF = df.filter(df.topic.isin(topicList)).select('topic', 'name').orderBy("topic")
    # analyze similarity 可能存在的重合
    delList = []
    for i in range(1, len(topicList) - 1):
        for j in range(i + 1, len(topicList) - 1):
            # print(topicList[i])
            res = similatiry.analysis(projectDF.filter(projectDF["topic"] == topicList[i]),
                                      projectDF.filter(projectDF["topic"] == topicList[j]))
            if res == 1:
                delList.append(topicList[j])
            elif res == 2:
                delList.append(topicList[i])
    for i in range(0, len(delList) - 1):
        topicList.remove(delList[i])
    for i in range(1, len(topicList) - 1):
        projects = []
        temp = projectDF.filter(projectDF["topic"] == topicList[i])
        # temp.show()
        for p in temp.collect():
            projects.append(p["name"])
    # temp = projectDF.filter(projectDF["topic"] == "spring-boot")
    # tt = []
    # for t in temp.collect():
    #     tt.append(t["name"])
    # print(tt)
    # 按照标签分类建立图 分开建图
    # 建图方法：1.筛选出标签对应的project 2.建立图，节点为项目&项目对应标签（所以应该按照项目名称筛选）项目与项目之间是依靠着标签相连的，然后再进行图计算操作
    # pd1 = projectDF.toPandas()
    # pd1.to_csv("filter.csv", index=False)
    # df = df.withColumn("relationship", functions.lit("have"))
    # df.show()


if __name__ == '__main__':
    # temp = ['c', 'cpp', 'go', 'html', 'java', 'javascript', 'php', 'python', 'ruby']
    temp = ['java']
    cPath = os.getcwd()
    for t in temp:
        isExists = os.path.exists(cPath+'/output/'+t+'/')
        if not isExists:
            mkdir(cPath+'/output/'+t+'/')
            print("-------------------mkdir "+t+"-------------------")
        startSpark(t)
