package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.utils.StreamIOHelper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.io.InputStream;

/**
 * Created by Administrator on 2017/2/19.
 */
public interface IHxChatroomNetWorkDao {

    public void deleteChatroom(String chatroomId);

    public String getHuanXinToken();

    public void sendHxSystemMessage(String roomId, String msg, String id,String userType);

    void sendHxSystemMessage(String userHxId,String msg,String userType);
}
