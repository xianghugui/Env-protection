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
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Test implements ApplicationListener<ContextRefreshedEvent> {

    private static final int PORT = 8888;

    /**
     * 匹配设备ID，格式为ABC-1234
     */
    private static Pattern DEVICE_PATTERN = Pattern.compile("[a-zA-Z]{3}[-]{1}\\d{4}");

    /**
     * 心跳应答包
     */
    private static byte[] RESPONSE_PACKAGE = {(byte)0x55, (byte)0xAA , (byte)0x08, (byte)0x00, (byte)0x00, (byte)0x00};

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

            /**
             * 合并高低八位
             * @param i
             * @param j
             * @return
             */
            private int change(byte i, byte j) {
                return (i & 0xFF) * 256 + (j & 0xFF);
            }

            /**
             * 将结果除以10
             * @param value
             * @return
             */
            private String result(int value){
                if(value == 0){
                    return String.valueOf(value);
                }else{
                    return String.valueOf(value * 1.0 / 10);
                }
            }


            @Override
            public void run() {
                try {
                    InputStream inStr = socket.getInputStream();
                    OutputStream outStr = socket.getOutputStream();
                    System.out.println("==============开始接收数据===============");
                    while (true) {
                        byte[] b = new byte[118];
                        int r = inStr.read(b);
                        if (r == 12) {
                            outStr.write(RESPONSE_PACKAGE);
                            outStr.flush();
                        } else if (r == 59) {
                            Matcher m = DEVICE_PATTERN.matcher(new String(b));
                            if (m.find()) {
//                                System.out.println("设备:" + new String(b, m.start(), 8) + "  " + new Date());
//                                System.out.println("风速:" + String.valueOf(change(b[36], b[37]) * 1.0 / 10) + "，原始数据:" + (b[36] & 0xFF) + "  " + (b[37] & 0xFF));
//                                System.out.println("风向:" + String.valueOf(change(b[38], b[39])) + "，原始数据:" + (b[38] & 0xFF) + "  " + (b[39] & 0xFF));
//                                System.out.println("CO2:" + String.valueOf(change(b[40], b[41])) + "，原始数据:" + (b[40] & 0xFF) + "  " + (b[41] & 0xFF));
//                                System.out.println("TVOC:" + String.valueOf(change(b[42], b[43]) * 1.0 / 10) + "，原始数据:" + (b[42] & 0xFF) + "  " + (b[43] & 0xFF));
//                                System.out.println("甲醛:" + String.valueOf(change(b[44], b[45]) * 1.0 / 10) + "，原始数据:" + (b[44] & 0xFF) + "  " + (b[45] & 0xFF));
//                                System.out.println("湿度:" + String.valueOf(b[46] & 0xFF) + "，原始数据:" + (b[46] & 0xFF));
//                                System.out.println("温度:" + String.valueOf(change(b[47], b[48]) * 1.0 / 10) + "，原始数据:" + (b[47] & 0xFF) + "  " + (b[48] & 0xFF));
//                                System.out.println("光照:" + String.valueOf(change(b[49], b[50])) + "，原始数据:" + (b[49] & 0xFF) + "  " + (b[50] & 0xFF));
//                                System.out.println("PM1.0:" + String.valueOf(change(b[51], b[52])) + "，原始数据:" + (b[51] & 0xFF) + "  " + (b[52] & 0xFF));
//                                System.out.println("PM2.5:" + String.valueOf(change(b[53], b[54])) + "，原始数据:" + (b[53] & 0xFF) + "  " + (b[54] & 0xFF));
//                                System.out.println("PM10:" + String.valueOf(change(b[55], b[56])) + "，原始数据:" + (b[55] & 0xFF) + "  " + (b[56] & 0xFF));
                                Parameter parameter = new Parameter();
                                parameter.setDeviceId(new String(b, m.start(), 8));
                                parameter.setWindSpeed(result(change(b[36], b[37])));
                                parameter.setWindDirection(String.valueOf(change(b[38], b[39])));
                                parameter.setCoTwo(String.valueOf(change(b[40], b[41])));
                                parameter.setTvoc(result(change(b[42], b[43])));
                                parameter.setHcho(result(change(b[44], b[45])));
                                parameter.setHumidity(String.valueOf(b[46] & 0xFF));
                                parameter.setTemperature(result(change(b[47], b[48])));
                                parameter.setIllumination(String.valueOf(change(b[49], b[50])));
                                parameter.setpMOnePointZero(String.valueOf(change(b[51], b[52])));
                                parameter.setpMTwoPointFive(String.valueOf(change(b[53], b[54])));
                                parameter.setpMTen(String.valueOf(change(b[55], b[56])));
                                parameter.setCreateTime(new Date());
                                parameterService.insert(parameter);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

}
