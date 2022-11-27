import pandas as pd

path = "spring.csv"
df = pd.read_csv(path)
col_n = 0
d = ["spring_boot"] * 40359
df.insert(col_n, "name", d, allow_duplicates=False)
df = df[::-1]
df.to_csv("spring_reserve.csv", index=False)
print(df)