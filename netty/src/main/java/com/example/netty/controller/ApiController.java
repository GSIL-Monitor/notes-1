package com.example.netty.controller;

import com.example.netty.server.ChannelManagement;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.security.auth.login.AccountException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Resource
    private ChannelManagement channelManagement;

    @RequestMapping("/push/{count}")
    public Object pushMessage(@PathVariable("count") int count){
        send(count);
        return "success";
    }

    private void send(int count){
        channelManagement.sendMessage("10001","appTest",getTestMsg(count));
    }
    private void send2(int count){
        new Thread(()->{
            while (true){
                channelManagement.sendMessage("10001","appTest",getTestMsg(count));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Object getTestMsg(int n){
        Map<String,Object> msgMap=new HashMap<>();
        msgMap.put("productId","app001");
        msgMap.put("total",n);
        List<Map> msgList=new ArrayList<>();
        for (int i=0;i<n;i++){
            Map<String,String> map1=new HashMap<>();
            map1.put("deviceId","deivce00"+(i/5));
            map1.put("payload","this is message from device"+i);
            msgList.add(map1);
        }
        msgMap.put("list",msgList);
        return msgMap;
    }
}
