package org.example;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Socket {
    public void socketConnection() throws IOException, InterruptedException {
        int port = 9999;
        ServerSocket serverSocket = new ServerSocket(port);

        java.net.Socket clientSocket = serverSocket.accept();
        OutputStream outputStream = clientSocket.getOutputStream();
        ArrayList<String> filePathList = new ArrayList<>();
        filePathList.add("src/main/resources/spring+elasticsearch.csv");
        CsvRead csvRead = new CsvRead();
        for (int i = 0; i < filePathList.size();i ++){
            String tempPath = filePathList.get(i);
            ArrayList<String> messages = csvRead.readCsvByCsvReader(tempPath);
            for (String message:messages){
                outputStream.write(message.getBytes());
                Thread.sleep(1);
            }
        }
    }
}
