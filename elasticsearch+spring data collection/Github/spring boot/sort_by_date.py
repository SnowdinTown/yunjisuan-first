import pandas as pd
import datetime

#导入数据集
data = pd.read_csv("spr_ela_reserve.csv")

# data = data.loc[:, ['author', 'date']] # 获取数据集中列名为date和value这两列

# 标准化日期，获取时间的“年、月、日”
def change_date(s):
    s = datetime.datetime.strptime(s, "%b %d, %Y")  # 把日期标准化，转化结果如：2015/1/4 => 2015-01-04 00:00:00
    s = str(s)  # 上一步把date转化为了时间格式，因此要把date转回str格式
    return s[:10] # 只获取年月日，即“位置10”之前的字符串

data['date'] = data['date'].map(change_date)  # 用change_date函数处理列表中date这一列，如把“2015/1/4”转化为“2015-01-04”
data = data.sort_values(by='date')  # 按date这一列进行排序
data.to_csv("spring+elasticsearch_output.csv", index=False)
print(data)

