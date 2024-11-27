package top.kangyaocoding.mall.weixin;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 描述: 文本消息
 *
 * @author K·Herbert
 * @since 2024-11-27 下午9:34
 */
@Data
public class MessageTextEntity {
    @XStreamAlias("ToUserName")
    private String toUserName;

    @XStreamAlias("FromUserName")
    private String fromUserName;

    @XStreamAlias("CreateTime")
    private String createTime;

    @XStreamAlias("MsgType")
    private String msgType;

    @XStreamAlias("Event")
    private String event;

    @XStreamAlias("EventKey")
    private String eventKey;

    @XStreamAlias("MsgID")
    private String msgId;

    @XStreamAlias("Status")
    private String status;

    @XStreamAlias("Ticket")
    private String ticket;

    @XStreamAlias("Content")
    private String content;


    public MessageTextEntity() {
    }
}
