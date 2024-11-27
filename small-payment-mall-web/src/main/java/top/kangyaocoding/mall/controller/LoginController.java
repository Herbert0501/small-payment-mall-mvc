package top.kangyaocoding.mall.controller;

import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.kangyaocoding.mall.ILoginService;
import top.kangyaocoding.mall.constants.Constants;
import top.kangyaocoding.mall.response.Response;

import javax.annotation.Resource;

/**
 * 描述: 登录
 *
 * @author K·Herbert
 * @since 2024-11-28 上午12:11
 */
@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/login/")
public class LoginController {

    @Resource
    private ILoginService loginService;

    @GetMapping(value = "weixin_qrcode_ticket")
    public Response<String> weixinQrCodeTicket() {
        try {
            String qrCodeTicket = loginService.createQrCodeTicket();
            log.info("创建微信二维码ticket为：{}", qrCodeTicket);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(qrCodeTicket)
                    .build();
        } catch (Exception e) {
            log.error("创建微信二维码ticket异常", e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @GetMapping(value = "check_login")
    public Response<String> checkLogin(@RequestParam String ticket) {
        try {
            String openidToken = loginService.checkLogin(ticket);
            log.info("扫码登录结果为 ticket：{}，openidToken：{}", ticket, openidToken);
            if (StringUtil.isNotBlank(openidToken)) {
                return Response.<String>builder()
                        .code(Constants.ResponseCode.SUCCESS.getCode())
                        .info(Constants.ResponseCode.SUCCESS.getInfo())
                        .data(openidToken)
                        .build();
            }
            return Response.<String>builder()
                    .code(Constants.ResponseCode.NO_LOGIN.getCode())
                    .info(Constants.ResponseCode.NO_LOGIN.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("扫码登录检测异常 ticket：{}", ticket, e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
