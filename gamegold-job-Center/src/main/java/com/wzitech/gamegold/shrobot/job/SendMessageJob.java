package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.message.IMobileMsgManager;
import com.wzitech.gamegold.order.business.IMessageRuleManager;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.business.ISentMessageManager;
import com.wzitech.gamegold.order.entity.MessageRule;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.order.entity.SentMessage;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 发送短信定时器
 *
 * @author yemq
 */
@Component
@JobHandler("startJob")
public class SendMessageJob extends IJobHandler{
    protected static final Logger logger = LoggerFactory.getLogger(SendMessageJob.class);
    private static final String JOB_ID = "SEND_MESSAGE_JOB";
    private static ScheduledExecutorService scheduledExecService = Executors.newScheduledThreadPool(5);
    /**
     * 存放ScheduledFuture，跟短信规则ID对应
     */
    private static Map<Long, ScheduledFuture<?>> scheduledFutureMap = new HashMap<Long, ScheduledFuture<?>>();

    @Autowired
    private IMessageRuleManager messageRuleManager;
    @Autowired
    private ISentMessageManager sentMessageManager;
    @Autowired
    private IOrderInfoManager orderInfoManager;
    @Autowired
    IMobileMsgManager mobileMsgManager;

    @Resource(name = "jobLock")
    private JobLockRedisImpl jobLock;

    /**
     * 创建任务并执行，确保同一个短信规则只有一个任务在执行
     *
     * @param rule
     */
    private synchronized void schedule(MessageRule rule) {
        // 先从MAP中获取
        ScheduledFuture future = scheduledFutureMap.get(rule.getId());

        // 如果已经有该规则的任务在运行，则停止这个任务，再创建一个新的任务，防止短信规则改变后不能生效
        if (future != null) {
            logger.info("该任务正在执行，准备取消该任务的执行...");
            future.cancel(true);
        }

        logger.info("创建任务");
        // 根据短信规则创建任务
        future = scheduledExecService.scheduleAtFixedRate(getTask(rule), rule.getDelay(), rule.getPeriod(),
                TimeUnit.SECONDS);

        scheduledFutureMap.put(rule.getId(), future);
    }

    /**
     * 获取一个发送短信的任务
     *
     * @param rule
     * @return
     */
    private Runnable getTask(final MessageRule rule) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    // 查询所有需要发送短信的订单
                    Map<String, Object> queryMap = new HashMap<String, Object>();
                    queryMap.put("orderGameName", rule.getGameName());
                    queryMap.put("orderState", rule.getOrderStatus());
                    queryMap.put("goodsTypeName", ServicesContants.TYPE_ALL);
                    List<OrderInfoEO> orders = orderInfoManager.queryOrderInfo(queryMap, "CREATE_TIME", true);
                    logger.info("游戏：{},触发的状态：{},需要发送短信的总数:{}", new Object[]{rule.getGameName(),
                            rule.getOrderStatus(), orders.size()});

                    for (OrderInfoEO order : orders) {
                        // 查询已否已经发送了短信
                        Map<String, Object> queryIfExists = new HashMap<String, Object>();
                        queryIfExists.put("orderId", order.getOrderId());
                        queryIfExists.put("messageRuleId", rule.getId());
                        int count = sentMessageManager.countByMap(queryIfExists);
                        //logger.info("{}已经发送过短信了：{}", new Object[]{order.getOrderId(), ((count > 0) ? true : false)});
                        if (count > 0) {
                            continue;
                        }

                        String content = "订单号：" + order.getOrderId() + "," + rule.getContent();
                        SentMessage message = new SentMessage();
                        message.setOrder(order);
                        message.setMessageRuleId(rule.getId());
                        message.setOrderStatus(order.getOrderState());
                        message.setContent(content);

                        // 发送短信
                        logger.info("正在发送短信,orderId:{}", order.getOrderId());
                        boolean sendSuccess = false;
                        for (int ii = 0; ii < 3; ii++) {
                            String mobilePhone = order.getMobileNumber();
                            // 验证手机号码格式
                            if (StringUtils.isBlank(mobilePhone)) {
                               break;
                            } else {
                                /**
                                 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
                                 　联通：130、131、132、152、155、156、185、186
                                 　电信：133、153、180、189、（1349卫通）
                                 */
                                Pattern regex = Pattern.compile("^((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$");
                                Matcher matcher = regex.matcher(mobilePhone);
                                if (!matcher.matches()) {
                                    break;
                                }
                            }
                            if (!sendMessage(mobilePhone, message)) {
                                logger.info("短信发送失败重新发送,orderId:{}", order.getOrderId());
                                continue;
                            } else {
                                sendSuccess = true;
                                break;
                            }
                        }

                        // 将已发送的短信存入数据库
                        if (sendSuccess) {
                            sentMessageManager.add(message);
                            //logger.info("{}短信发送成功，写入数据库", order.getOrderId());
                        }
                    }
                    logger.info("游戏：{},触发的状态：{},发送完毕", rule.getGameName(), rule.getOrderStatus());
                } catch (Exception e) {
                    logger.error("执行发短信任务出错了", e);
                }
            }
        };
        return task;
    }

    /**
     * 发送短信
     *
     * @param message
     * @return
     */
    private boolean sendMessage(String mobilePhone, SentMessage message) {
        try {
            mobileMsgManager.sendMessage(mobilePhone, message.getContent());
        } catch (Exception e) {
            logger.error("发送短信出错了", e);
            return false;
        }
        return true;
    }

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            // 查询短信规则
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("enabled", true);
            List<MessageRule> rules = messageRuleManager.queryList(queryMap, "LAST_UPDATE_TIME", true);
            for (MessageRule rule : rules) {
                schedule(rule);
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("startJob异常：{}", e);
            return FAIL;
        }
    }
}
