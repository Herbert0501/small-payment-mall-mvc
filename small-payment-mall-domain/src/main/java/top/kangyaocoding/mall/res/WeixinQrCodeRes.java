package top.kangyaocoding.mall.res;

import lombok.Data;

/**
 * 描述: 微信二维码响应结果
 *
 * @author K·Herbert
 * @since 2024-11-27 下午11:37
 */
@Data
public class WeixinQrCodeRes {
    private String ticket;
    private Long expire_seconds;
    private String url;
}
