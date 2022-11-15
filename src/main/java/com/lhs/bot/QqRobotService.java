package com.lhs.bot;

import com.lhs.bean.pojo.qqMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface QqRobotService {
    void QqRobotEvenHandle(HttpServletRequest request);
    void sendItemImg(long group_id);
    void sendModImg(long group_id,String roleName);
    void sendMessage(long group_id,String message);

    void sendCharImg(long group_id, String roleName);
    void sendSkillImg(long group_id, String roleName);
    boolean imageOcr(String messageId,long group_id);
    List<qqMessage> getGroupMessage(long group_id, HttpServletRequest request);
}