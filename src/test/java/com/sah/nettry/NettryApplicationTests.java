package com.sah.nettry;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

@SpringBootTest
class NettryApplicationTests {

    @Test
    void testNettry() {
        try {
            Socket socket=new Socket("127.0.0.1",8888);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter=new PrintWriter(outputStream);
            printWriter.write("学习nettry");
            printWriter.flush();
            socket.shutdownOutput();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
