package com.lhs.bean.vo;

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MaaTagRequestVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String uuid;
    private List<String> tags;
    private Integer level;
    private JSONArray result;
    private String server;
    private String source;
    private String version;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }


    public JSONArray getResult() {
        return result;
    }

    public void setResult(JSONArray result) {
        this.result = result;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
