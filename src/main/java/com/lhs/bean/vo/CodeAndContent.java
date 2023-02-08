package com.lhs.bean.vo;

import com.alibaba.fastjson.JSONObject;

public class CodeAndContent {
    private Boolean code;
    private JSONObject content;

    public Boolean getCode() {
        return code;
    }

    public void setCode(Boolean code) {
        this.code = code;
    }

    public JSONObject getContent() {
        return content;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }
}
