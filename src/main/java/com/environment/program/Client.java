package com.environment.program;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
    private String ip;
    private int port;
    public Client(String ip,int port) {
        this.ip = ip;
        this.port = port;
    }
    public void conn() throws UnknownHostException, IOException{
        Socket socket = new Socket(this.ip,this.port);
        InputStream inputStream = socket. getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        byte [] aa = {0x16,0x52, (byte) 0xB8};
        outputStream.write(aa);
        outputStream.flush();
        byte[] arry = new byte[1024];
        int length = 0;
        while((length=inputStream.read(arry))!=-1){
            for(int i=0;i<length;i++){
                System.out.print(arry[i]);
            }
        }
        outputStream.close();
        inputStream.close();
    }
    public static void main(String[] args) throws IOException{
        Client cient = new Client("localhost",9999);
        cient.conn();
    }
}

