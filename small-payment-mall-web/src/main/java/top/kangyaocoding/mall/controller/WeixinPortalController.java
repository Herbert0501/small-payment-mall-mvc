package top.kangyaocoding.mall.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.kangyaocoding.mall.ILoginService;
import top.kangyaocoding.mall.weixin.MessageTextEntity;
import top.kangyaocoding.mall.weixin.SignatureUtil;
import top.kangyaocoding.mall.weixin.XmlUtil;

import javax.annotation.Resource;

/**
 * 描述: 微信公众号接入
 *
 * @author K·Herbert
 * @since 2024-11-27 下午10:14
 */
@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/weixin/portal/")
public class WeixinPortalController {

    @Value("${weixin.config.originalid}")
    private String originalid;

    @Value("${weixin.config.token}")
    private String token;

    @Resource
    private ILoginService loginService;

    @GetMapping(value = "receive", produces = "text/plain;charset=utf-8")
    public String validate(@RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce,
                           @RequestParam(value = "echostr", required = false) String echostr) {

        try {
            log.info("微信公众号验签信息开始 [{}, {}, {}, {}]", signature, timestamp, nonce, echostr);

            if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
                throw new IllegalArgumentException("请求参数非法！");
            }
            boolean check = SignatureUtil.checkSignature(token, signature, timestamp, nonce);
            log.info("微信公众号验签结果 check 为：{}", check);
            if (!check) return null;
            return echostr;
        } catch (IllegalArgumentException e) {
            log.error("微信公众号验签参数异常[{}，{}，{}，{}]", signature, timestamp, nonce, echostr, e);
            return null;
        }
    }

    @PostMapping(value = "receive", produces = "application/xml;charset=utf-8")
    public String post(@RequestBody String requestBody,
                       @RequestParam(value = "signature", required = false) String signature,
                       @RequestParam(value = "timestamp", required = false) String timestamp,
                       @RequestParam(value = "nonce", required = false) String nonce,
                       @RequestParam(value = "openid", required = false) String openid,
                       @RequestParam(value = "encrypt_type", required = false) String encType,
                       @RequestParam(value = "msg_signature", required = false) String msgSign
    ) {
        try {
            log.info("微信公众号接收信息开始 [请求体：{}, openid：{}]", requestBody, openid);
            // 消息转化
            MessageTextEntity messageText = XmlUtil.xmlToBean(requestBody, MessageTextEntity.class);
            if ("event".equals(messageText.getMsgType()) && "SCAN".equals(messageText.getEvent())) {
                loginService.saveLoginState(messageText.getTicket(), openid);
                return buildMessageTextEntity(openid, "欢迎关注！");
            }

            return buildMessageTextEntity(openid, String.valueOf(messageText.getContent()));
        } catch (Exception e) {
            log.error("微信公众号接收信息异常 [请求体：{}, openid：{}]", requestBody, openid, e);
            return null;
        }
    }

    private String buildMessageTextEntity(String openid, String content) throws Exception {
        MessageTextEntity responseMessage = new MessageTextEntity();
        responseMessage.setFromUserName(originalid);
        responseMessage.setToUserName(openid);
        responseMessage.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
        responseMessage.setMsgType("text");
        responseMessage.setContent(content);
        return XmlUtil.beanToXml(responseMessage);
    }
}
