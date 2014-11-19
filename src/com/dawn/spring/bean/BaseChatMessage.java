package com.dawn.spring.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dawn-pc on 2014/11/15.
 */
public class BaseChatMessage {
    /*
    * 文字类：
	* "code":100000,
	* "text":"你好，我是图灵机器人
	*/

    /*消息类型*/
    public Type type;
    /*消息类容*/
    public String msg;
    /*日期*/
    public Date date;
    /*日期的字符格式*/
    public String dateStr;
    /*发送人*/
    public String name;

    public enum Type{
        INPUT,OUTPUT
    }

    public BaseChatMessage() {
    }

    public BaseChatMessage(Type type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateStr = df.format(date);
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
