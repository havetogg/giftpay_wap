package org.jumutang.giftpay.entity;

/**
 * Created by RuanYJ on 2017/6/15.
 */
public class CountModel {
    public static final String NEWUSER="newUser";
    public static final String REGISTERUSER="registerUser";
    public static final String ZSHUSER="zshUser";
    public static final String FULIUSER="fuliUser";
    public static final String OTHERUSER="otherUser";

    private String num;
    private String addTime;
    private String countType;
    private String startTime;
    private String endTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCountType() {
        return countType;
    }

    public void setCountType(String countType) {
        this.countType = countType;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
