import random
import re
import requests
import csv
import time

headers = {
    "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36"
}
f = open('spring.csv', mode='a', encoding='utf-8', newline='')
csv_writer = csv.writer(f)

for page in range(0, 1162):
    print(f'-------------------正在抓取第{page}页-----------------')
    url = f"https://github.com/spring-projects/spring-boot/commits/main?after=c93e248c46c2c4a7cef5596b23e54c715c52a204+{page*35+34}&branch=main&qualified_name=refs%2Fheads%2Fmain"
    response = requests.get(url, headers=headers, verify=False)
    content = response.text

    obj1 = re.compile(r'<p class="mb-1" >.*?href="(?P<href>.*?)">(?P<overview>.*?)</a>.*?'
                      r'<div class="f6 color-fg-muted min-width-0">.*?">'
                      r'(?P<author>.*?)(</span>|</a>).*? <relative-time datetime.*?">('
                      r'?P<date>.*?)</relative-time>', re.S)

#   obj2 = re.compile(r'<p class="mb-1" >.*?">', re.S)
    obj3 = re.compile(r'<strong>(?P<changed_file>.*?)changed file'
                      r'.*?<strong>(?P<additions>.*?)addition'
                      r'.*?<strong>(?P<deletions>.*?)deletion', re.S)
    result1 = obj1.finditer(content)
#   child_href_list = []
#   result2 = obj2.finditer(content)
    for iq in result1:
        dic1 = iq.groupdict()
        dic1.pop('href')
#        print(dic1.values())
        child_href = 'https://github.com' + iq.group('href')
        child_resp = requests.get(child_href, verify=False)
        result3 = obj3.search(child_resp.text)
        dic2 = result3.groupdict()
#        print(dic2.values())
        d = dic1.copy()
        d.update(dic2)
        csv_writer.writerow(d.values())
#       print(result3.group('changed_file', 'additions', 'deletions'))
        print(d.values())
        time.sleep(random.randint(1, 2))