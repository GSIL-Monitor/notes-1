package com.api.wechat.controller;

import com.api.wechat.entity.menu.*;
import com.api.wechat.utils.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.plugin2.main.server.AppletID;

/**
 * Author   NieYinjun
 * Date     2018/8/16 16:53
 * TAG      菜单修改测试使用
 */
public class MenuController {
    private static Logger log = LoggerFactory.getLogger(MenuController.class);

    public static void main(String[] args) {
        // 第三方用户唯一凭证
        // String appId = "wx7ffd957a6497616c";
        String appId = "wx5eb87b2b6b2f1223";
        // 第三方用户唯一凭证密钥
        //String appSecret = "fc6d4aa19d95b52f8f1dd536cb71c257";
        String appSecret = "95af98d4fdc623b3773a7c0455ddd2d6";
        // 调用接口获取access_token
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

        if (null != at) {
            // 调用接口创建菜单
            int result = WeixinUtil.createMenu(getMenu(), at.getToken());

            // 判断菜单创建结果
            if (0 == result)
                log.info("菜单创建成功！");
            else
                log.info("菜单创建失败，错误码：" + result);
        }
    }

    /**
     * 组装菜单数据
     *
     * @return
     */
    private static Menu getMenu() {
        CommonButton btn11 = new CommonButton();
        btn11.setName("天气预报");
        btn11.setType("click");
        btn11.setKey("11");

        CommonButton btn12 = new CommonButton();
        btn12.setName("公交查询");
        btn12.setType("click");
        btn12.setKey("12");

        CommonButton btn13 = new CommonButton();
        btn13.setName("周边搜索");
        btn13.setType("click");
        btn13.setKey("13");

        CommonButton btn14 = new CommonButton();
        btn14.setName("历史上的今天");
        btn14.setType("click");
        btn14.setKey("14");

        CommonButton btn21 = new CommonButton();
        btn21.setName("歌曲点播");
        btn21.setType("click");
        btn21.setKey("21");

        CommonButton btn22 = new CommonButton();
        btn22.setName("经典游戏");
        btn22.setType("click");
        btn22.setKey("22");

        CommonButton btn23 = new CommonButton();
        btn23.setName("美女电台");
        btn23.setType("click");
        btn23.setKey("23");

        CommonButton btn24 = new CommonButton();
        btn24.setName("人脸识别");
        btn24.setType("click");
        btn24.setKey("24");

        CommonButton btn25 = new CommonButton();
        btn25.setName("聊天唠嗑");
        btn25.setType("click");
        btn25.setKey("25");


        /**
         * 微信：  mainBtn1,mainBtn2,mainBtn3底部的三个一级菜单。
         */

        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName("生活助手");
        //一级下有4个子菜单
        mainBtn1.setSub_button(new CommonButton[]{btn11, btn12, btn13, btn14});


        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName("休闲驿站");
        mainBtn2.setSub_button(new CommonButton[]{btn21, btn22, btn23, btn24, btn25});


        CommonButton mainBtn3 = new CommonButton();
        mainBtn3.setName("授权测试");
        mainBtn3.setType("view");
        mainBtn3.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5eb87b2b6b2f1223" +
                "&redirect_uri=http%3A%2F%2F52bf2ce9.ngrok.io%2Fmovie%2Fauthcallback" +
                "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");


        /**
         * 封装整个菜单
         */
        Menu menu = new Menu();
        menu.setButton(new Button[]{mainBtn1, mainBtn2, mainBtn3});

        return menu;
    }

}
