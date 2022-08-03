package com.kontrol.newsarchive.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeskOfficer {

    private static final Logger LOGGER = Logger.getLogger(DeskOfficer.class.getName());
    private String username;
    private String password;
    private String intervals;
    private String aggregatorName;
    private String aggregatorUrl;

    public DeskOfficer(){
    }

    public DeskOfficer(String username, String password, String intervals,
                       String aggregatorName, String aggregatorUrl){
        this.username = username;
        this.password = password;
        this.intervals = intervals;
        this.aggregatorName = aggregatorName;
        this.aggregatorUrl = aggregatorUrl;
    }

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

    public String getIntervals() {
        return intervals;
    }

    public void setIntervals(String intervals) {
        this.intervals = intervals;
    }

    public String getAggregatorName() {
        return aggregatorName;
    }

    public void setAggregatorName(String aggregatorName) {
        this.aggregatorName = aggregatorName;
    }

    public String getAggregatorUrl() {
        return aggregatorUrl;
    }

    public void setAggregatorUrl(String aggregatorUrl) {
        this.aggregatorUrl = aggregatorUrl;
    }

    public static String sha1(String input){
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new IllegalStateException("Unable to create message digest");
        }
    }

    @Override
    public String toString() {
        return "DeskOfficer{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", intervals='" + intervals + '\'' +
                ", aggregatorName='" + aggregatorName + '\'' +
                ", aggregatorUrl='" + aggregatorUrl + '\'' +
                '}';
    }
}
