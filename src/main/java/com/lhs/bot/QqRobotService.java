package com.lhs.bot;

import com.lhs.bean.pojo.qqMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface QqRobotService {
    void QqRobotEvenHandle(HttpServletRequest request);

    void sendItemImg(String type,long group_id);

    void sendModImg(long group_id,String roleName);

    void sendCharImg(long group_id, String roleName);

    void sendSkillImg(long group_id, String roleName);

    boolean imageOcr(String messageId,long group_id);

    void sendMessage(long group_id,String message,boolean toUTF8);

    Boolean verificationPenguinsData();

    void spaceSend(Long[] group_ids);

    void sendGroupMessage(long group_id,String message);

    void deleteMessage(Integer message_id);
}