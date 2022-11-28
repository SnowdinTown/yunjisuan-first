package org.example;


import com.csvreader.CsvReader;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class CsvRead {
    public ArrayList<String> readCsvByCsvReader(String filePath) {
        ArrayList<String> strList = null;
        try {
            ArrayList<String[]> arrList = new ArrayList<>();
            strList = new ArrayList<>();
            CsvReader reader = new CsvReader(filePath, ',', Charset.forName("UTF-8"));
            while (reader.readRecord()) {
//                System.out.println(Arrays.asList(reader.getValues()));
                arrList.add(reader.getValues()); // 按行读取，并把每一行的数据添加到list集合
            }
            reader.close();
//            System.out.println("读取的行数：" + arrList.size());
//            数据处理
            for (int row = 0; row < arrList.size(); row++) {
                String ele = arrList.get(row)[0] + ";" + arrList.get(row)[2] + ";"
                        + arrList.get(row)[5] + ";" + arrList.get(row)[6] + "\r\n";
                strList.add(ele);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strList;
    }
}
