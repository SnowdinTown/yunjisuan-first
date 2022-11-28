package org.example;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws SQLException, InterruptedException, ClassNotFoundException {
        System.out.println("Start");
        CompletableFuture<Void> socketCF = CompletableFuture.runAsync(() -> {
            Socket socket = new Socket();
            try {
                socket.socketConnection();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<Void> sparkCF = CompletableFuture.runAsync(() -> {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            Spark spark = new Spark();
            try {
                spark.connection();
            } catch (InterruptedException | ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<Void> all = CompletableFuture.allOf(socketCF,sparkCF);
        try {
            all.get();
        } catch (ExecutionException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}