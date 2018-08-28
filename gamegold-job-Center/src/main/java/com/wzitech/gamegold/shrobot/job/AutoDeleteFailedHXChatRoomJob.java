package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.shorder.dao.IHxChatroomNetWorkDao;
import com.wzitech.gamegold.shorder.dao.IOrderPushRedisDao;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 340096
 * @date 2018/2/7.
 * 定时删除：删除失败的环信聊天室
 */
@Component
@JobHandler("autoDeleteHXChatRoom")
public class AutoDeleteFailedHXChatRoomJob extends IJobHandler{

    protected static final Logger logger = LoggerFactory.getLogger(AutoDeleteFailedHXChatRoomJob.class);

    private static final String JOB_ID ="DELETE_HX_ROOM";

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    @Autowired
    IOrderPushRedisDao orderPushRedisDao;

    @Autowired
    IHxChatroomNetWorkDao hxChatroomNetWorkDao;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            List hxChatRoomId = orderPushRedisDao.findHxChatRoomId();
            if (hxChatRoomId != null){
                for (int i = 0; i < hxChatRoomId.size(); i++){
                    String chatRoomId = (String) hxChatRoomId.get(i);
                    hxChatroomNetWorkDao.deleteChatroom(chatRoomId);
                }
                orderPushRedisDao.removeHxChatRoomId(hxChatRoomId.size());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("autoDeleteHXChatRoom异常:{}",e);
            return FAIL;
        }
    }
}
