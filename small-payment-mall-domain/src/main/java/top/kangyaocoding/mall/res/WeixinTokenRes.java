package top.kangyaocoding.mall.res;

import lombok.Data;

/**
 * 描述: 微信公众号获取token响应结果
 *
 * @author K·Herbert
 * @since 2024-11-27 下午11:33
 */
@Data
public class WeixinTokenRes {
    private String access_token;
    private int expires_in;
    private String errcode;
    private String errmsg;
}
