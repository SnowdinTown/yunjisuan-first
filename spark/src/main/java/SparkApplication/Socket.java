package SparkApplication;

import SparkApplication.CsvRead;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Socket {
    int clock = 0;
    public void socketConnection() throws IOException, InterruptedException {
        int port = 9999;
        ServerSocket serverSocket = new ServerSocket(port);

        java.net.Socket clientSocket = serverSocket.accept();
        OutputStream outputStream = clientSocket.getOutputStream();
        ArrayList<String> filePathList = new ArrayList<>();
        filePathList.add("src/main/resources/spring_boot.csv");
        CsvRead csvRead = new CsvRead();
        for (int i = 0; i < filePathList.size();i ++){
            String tempPath = filePathList.get(i);
            ArrayList<String> messages = csvRead.readCsvByCsvReader(tempPath);
            for (String message:messages){
                outputStream.write(message.getBytes());
                clock ++;
                if (clock == 10) {
                    clock = 0;
                    Thread.sleep(10);
                }
            }
        }
    }
}
