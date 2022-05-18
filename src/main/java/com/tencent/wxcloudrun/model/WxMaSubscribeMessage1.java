package com.tencent.wxcloudrun.model;

import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxMaSubscribeMessage1 implements Serializable {
    private static final long serialVersionUID = 6846729898251286686L;

    /**
     * 接收者（用户）的 openid.
     * <pre>
     * 参数：touser
     * 是否必填： 是
     * 描述： 接收者（用户）的 openid
     * </pre>
     */
    private String touser;

    /**
     * 所需下发的模板消息的id.
     * <pre>
     * 参数：template_id
     * 是否必填： 是
     * 描述： 所需下发的模板消息的id
     * </pre>
     */
    private String template_id;

    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面.
     * <pre>
     * 参数：page
     * 是否必填： 否
     * 描述： 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
     * </pre>
     */
    private String page;

    /**
     * 模板内容，不填则下发空模板.
     * <pre>
     * 参数：data
     * 是否必填： 是
     * 描述： 模板内容，不填则下发空模板
     * </pre>
     */
    private Map<String,MsgData> data;

    /**
     * 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
     */
    private String miniprogramState = "formal";

    /**
     * 进入小程序查看的语言类型，支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN
     */
    private String lang = "zh_CN";

    public WxMaSubscribeMessage1 addData(String key, MsgData datum) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }

        this.data.put(key,datum);

        return this;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MsgData implements Serializable {
        private static final long serialVersionUID = 1L;

        private String value;
    }

}
