package com.ormcore.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class TblUserInfo implements Serializable {
    private java.lang.Integer id; //
    private java.lang.String password;
    private java.lang.String phone;
    private java.lang.String name;
    private java.lang.String iccard;
    private java.lang.String vehicleNumber;
    private java.math.BigDecimal balance;  //普通用户充值余额
    private java.math.BigDecimal present; //赠送余额
    private java.lang.Integer status; //
    private Integer level;
    private java.lang.String invitePhone;//邀请者号码
    private java.lang.Integer accountId;
    private java.lang.Integer cpyId=0;
    private java.lang.Integer cpyNumber=0;
    private java.lang.Integer levelId=0;
    private java.lang.String deviceid;//手机设备ID号

    public TblUserInfo() {
        level = 0;
    }

    public java.lang.String getInvitePhone() {
        return invitePhone;
    }

    public void setInvitePhone(java.lang.String invitePhone) {
        this.invitePhone = invitePhone;
    }

    public java.lang.String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(java.lang.String deviceid) {
        this.deviceid = deviceid;
    }

    public java.lang.String getDeviceId() {
        return deviceid;
    }

    public void setDeviceId(java.lang.String deviceid) {
        this.deviceid = deviceid;
    }

    public java.lang.Integer getStatus() {
        return status;
    }

    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    public java.lang.Integer getId() {
        return id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.String getPassword() {
        return password;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    public java.lang.String getPhone() {
        return phone;
    }

    public void setPhone(java.lang.String phone) {
        this.phone = phone;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getIccard() {
        return iccard;
    }

    public void setIccard(java.lang.String iccard) {
        this.iccard = iccard;
    }

    public java.lang.String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(java.lang.String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public java.math.BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(java.math.BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getPresent() {
        return present;
    }

    public void setPresent(BigDecimal present) {
        this.present = present;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCpyId() {
        return cpyId;
    }

    public void setCpyId(Integer cpyId) {
        this.cpyId = cpyId;
    }

    public Integer getCpyNumber() {
        return cpyNumber;
    }

    public void setCpyNumber(Integer cpyNumber) {
        this.cpyNumber = cpyNumber;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }
}
