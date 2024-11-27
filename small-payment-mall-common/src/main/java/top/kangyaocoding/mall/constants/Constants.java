package top.kangyaocoding.mall.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 描述: 通用参数
 *
 * @author K·Herbert
 * @since 2024-11-28 上午12:00
 */
public class Constants {
    public final static String SPLIT = ",";

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum ResponseCode {
        SUCCESS("0000", "调用成功"),
        UN_ERROR("0001", "调用失败"),
        ILLEGAL_PARAMETER("0002", "非法参数"),
        NO_LOGIN("0003", "未登录"),
        ;

        private String code;
        private String info;

    }

    @Getter
    @AllArgsConstructor
    public enum OrderStatusEnum {

        CREATE("CREATE", "创建完成"),
        PAY_WAIT("PAY_WAIT", "等待支付"),
        PAY_SUCCESS("PAY_SUCCESS", "支付成功"),
        DEAL_DONE("DEAL_DONE", "交易完成"),
        CLOSE("CLOSE", "超时关单"),
        ;

        private final String code;
        private final String desc;

    }
}
