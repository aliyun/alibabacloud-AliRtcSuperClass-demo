package com.aliyun.rtc.superclassroom.bean;

import java.util.List;

public class RtcOnlineAuthInfo {

    /**
     * appid : 9qb1zcyc
     * userid : 1d3bcabaaa9ad62f
     * nonce : AK-1744277807def3d098a1c627becbfd5d
     * timestamp : 1602820217
     * token : 5a335b751f50b1e3bee3963cd66a0a31ba7823d4ed440f9cefb5e619c39891e0
     * turn : {"username":"1d3bcabaaa9ad62f?appid=9qb1zcyc&channel=23123&nonce=AK-1744277807def3d098a1c627becbfd5d×tamp=1602820217","password":"5a335b751f50b1e3bee3963cd66a0a31ba7823d4ed440f9cefb5e619c39891e0"}
     * gslb : ["https://dgslb.rtc.aliyuncs.com"]
     * agent : ["ragent.rtc.aliyuncs.com"]
     */

    private String appid;
    private String userid;
    private String nonce;
    private int timestamp;
    private String token;
    private TurnBean turn;
    private List<String> gslb;
    private List<String> agent;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TurnBean getTurn() {
        return turn;
    }

    public void setTurn(TurnBean turn) {
        this.turn = turn;
    }

    public List<String> getGslb() {
        return gslb;
    }

    public void setGslb(List<String> gslb) {
        this.gslb = gslb;
    }

    public List<String> getAgent() {
        return agent;
    }

    public void setAgent(List<String> agent) {
        this.agent = agent;
    }

    public static class TurnBean {
        /**
         * username : 1d3bcabaaa9ad62f?appid=9qb1zcyc&channel=23123&nonce=AK-1744277807def3d098a1c627becbfd5d×tamp=1602820217
         * password : 5a335b751f50b1e3bee3963cd66a0a31ba7823d4ed440f9cefb5e619c39891e0
         */

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
