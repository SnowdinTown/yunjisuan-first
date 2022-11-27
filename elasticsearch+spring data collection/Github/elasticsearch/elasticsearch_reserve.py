import pandas as pd

path = "elasticsearch.csv"
df = pd.read_csv(path)
col_n = 0
d = ["elasticsearch"] * 65446
df.insert(col_n, "name", d, allow_duplicates=False)
df = df[::-1]
df.to_csv("elasticsearch_reserve.csv", index=False)
print(df)