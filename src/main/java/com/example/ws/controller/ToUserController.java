package com.example.ws.controller;
import com.example.ws.core.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("to")
public class ToUserController {
    @Autowired
    private SessionManager sessionManager;
    @RequestMapping("user")
    public String toUser(@RequestParam("userId") Integer userId, @RequestParam("msg") String msg){
         sessionManager.getUserSessions(userId).forEach(session ->
                 session.sendEvent("message", msg));
        return "success";
    }
}
