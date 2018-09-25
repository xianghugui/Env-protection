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
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SocketPool implements ApplicationListener<ContextRefreshedEvent> {

    private static final int PORT = 8888;

    @Autowired
    private ParameterService parameterService;

    /**
     * 匹配设备ID，格式为ABC-1234
     */
    private static final Pattern DEVICE_PATTERN = Pattern.compile("[a-zA-Z]{3}[-]{1}\\d{4}");

    /**
     * 心跳应答包
     */
    private static final byte[] RESPONSE_PACKAGE = {(byte) 0x55, (byte) 0xAA, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00};

    /**
     * 线程池
     */
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(1, 100,
            0, TimeUnit.MILLISECONDS, new SynchronousQueue(),
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
            InputStream inStr = null;
            OutputStream outStr = null;
            int length;
            byte[] b = new byte[118];
            Matcher matcher;
            Parameter parameter = new Parameter();
            try {
                inStr = socket.getInputStream();
                outStr = socket.getOutputStream();
                System.out.println("==============开始接收数据===============");
                while ((length = inStr.read(b)) > 0) {
                    if (length == 59) {
                        matcher = DEVICE_PATTERN.matcher(new String(b, 0, length, StandardCharsets.UTF_8));
                        if (matcher.find()) {
                            parameter.setDeviceId(new String(b, matcher.start(), 8, StandardCharsets.UTF_8));
                            parameter.setWindSpeed(result(change(b[36], b[37])));
                            parameter.setWindDirection(String.valueOf(change(b[38], b[39])));
                            parameter.setCoTwo(String.valueOf(change(b[40], b[41]) / 10000));
                            parameter.setTvoc(String.valueOf(change(b[42], b[43]) / 1000 * 833));
                            parameter.setHcho(String.valueOf(change(b[44], b[45]) /1000 * 833));
                            parameter.setHumidity(String.valueOf(b[46] & 0xFF));
                            parameter.setTemperature(result(change(b[47], b[48])));
                            parameter.setIllumination(String.valueOf(change(b[49], b[50])));
                            parameter.setpMOnePointZero(String.valueOf(change(b[51], b[52])));
                            parameter.setpMTwoPointFive(String.valueOf(change(b[53], b[54])));
                            parameter.setpMTen(String.valueOf(change(b[55], b[56])));
                            parameter.setCreateTime(new Date());
                            parameterService.insert(parameter);
                        }
                    } else if(length == 12) {
                        matcher = DEVICE_PATTERN.matcher(new String(b, 0, length, StandardCharsets.UTF_8));
                        if (matcher.find()) {
                            outStr.write(RESPONSE_PACKAGE);
                            outStr.flush();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inStr != null) {
                    try {
                        inStr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outStr != null) {
                    try {
                        outStr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

}
