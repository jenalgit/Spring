package com.dawn.spring.bean;

/**
 * Created by dawn-pc on 2014/11/16.
 */
public class ChatMessageUrl extends BaseChatMessage {
    private String code;
    private String text;
    private String url;

    public ChatMessageUrl(Type type, String msg, String code, String text, String url) {
        super(type, msg);
        this.code = code;
        this.text = text;
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
