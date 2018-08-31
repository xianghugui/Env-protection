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
import java.net.SocketException;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Test implements ApplicationListener<ContextRefreshedEvent> {

    private static final int PORT = 8888;

    @Autowired
    private ParameterService parameterService;

    /**
     * 匹配设备ID，格式为ABC-1234
     */
    private static Pattern DEVICE_PATTERN = Pattern.compile("[a-zA-Z]{3}[-]{1}\\d{4}");

    /**
     * 心跳应答包
     */
    private static byte[] RESPONSE_PACKAGE = {(byte) 0x55, (byte) 0xAA, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00};

    /**
     * 线程池
     */
    private static ExecutorService THREAD_POOL = new ThreadPoolExecutor(1, 100,
            10, TimeUnit.MINUTES, new SynchronousQueue(),
            new ThreadFactory() {

                private final AtomicInteger mCount = new AtomicInteger(1);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "Socket_pool:" + mCount.getAndIncrement());
                }
            });


    /**
     * spring启动后执行
     *
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        THREAD_POOL.execute(new Connect());
    }

    /**
     * 开启Socket
     */
    private class Connect implements Runnable {
        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                // 一旦有堵塞, 则表示服务器与客户端获得了连接
                while (true) {
                    Socket client = serverSocket.accept();
                    System.out.println("------已连接------");
                    THREAD_POOL.execute(new RecvThread(client));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 接受消息
     */
    private class RecvThread implements Runnable {

        private Socket socket;

        public RecvThread(Socket client) {
            socket = client;
        }

        /**
         * 合并高低八位
         *
         * @param i
         * @param j
         * @return
         */
        private int change(byte i, byte j) {
            return (i & 0xFF) * 256 + (j & 0xFF);
        }

        /**
         * 将结果除以10
         *
         * @param value
         * @return
         */
        private String result(int value) {
            if (value == 0) {
                return String.valueOf(value);
            } else {
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
                        Matcher m = DEVICE_PATTERN.matcher(new String(b, "UTF-8"));
                        if (m.find()) {
                            Parameter parameter = new Parameter();
                            parameter.setDeviceId(new String(b, m.start(), 8, "UTF-8"));
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
            } catch (SocketException e) {
                System.out.println("SocketException:" + e.getMessage());
            } catch (IOException e){
                e.printStackTrace();
            }

        }
    }

}
