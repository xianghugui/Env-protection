package com.environment.program.controller;

import com.environment.program.bean.ClientMassageParameter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ClientSocketMassageController {
    /**
     * 用户主动请求<------->服务器响应用户请求
     * @param chatMessage
     * @return
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ClientMassageParameter sendMessage(@Payload ClientMassageParameter chatMessage) {
        System.out.println(chatMessage.getContent());
        //解析客户端发送给服务端的数据

        //服务端再返回响应结果
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ClientMassageParameter addUser(@Payload ClientMassageParameter chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}
