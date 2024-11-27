package top.kangyaocoding.mall;

import java.io.IOException;

/**
 * 描述: 登录服务接口
 *
 * @author K·Herbert
 * @since 2024-11-27 下午11:22
 */
public interface ILoginService {

    String createQrCodeTicket() throws IOException;

    String checkLogin(String ticket);

    void saveLoginState(String openid, String ticket) throws IOException;
}
