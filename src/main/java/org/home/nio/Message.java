package org.home.nio;

import com.alibaba.fastjson.JSON;

public class Message {
    final public String Operation_Message = "MSG";
    public String operation = "";
    public String from = "";
    public String to = "";
    public String body = "";

    public static String ToString(Message message) {
        return JSON.toJSONString(message);
    }
    public String ToString() {
        return ToString(this);
    }

    public static Message getFromString(String json) {
        return JSON.parseObject(json, Message.class);
    }
}
