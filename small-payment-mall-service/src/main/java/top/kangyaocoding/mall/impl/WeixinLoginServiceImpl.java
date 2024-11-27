package top.kangyaocoding.mall.impl;

import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import top.kangyaocoding.mall.ILoginService;
import top.kangyaocoding.mall.req.WeixinQrCodeReq;
import top.kangyaocoding.mall.res.WeixinQrCodeRes;
import top.kangyaocoding.mall.res.WeixinTokenRes;
import top.kangyaocoding.mall.vo.WeixinTemplateMessageVO;
import top.kangyaocoding.mall.weixin.IWeixinApiService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述: 微信登录服务实现
 *
 * @author K·Herbert
 * @since 2024-11-28 上午12:31
 */
@Slf4j
@Service
public class WeixinLoginServiceImpl implements ILoginService {

    @Value("${weixin.config.app-id}")
    private String appid;
    @Value("${weixin.config.app-secret}")
    private String appSecret;
    @Value("${weixin.config.template_id}")
    private String template_id;

    @Resource
    private Cache<String, String> weixinAccessToken;
    @Resource
    private Cache<String, String> openidToken;
    @Resource
    private IWeixinApiService weixinApiService;


    @Override
    public String createQrCodeTicket() throws IOException {
        // 获取 access token
        String accessToken = weixinAccessToken.getIfPresent(appid);
        if (null == accessToken) {
            Call<WeixinTokenRes> tokenResCall = weixinApiService.getToken("client_credential", appid, appSecret);
            WeixinTokenRes weixinTokenRes = tokenResCall.execute().body();
            assert weixinTokenRes != null;
            accessToken = weixinTokenRes.getAccess_token();
            weixinAccessToken.put(appid, accessToken);
        }

        // 创建二维码 ticket
        WeixinQrCodeReq weixinQrCodeReq = WeixinQrCodeReq.builder()
                .expire_seconds(259200)
                .action_name(WeixinQrCodeReq.ActionNameTypeVO.QR_SCENE.getCode())
                .action_info(WeixinQrCodeReq.ActionInfo.builder()
                        .scene(WeixinQrCodeReq.ActionInfo.Scene.builder()
                                .scene_id(100601)
                                .build())
                        .build())
                .build();

        Call<WeixinQrCodeRes> qrCodeResCall = weixinApiService.createQrCode(accessToken, weixinQrCodeReq);
        WeixinQrCodeRes weixinQrCodeRes = qrCodeResCall.execute().body();
        assert weixinQrCodeRes != null;
        return weixinQrCodeRes.getTicket();
    }

    @Override
    public String checkLogin(String ticket) {
        return openidToken.getIfPresent(ticket);
    }

    @Override
    public void saveLoginState(String openid, String ticket) throws IOException {
        openidToken.put(ticket, openid);

        // 获取 access token
        String accessToken = weixinAccessToken.getIfPresent(appid);
        if (null == accessToken) {
            Call<WeixinTokenRes> tokenResCall = weixinApiService.getToken("client_credential", appid, appSecret);
            WeixinTokenRes weixinTokenRes = tokenResCall.execute().body();
            assert weixinTokenRes != null;
            accessToken = weixinTokenRes.getAccess_token();
            weixinAccessToken.put(appid, accessToken);
        }
        // 发送模板消息
        Map<String, Map<String, String>> data = new HashMap<>();
        WeixinTemplateMessageVO.put(data, WeixinTemplateMessageVO.TemplateKey.USER, openid);
        WeixinTemplateMessageVO templateMessageDTO = new WeixinTemplateMessageVO(openid, template_id);
        templateMessageDTO.setData(data);
        templateMessageDTO.setUrl("https://blog.kangyaocoding.top");

        log.info("发送模板消息：{}", templateMessageDTO);

        Call<Void> call = weixinApiService.sendTemplateMessage(accessToken, templateMessageDTO);
        call.execute();
    }
}
