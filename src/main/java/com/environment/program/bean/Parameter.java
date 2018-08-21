package com.environment.program.bean;

import java.util.Date;

public class Parameter {

    private Integer id;
    // 温度
    private String temperature;
    // 湿度
    private String humidity;
    // 甲醛
    private String hcho;
    //总挥发性有机物
    private String tvoc;
    //CO2
    private String coTwo;
    // PM2.5
    private String pMTwoPointFive;
    // PM1.0
    private String pMOnePointZero;
    //光照
    private String illumination;
    //风速
    private String windSpeed;
    //风向
    private String windDirection;
    //风向
    private String deviceId;
    //创建时间
    private Date createTime;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getHcho() {
        return hcho;
    }

    public void setHcho(String hcho) {
        this.hcho = hcho;
    }

    public String getTvoc() {
        return tvoc;
    }

    public void setTvoc(String tvoc) {
        this.tvoc = tvoc;
    }

    public String getCoTwo() {
        return coTwo;
    }

    public void setCoTwo(String coTwo) {
        this.coTwo = coTwo;
    }

    public String getpMTwoPointFive() {
        return pMTwoPointFive;
    }

    public void setpMTwoPointFive(String pMTwoPointFive) {
        this.pMTwoPointFive = pMTwoPointFive;
    }

    public String getpMOnePointZero() {
        return pMOnePointZero;
    }

    public void setpMOnePointZero(String pMOnePointZero) {
        this.pMOnePointZero = pMOnePointZero;
    }

    public String getIllumination() {
        return illumination;
    }

    public void setIllumination(String illumination) {
        this.illumination = illumination;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "id=" + id +
                ", temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", hcho='" + hcho + '\'' +
                ", tvoc='" + tvoc + '\'' +
                ", coTwo='" + coTwo + '\'' +
                ", pMTwoPointFive='" + pMTwoPointFive + '\'' +
                ", pMOnePointZero='" + pMOnePointZero + '\'' +
                ", illumination='" + illumination + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", windDirection='" + windDirection + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
