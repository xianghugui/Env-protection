package com.environment.program;

import com.environment.program.bean.Parameter;
import com.environment.program.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Service
public class Test implements ApplicationListener<ContextRefreshedEvent> {
    private static final int PORT = 8888;

    @Autowired
    private ParameterService parameterService;

    /**
     * spring启动后执行
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Connect(parameterService);
    }

    /**
     * 开启Socket
     */
    private static class Connect implements Runnable {

        private ParameterService parameterService;

        public Connect(ParameterService parameterService) {
            this.parameterService = parameterService;
            new Thread(this).start();
        }

        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                // 一旦有堵塞, 则表示服务器与客户端获得了连接
                while (true) {
                    Socket client = serverSocket.accept();
                    System.out.println("------已连接------");
                    new KeepThread(client);
                    new RecvThread(client, parameterService);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        /**
         * 接受消息
         */
        static class RecvThread implements Runnable {

            private Socket socket;

            private ParameterService parameterService;

            public RecvThread(Socket client, ParameterService parameterService) {
                socket = client;
                this.parameterService = parameterService;
                new Thread(this).start();
            }

            private int change(byte i, byte j) {
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
                        System.out.println(new String(b, "UTF-8"));
                        String device = new String(b, 2, 8, "UTF-8");
                        if (r > 58) {
                            Parameter parameter = new Parameter();
                            parameter.setDeviceId(device);
                            parameter.setWindSpeed(String.valueOf(change(b[36], b[37]) * 1.0 / 10));
                            parameter.setWindDirection(String.valueOf(change(b[38], b[39])));
                            parameter.setCoTwo(String.valueOf(change(b[40], b[41])));
                            parameter.setTvoc(String.valueOf(change(b[42], b[43]) * 1.0 / 10));
                            parameter.setHcho(String.valueOf(change(b[44], b[45]) * 1.0 / 10));
                            parameter.setHumidity(String.valueOf(b[46]));
                            parameter.setTemperature(String.valueOf(change(b[47], b[48])));
                            parameter.setIllumination(String.valueOf(change(b[49], b[50])));
                            parameter.setpMOnePointZero(String.valueOf(change(b[51], b[52])));
                            parameter.setpMTwoPointFive(String.valueOf(change(b[53], b[54])));
                            parameter.setpMTen(String.valueOf(change(b[55], b[56])));
                            parameter.setCreateTime(new java.util.Date());
                            parameterService.insert(parameter);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        /**
         * 10秒发一次心跳包
         */
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

    }


}
