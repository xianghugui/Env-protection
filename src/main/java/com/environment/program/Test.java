package com.environment.program;

import com.environment.program.bean.Parameter;
import com.environment.program.util.DBUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class Test {
    private static final int PORT = 8888;

    public static void connect() throws IOException  {
        ServerSocket serverSocket = new ServerSocket(PORT);
        // 一旦有堵塞, 则表示服务器与客户端获得了连接
        while (true) {
            Socket client = serverSocket.accept();
            System.out.println("------已连接------");
            new KeepThread(client);
            new RecvThread(client);
        }
    }


    private static class KeepThread implements Runnable {

        private Socket socket;

        public KeepThread(Socket client) {
            socket = client;
            new Thread(this).start();
        }

        @Override
        public void run() {
            try {
                System.out.println("=====================开始发送心跳包==============");
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    OutputStream outStr = socket.getOutputStream();
                    outStr.write("send heart beat data package !".getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    static class RecvThread implements Runnable {

        private Socket socket;

        public RecvThread(Socket client) {
            socket = client;
            new Thread(this).start();
        }

        private int change(byte i, byte j){
            return Math.abs(Integer.parseInt(String.valueOf(i)) << 2 | Integer.parseInt(String.valueOf(j)));
        }

        @Override
        public void run() {
            try {
                InputStream inStr = socket.getInputStream();
                System.out.println("==============开始接收数据===============");
                while (true) {
                    byte[] b = new byte[118];
                    int r = inStr.read(b);
                    String device = new String(b, 2, 8, "UTF-8");
                    if (r > 58) {
                        Parameter parameter = new Parameter();
                        parameter.setDeviceId(device);
                        parameter.setWindSpeed(String.valueOf(change(b[36] , b[37]) * 1.0 / 10));
                        parameter.setWindDirection(String.valueOf(change(b[38] , b[39])));
                        parameter.setCoTwo(String.valueOf(change(b[40] , b[41])));
                        parameter.setTvoc(String.valueOf(change(b[42] , b[43]) * 1.0 / 10));
                        parameter.setHcho(String.valueOf(change(b[44] , b[45]) * 1.0 / 10));
                        parameter.setHumidity(String.valueOf(b[46]));
                        parameter.setTemperature(String.valueOf(change(b[47] , b[48])));
                        parameter.setIllumination(String.valueOf(change(b[49] , b[50])));
                        parameter.setpMOnePointZero(String.valueOf(change(b[51] , b[52])));
                        parameter.setpMTwoPointFive(String.valueOf(change(b[53] , b[54])));
                        parameter.setpMTen(String.valueOf(change(b[55] , b[56])));
                        parameter.setCreateTime(new java.util.Date());
                        System.out.println(parameter);
                        Connection con = DBUtil.getConnection();
                        String sql = "INSERT INTO parameter" +
                                "(temperature,humidity,HCHO,TVOC,CO_two,PM_two_point_five," +
                                " PM_one_point_zero, PM_ten, illumination,windSpeed,windDirection," +
                                "deviceId,createTime)" +
                                " values" +
                                " (?,?,?,?,?,?,?,?,?,?,?,?,?)";

                        PreparedStatement psmt = con.prepareStatement(sql);
                        psmt.setString(1, parameter.getTemperature());
                        psmt.setString(2, parameter.getHumidity());
                        psmt.setString(3, parameter.getHcho());
                        psmt.setString(4, parameter.getTvoc());
                        psmt.setString(5, parameter.getCoTwo());
                        psmt.setString(6, parameter.getpMTwoPointFive());
                        psmt.setString(7, parameter.getpMOnePointZero());
                        psmt.setString(8, parameter.getpMTen());
                        psmt.setString(9, parameter.getIllumination());
                        psmt.setString(10, parameter.getWindSpeed());
                        psmt.setString(11, parameter.getWindDirection());
                        psmt.setString(12, parameter.getDeviceId());
                        psmt.setTimestamp(13, new Timestamp(parameter.getCreateTime().getTime()));
                        //执行SQL语句
                        psmt.execute();
//                        System.out.println(parameterService.insert(parameter));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
