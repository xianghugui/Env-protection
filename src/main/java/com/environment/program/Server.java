package com.environment.program;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private int port;
    public Server(int port) {
        this.port = port;
    }
    public void Listen() throws IOException{
        ServerSocket serverSocket = new ServerSocket(this.port);
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        byte[] arry = new byte[1024];
        int length = 0;
        while((length=inputStream.read(arry))!=-1){
            outputStream.write(arry, 0, length);
            outputStream.flush();
        }
    }
    public static void main(String[] args) throws IOException{
        Server server = new Server(9999);
        server.Listen();
    }
}