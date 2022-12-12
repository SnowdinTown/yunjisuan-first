import requests
from bs4 import BeautifulSoup
import time
import csv
import random
import urllib3
from fake_useragent import UserAgent
# ua = UserAgent(use_cache_server=False)
# ua = UserAgent(cache=False)
# ua = UserAgent(verify_ssl=False)
ua = UserAgent()
urllib3.disable_warnings()

headers = {
    "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36",
#     "Host":"api.github.com",
     "Origin":"https://github.com",
     "Referer":"https://github.com/topics/java?o=desc&s=stars&page=1",
     "Cookie":"_octo=GH1.1.313234806.1666761713; logged_in=yes; dotcom_user=lydia-233; color_mode=%7B%22color_mode%22%3A%22auto%22%2C%22light_theme%22%3A%7B%22name%22%3A%22light%22%2C%22color_mode%22%3A%22light%22%7D%2C%22dark_theme%22%3A%7B%22name%22%3A%22dark%22%2C%22color_mode%22%3A%22dark%22%7D%7D; preferred_color_mode=light; tz=Asia%2FShanghai"
}
f = open('html.csv', mode='a', encoding='utf-8', newline='')
csv_writer = csv.writer(f)

for page in range(1, 51):
    print(f'-------------------正在抓取第{page}页------------------')
    url = f"https://github.com/topics/html?o=desc&s=stars&page={page}"
    resp = requests.get(url, headers=headers, verify=False)
    # print(resp.text)
    page = BeautifulSoup(resp.text, "html.parser")
#    obj = re.compile(r'<a data-hydro-click=".*?<a data-hydro-click=".*?">(?P<name>.*?)</a>', re.S)
#   正则表达式提取项目名字
#   在页面中，取出项目列表，一页20个
    projlist = page.find_all("article", class_="border rounded color-shadow-small color-bg-subtle my-4")
#   print(projlist)
    for proj in projlist:
        alist = proj.find("div", class_="d-flex flex-wrap border-bottom color-border-muted px-3 pt-2 pb-2").find_all("a")
#        result = obj.finditer(proj.text)
#        for it in result:
#            dic = it.groupdict()
#           print(dic.values())
        namel = proj.find_all("a")[1]
        for a in alist:
        #   print(a.text)
            d = dict(name=namel.text.strip().replace("\n", ""),
                     label=a.text.strip().replace("\n", ""))
            csv_writer.writerow(d.values())
            print(d.values())
    time.sleep(random.randint(2, 3))