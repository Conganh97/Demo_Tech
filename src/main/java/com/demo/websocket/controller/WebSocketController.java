package com.demo.websocket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/send/{message}")
    public String sendMessage(@PathVariable String message) {
        messagingTemplate.convertAndSend("/topic/greetings", "{\"name\":\"" + message + "\"}");
        return "Message sent";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String receiveMessage(String message) {
        log.info("Receive message: {}", message);
        return message;
    }

}
