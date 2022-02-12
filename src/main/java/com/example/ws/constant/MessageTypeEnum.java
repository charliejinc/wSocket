package com.example.ws.constant;

/**
 * 消息类型
 */
public enum MessageTypeEnum {
    //消息内容为普通文本
    TEXT("01", "文本消息"),
    IMAGE("02", "图片消息"),
    AUDIO("03","音频消息"),
    VIDEO("04","视频消息"),
    FILE("05", "文件消息"),
    LOCATION("06","定位消息");
    public String code;
    public String msg;
    MessageTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
