package SparkApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Order(1)
public class SparkMain implements CommandLineRunner {

    public static void sparkMain() throws InterruptedException {
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
        } catch (ExecutionException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run(String... args) throws Exception {
        sparkMain();
    }
}
