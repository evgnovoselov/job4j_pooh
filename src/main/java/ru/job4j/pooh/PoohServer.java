package ru.job4j.pooh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PoohServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(9000)) {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                try (OutputStream output = socket.getOutputStream();
                     InputStream input = socket.getInputStream()) {
                    byte[] buff = new byte[1_000_000];
                    int total = input.read(buff);
                    String text = new String(Arrays.copyOfRange(buff, 0, total), StandardCharsets.UTF_8);
                    System.out.println(text);
                    output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                    output.write(text.getBytes());
                }
            }
        }
    }
}
