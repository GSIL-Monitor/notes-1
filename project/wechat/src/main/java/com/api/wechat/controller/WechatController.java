package com.api.wechat.controller;

/**
 * Author   NieYinjun
 * Date     2018/8/15 13:45
 * TAG
 */

import com.alibaba.fastjson.JSONObject;
import com.api.wechat.auth.AuthUtil;
import com.api.wechat.auth.SNSUserInfo;
import com.api.wechat.auth.WeixinOauth2Token;
import com.api.wechat.entity.message.MsgType;
import com.api.wechat.entity.message.WechatInputMessage;
import com.api.wechat.utils.WechatUtil;
import com.api.wechat.utils.WeixinUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Controller
@RequestMapping("/movie")
public class WechatController {
    private static final String TOKEN = "sochinlar";
    private static final String AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5eb87b2b6b2f1223" +
            "&redirect_uri=http%3A%2F%2F91f161fb.ngrok.io%2Fmovie%2Fauthcallback" +
            "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
//private static final String AUTH_URL="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5eb87b2b6b2f1223" +
//        "&redirect_uri=http%3A%2F%2F91f161fb.ngrok.io%2Fmovie%2Fauthcallback"+
    //       "%3Fd%3D%26c%3DwxAdapter%26m%3DmobileDeal%26showwxpaytitle%3D1%26vb2ctag%3D4_2030_5_1194_60&response_type=code&scope=snsapi_base&state=123#wechat_redirect";

    @RequestMapping(value = "/authcallback")
    public String authCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        System.out.println("====================================================");
        System.out.println(code);
        System.out.println(state);
        System.out.println("====================================================");
        WeixinOauth2Token oauth2Token = AuthUtil.getOauth2AccessToken("wx5eb87b2b6b2f1223", "95af98d4fdc623b3773a7c0455ddd2d6", code);
        System.out.println("=================oauth2Token===============================");
        System.out.println(oauth2Token);
        SNSUserInfo snsUserInfo = AuthUtil.getSNSUserInfo(oauth2Token.getAccessToken(), oauth2Token.getOpenId());
        System.out.println("========================snsUserInfo=================================");
        System.out.println(snsUserInfo);
   /*     WechatInputMessage inputMsg=new WechatInputMessage();
        inputMsg.setContent("授权成功！");
        inputMsg.setCreateTime(new Date().getTime()/1000);
        inputMsg.setToUserName(userInfo.getOpenId());
        WechatUtil.sendTextMsg(inputMsg,response);*/
        // 设置要传递的参数
        request.setAttribute("snsUserInfo", snsUserInfo);
        request.setAttribute("state", state);
        // 跳转到index.jsp
        return "success";

    }

    @RequestMapping(value = "/getSpecMovie", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void getSpecMovie(HttpServletRequest request, HttpServletResponse response) throws Exception {
        WechatInputMessage inputMsg = WechatUtil.getInputMessage(request, response, TOKEN);
        if (inputMsg == null) return;
        // 取得消息类型
        String msgType = inputMsg.getMsgType();
        // 根据消息类型获取对应的消息内容
        if (msgType.equals(MsgType.Text.toString())) {
            //客户端发送的信息
            String content = inputMsg.getContent();
            //解析信息，获取要回复的内容。
            //。。。。补全
            inputMsg.setContent("这是公众号发送给用户的信息。");

            WechatUtil.sendTextMsg(inputMsg, response);
        } else if (msgType.equals(MsgType.Image.toString())) {
            WechatUtil.sendImageMsg(inputMsg, response);
        } else if (msgType.equals(MsgType.Event.toString())) {
            if (inputMsg.getEvent().equals("subscribe")) {
                System.out.println(inputMsg.getFromUserName() + "关注了公众号！");
                inputMsg.setContent("欢迎关注公众号!");
                WechatUtil.sendTextMsg(inputMsg, response);
            } else if (inputMsg.getEvent().equals("unsubscribe")) {
                System.out.println(inputMsg.getFromUserName() + "取消了关注公众号！");
                WechatUtil.sendTextMsg(inputMsg, response);
            }
        }

    }
}