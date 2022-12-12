from __future__ import division

from pyspark import Row


def analysis(df1, df2):
    num1 = df1.count()
    num2 = df2.count()
    temp = df1.select("name").intersect(df2.select("name"))
    length = temp.count()
    if length / num1 > 0.9:
        return 2
    elif length / num2 > 0.9:
        return 1
    else:
        return 0


def unionList(row, add, dele):
    # print(row)
    if row["topic"] == add:
        top = add + ',' + dele
        n = row["name"]
    else:
        top = row["topic"]
        n = row["name"]
    row = Row(topic=top, name=n)
    # print(row)
    return row
