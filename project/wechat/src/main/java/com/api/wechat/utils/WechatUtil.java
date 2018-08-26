package com.api.wechat.utils;

import com.api.wechat.entity.message.ImageMessage;
import com.api.wechat.entity.message.MsgType;
import com.api.wechat.entity.message.WechatInputMessage;
import com.api.wechat.entity.message.WechatOutputMessage;
import com.thoughtworks.xstream.XStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Author   NieYinjun
 * Date     2018/8/15 20:07
 * TAG
 */
public class WechatUtil {
    public static WechatInputMessage getInputMessage(HttpServletRequest request, HttpServletResponse response, String token) {
        response.setContentType("text/html;charset=utf-8");
        System.out.println("进入方法");
        Boolean isGet = request.getMethod().toLowerCase().equals("get");
        if (isGet) {
            access(request, response, token);
        } else {
            try {
                return acceptMessage(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void sendTextMsg(WechatInputMessage inputMsg, HttpServletResponse response) {
        String servername = inputMsg.getToUserName();// 服务端
        String custermname = inputMsg.getFromUserName();// 客户端
        long createTime = inputMsg.getCreateTime();// 接收时间
        Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;// 返回时间
        System.out.println("开发者微信号：" + inputMsg.getToUserName());
        System.out.println("发送方帐号：" + inputMsg.getFromUserName());
        System.out.println("消息创建时间：" + inputMsg.getCreateTime() + new Date(createTime * 1000l));
        System.out.println("消息内容：" + inputMsg.getContent());
        System.out.println("消息Id：" + inputMsg.getMsgId());
        StringBuffer str = new StringBuffer();
        str.append("<xml>");
        str.append("<ToUserName><![CDATA[" + custermname + "]]></ToUserName>");
        str.append("<FromUserName><![CDATA[" + servername + "]]></FromUserName>");
        str.append("<CreateTime>" + returnTime + "</CreateTime>");
        str.append("<MsgType><![CDATA[" + MsgType.Text.toString() + "]]></MsgType>");

        str.append("<Content><![CDATA[" + inputMsg.getContent() + "]]></Content>");
        str.append("</xml>");
        System.out.println(str.toString());
        try {
            response.getWriter().write(str.toString());
        } catch (IOException ie) {
            System.out.println("获取输出流异常");
            ie.printStackTrace();
        }

    }

    public static void sendImageMsg(WechatInputMessage inputMsg, HttpServletResponse response) {
        String servername = inputMsg.getToUserName();// 服务端
        String custermname = inputMsg.getFromUserName();// 客户端
        long createTime = inputMsg.getCreateTime();// 接收时间
        Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;// 返回时间

        System.out.println("获取多媒体信息");
        System.out.println("多媒体文件id：" + inputMsg.getMediaId());
        System.out.println("图片链接：" + inputMsg.getPicUrl());
        System.out.println("消息id，64位整型：" + inputMsg.getMsgId());

        WechatOutputMessage outputMsg = new WechatOutputMessage();
        outputMsg.setFromUserName(servername);
        outputMsg.setToUserName(custermname);
        outputMsg.setCreateTime(returnTime);
        outputMsg.setMsgType(inputMsg.getMsgType());
        ImageMessage images = new ImageMessage();
        images.setMediaId(inputMsg.getMediaId());
        outputMsg.setImage(images);
        XStream xs = getXStream();
        System.out.println("xml转换：/n" + xs.toXML(outputMsg));
        try {
            response.getWriter().write(xs.toXML(outputMsg));
        } catch (IOException ioe) {
            System.out.println("获取输出流失败！");
            ioe.printStackTrace();
        }
    }

    private static XStream getXStream() {
        XStream xs = SerializeXmlUtil.createXstream();
        xs.processAnnotations(WechatInputMessage.class);
        xs.processAnnotations(WechatOutputMessage.class);
        // 将指定节点下的xml节点数据映射为对象
        xs.alias("xml", WechatInputMessage.class);
        return xs;
    }

    /**
     * 验证url真实性
     *
     * @param request
     * @param response
     * @return
     */
    public static String access(HttpServletRequest request, HttpServletResponse response, String token) {
        // 验证URL真实性
        System.out.println("进入验证access");
        String signature = request.getParameter("signature");// 微信加密签名
        String timestamp = request.getParameter("timestamp");// 时间戳
        String nonce = request.getParameter("nonce");// 随机数
        String echostr = request.getParameter("echostr");// 随机字符串
        System.out.println("signature:" + signature);
        System.out.println("timestamp:" + timestamp);
        System.out.println("nonce:" + nonce);
        System.out.println("echostr:" + echostr);
        List<String> params = new ArrayList<String>();
        params.add(token);
        params.add(timestamp);
        params.add(nonce);
        // 1. 将token、timestamp、nonce三个参数进行字典序排序
        Collections.sort(params, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密
        String temp = SHA1.encode(params.get(0) + params.get(1) + params.get(2));
        System.out.println("temp:" + temp);
        if (temp.equals(signature)) {
            try {
                response.getWriter().write(echostr);
                System.out.println("成功返回 echostr：" + echostr);
                return echostr;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("失败 认证");
        return null;
    }

    private static WechatInputMessage acceptMessage(HttpServletRequest request) throws IOException {
        // 进入POST聊天处理
        System.out.println("enter post");
        // 处理接收消息
        ServletInputStream in = request.getInputStream();
        // 将POST流转换为XStream对象
        XStream xs = getXStream();
        // 将流转换为字符串
        StringBuilder xmlMsg = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            xmlMsg.append(new String(b, 0, n, "UTF-8"));
        }
        // 将xml内容转换为InputMessage对象
        System.out.println(xmlMsg.toString());
        WechatInputMessage inputMsg = (WechatInputMessage) xs.fromXML(xmlMsg.toString());

   /*     String servername = inputMsg.getToUserName();// 服务端
        String custermname = inputMsg.getFromUserName();// 客户端
        long createTime = inputMsg.getCreateTime();// 接收时间
        Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;// 返回时间

        // 取得消息类型
        String msgType = inputMsg.getMsgType();
        // 根据消息类型获取对应的消息内容
        if (msgType.equals(MsgType.Text.toString())) {
            // 文本消息
            System.out.println("开发者微信号：" + inputMsg.getToUserName());
            System.out.println("发送方帐号：" + inputMsg.getFromUserName());
            System.out.println("消息创建时间：" + inputMsg.getCreateTime() + new Date(createTime * 1000l));
            System.out.println("消息内容：" + inputMsg.getContent());
            System.out.println("消息Id：" + inputMsg.getMsgId());

            StringBuffer str = new StringBuffer();
            str.append("<xml>");
            str.append("<ToUserName><![CDATA[" + custermname + "]]></ToUserName>");
            str.append("<FromUserName><![CDATA[" + servername + "]]></FromUserName>");
            str.append("<CreateTime>" + returnTime + "</CreateTime>");
            str.append("<MsgType><![CDATA[" + msgType + "]]></MsgType>");

            str.append("<Content><![CDATA[" + responseMsg + "]]></Content>");
            str.append("</xml>");
            System.out.println(str.toString());
            response.getWriter().write(str.toString());
        }
        // 获取并返回多图片消息
        if (msgType.equals(MsgType.Image.toString())) {
            System.out.println("获取多媒体信息");
            System.out.println("多媒体文件id：" + inputMsg.getMediaId());
            System.out.println("图片链接：" + inputMsg.getPicUrl());
            System.out.println("消息id，64位整型：" + inputMsg.getMsgId());

            WechatOutputMessage outputMsg = new WechatOutputMessage();
            outputMsg.setFromUserName(servername);
            outputMsg.setToUserName(custermname);
            outputMsg.setCreateTime(returnTime);
            outputMsg.setMsgType(msgType);
            ImageMessage images = new ImageMessage();
            images.setMediaId(inputMsg.getMediaId());
            outputMsg.setImage(images);
            System.out.println("xml转换：/n" + xs.toXML(outputMsg));
            response.getWriter().write(xs.toXML(outputMsg));
        }
*/
        return inputMsg;
    }
}
