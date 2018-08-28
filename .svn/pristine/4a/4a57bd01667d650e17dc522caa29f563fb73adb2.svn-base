package com.wzitech.gamegold.facade.backend.job;

import com.wzitech.gamegold.shorder.dao.IHxChatroomNetWorkDao;
import com.wzitech.gamegold.shorder.dao.IOrderPushRedisDao;
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
public class AutoDeleteFailedHXChatRoomJob {

    protected static final Logger logger = LoggerFactory.getLogger(AutoDeleteFailedHXChatRoomJob.class);

    private static final String JOB_ID ="DELETE_HX_ROOM";

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    @Autowired
    IOrderPushRedisDao orderPushRedisDao;

    @Autowired
    IHxChatroomNetWorkDao hxChatroomNetWorkDao;

    public void autoDeleteHXChatRoom(){
        Boolean locked = jobLock.lock(JOB_ID);
        if (!locked) {
            logger.info("上一个补偿订单AutoDeleteFailedHXChatRoom尚未结束");
            return;
        }
        try {
            List hxChatRoomId = orderPushRedisDao.findHxChatRoomId();
            if (hxChatRoomId != null){
                for (int i = 0; i < hxChatRoomId.size(); i++){
                    String chatRoomId = (String) hxChatRoomId.get(i);
                    hxChatroomNetWorkDao.deleteChatroom(chatRoomId);
                }
                orderPushRedisDao.removeHxChatRoomId(hxChatRoomId.size());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
