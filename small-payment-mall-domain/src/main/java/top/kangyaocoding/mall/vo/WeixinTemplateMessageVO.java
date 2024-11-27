package top.kangyaocoding.mall.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述: 微信模板消息
 *
 * @author K·Herbert
 * @since 2024-11-27 下午11:42
 */
@Setter
@Getter
public class WeixinTemplateMessageVO {
    private String touser = "ovrXh6XQqKPHUQIaEQLF9_EB1cNk";
    private String template_id = "1TtVeKa8EEDAUoGgGnbYS-12D6hK4ZTAbN3xkaZJNpA";
    private String url = "https://weixin.qq.com";
    private Map<String, Map<String, String>> data = new HashMap<>();

    public WeixinTemplateMessageVO(String touser, String template_id) {
        this.touser = touser;
        this.template_id = template_id;
    }

    public void put(TemplateKey key, String value) {
        data.put(key.getCode(), new HashMap<String, String>() {
            private static final long serialVersionUID = 7092338402387318563L;

            {
                put("value", value);
            }
        });
    }

    public static void put(Map<String, Map<String, String>> data, TemplateKey key, String value) {
        data.put(key.getCode(), new HashMap<String, String>() {
            private static final long serialVersionUID = 7092338402387318563L;

            {
                put("value", value);
            }
        });
    }


    @Getter
    public enum TemplateKey {
        USER("user", "用户ID");

        private final String code;
        private final String desc;

        TemplateKey(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }


}
