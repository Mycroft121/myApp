package com.tencent.wxcloudrun.controller;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CounterRequest;
import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.model.OpenId;
import com.tencent.wxcloudrun.model.WxMaSubscribeMessage;
import com.tencent.wxcloudrun.service.CounterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * counter控制器
 */
@RestController

public class CounterController {

    String token = "56_z29Ut4WhqXxQtdFV8NXQOxT3Iz85v-WshPuuT3fixpXYhbm9Z73LL0nre0MtRGvwfiHFKl1InhvHQ3aQJQnXR8tsiOB6NeKquPZz2lxjZXBwZQd7ltv4DXo9qe19n55D6Ks3vlwdqIUPX2o5ISKaAGAEOG";

    final CounterService counterService;
    final Logger logger;

    public CounterController(@Autowired CounterService counterService) {
        this.counterService = counterService;
        this.logger = LoggerFactory.getLogger(CounterController.class);
    }

    /**
     * 获取当前计数
     *
     * @return API response json
     */
    @GetMapping(value = "/getAccessToken")
    ApiResponse getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx9d7ec299fee2d23b&secret=1eee794a8910f5d263b24647fb78c1b9";

        String res = HttpRequest.get(url)
                .execute().body();
        logger.info("/getAccessToken response: {}", res);
        return ApiResponse.ok(res);
    }

    @GetMapping(value = "/getPluginOpenPId")
    ApiResponse getPluginOpenPId(String code) {
        String url = "https://api.weixin.qq.com/wxa/getpluginopenpid?access_token=" + token;
        OpenId openId = OpenId.builder().code(code).build();
        String res = HttpRequest.post(url)
                .body(JSONUtil.toJsonStr(openId))
                .execute().body();
        logger.info("/getPluginOpenPId response: {}", res);
        return ApiResponse.ok(res);
    }

    /**
     * 获取当前计数
     *
     * @return API response json
     */
    @GetMapping(value = "/send")
    ApiResponse getOpenId(HttpServletRequest request) {
        //获取请求头信息
        Enumeration headerNames = request.getHeaderNames();
        //使用循环遍历请求头，并通过getHeader()方法获取一个指定名称的头字段
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            System.out.println(headerName + " : " + request.getHeader(headerName) + "<br/>");
        }
        String openId = request.getHeader("x-wx-openid");
//        String openId = "ociwi0ZL_j3_z8CxvrElJkicoeGg";
        List<WxMaSubscribeMessage.MsgData> msgDataList = new ArrayList<>();
        msgDataList.add(new WxMaSubscribeMessage.MsgData("phrase1", "value1"));
        msgDataList.add(new WxMaSubscribeMessage.MsgData("thing2", "value1"));
        WxMaSubscribeMessage build = WxMaSubscribeMessage.builder()
                .toUser(openId)
                .templateId("vKzxbGQYqEQdIfi9Kjzf6FEDqUbKgVkLxMe2VVRQdz0")
                .data(msgDataList)
                .build();
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send";
//        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token="+token;
        String res = HttpRequest.post(url)
                .header(Header.USER_AGENT, "Hutool http")//头信息，多个头信息多次调用此方法即可
                .body(JSONUtil.toJsonStr(build))//表单内容
                .execute().body();
        logger.info("/getOpenId response: {}", res);
        return ApiResponse.ok(res);
    }


    /**
     * 获取当前计数
     *
     * @return API response json
     */
    @GetMapping(value = "/api/index")
    ApiResponse index() {
        logger.info("/api/index get request");
        Optional<Counter> counter = counterService.getCounter(1);
        return ApiResponse.ok("hello world");
    }


    /**
     * 获取当前计数
     *
     * @return API response json
     */
    @GetMapping(value = "/api/count")
    ApiResponse get() {
        logger.info("/api/count get request");
        Optional<Counter> counter = counterService.getCounter(1);
        Integer count = 0;
        if (counter.isPresent()) {
            count = counter.get().getCount();
        }

        return ApiResponse.ok(count);
    }


    /**
     * 更新计数，自增或者清零
     *
     * @param request {@link CounterRequest}
     * @return API response json
     */
    @PostMapping(value = "/api/count")
    ApiResponse create(@RequestBody CounterRequest request) {
        logger.info("/api/count post request, action: {}", request.getAction());

        Optional<Counter> curCounter = counterService.getCounter(1);
        if (request.getAction().equals("inc")) {
            Integer count = 1;
            if (curCounter.isPresent()) {
                count += curCounter.get().getCount();
            }
            Counter counter = new Counter();
            counter.setId(1);
            counter.setCount(count);
            counterService.upsertCount(counter);
            return ApiResponse.ok(count);
        } else if (request.getAction().equals("clear")) {
            if (!curCounter.isPresent()) {
                return ApiResponse.ok(0);
            }
            counterService.clearCount(1);
            return ApiResponse.ok(0);
        } else {
            return ApiResponse.error("参数action错误");
        }
    }

}