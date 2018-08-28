package com.wzitech.gamegold.backend.test;

import com.wzitech.gamegold.common.httpClientPool.HttpClientPool;
import com.wzitech.gamegold.common.message.IMobileMsgManager;
import com.wzitech.gamegold.facade.backend.business.ISellerShManager;
import com.wzitech.gamegold.facade.backend.job.AutoCancellJob;
import com.wzitech.gamegold.facade.backend.job.AutoPlayJob;
import com.wzitech.gamegold.facade.backend.job.AutoRefundTimeoutPayOrderJob;
import com.wzitech.gamegold.facade.backend.util.CreateZBaoUtil;
import com.wzitech.gamegold.repository.dao.ISellerDBDAO;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chengXY on 2017/8/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-backend-context.xml"})
@ActiveProfiles("development")
public class CxyTest0829 {
    @Autowired
    private ISellerShManager sellerShManager;
    @Autowired
    private HttpClientPool httpClientPool;
    @Autowired
    AutoRefundTimeoutPayOrderJob autoRefundTimeoutPayOrderJob;
    @Autowired
    IMobileMsgManager mobileMsgManager;
    @Autowired
    AutoPlayJob autoPlayJob;

    protected static final Logger logger = LoggerFactory.getLogger(CxyTest0829.class);

    @Test
    public void testPlay(){
        autoPlayJob.autoPlay();
    }

    @Test
    public void sendMsg() throws Exception {
        mobileMsgManager.sendMessage("15805795603","【5173网】订单：YX001, 出货商游戏角色：%s已登录游戏，交易方式：gaga，请及时上号交易");
    }

    @Test
    public void testAutoRefundTimeoutPayOrderJob(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 87);
        System.out.println("==================");
        System.out.println(c.getTime());
        autoRefundTimeoutPayOrderJob.autoRefundTimeoutPayOrder();
    }
    @Test
    public void testRealName() {
        String realName = sellerShManager.isRealName("US17091469170213-005A");
        System.out.println(realName + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    }

    @Test
    public void testSetMerChant() {
        sellerShManager.setMerChant("US16122251120213-0720", 1);
    }


    @Test
    public void test() throws IOException {
        CloseableHttpClient httpClient = httpClientPool.getHttpClient();
        String[] urisToGet = {
                "http://blog.csdn.net/gaolu/article/details/48466059",
                "http://blog.csdn.net/gaolu/article/details/48243103",
                "http://blog.csdn.net/gaolu/article/details/47656987",
                "http://blog.csdn.net/gaolu/article/details/47055029",

                "http://blog.csdn.net/gaolu/article/details/46400883",
                "http://blog.csdn.net/gaolu/article/details/46359127",
                "http://blog.csdn.net/gaolu/article/details/46224821",
                "http://blog.csdn.net/gaolu/article/details/45305769",

                "http://blog.csdn.net/gaolu/article/details/43701763",
                "http://blog.csdn.net/gaolu/article/details/43195449",
                "http://blog.csdn.net/gaolu/article/details/42915521",
                "http://blog.csdn.net/gaolu/article/details/41802319",

                "http://blog.csdn.net/gaolu/article/details/41045233",
                "http://blog.csdn.net/gaolu/article/details/40047065",
                "http://blog.csdn.net/gaolu/article/details/39891877",

                "http://blog.csdn.net/gaolu/article/details/39499073",
                "http://blog.csdn.net/gaolu/article/details/39314327",
                "http://blog.csdn.net/gaolu/article/details/38820809",
                "http://blog.csdn.net/gaolu/article/details/38439375",
        };

        int pagecount = urisToGet.length;
        ExecutorService executors = Executors.newFixedThreadPool(pagecount);
        CountDownLatch countDownLatch = new CountDownLatch(pagecount);
        for (int i = 0; i < pagecount; i++) {
            HttpGet httpget = new HttpGet(urisToGet[i]);
            HttpClientPool.config(httpget);
            executors.execute(new GetRunnable(httpClient, httpget, i, countDownLatch));

//            //启动线程抓取
//            CloseableHttpResponse response = httpClient.execute(httpget);
//            InputStream content = response.getEntity().getContent();
//            String json = IOUtils.toString(content);
//            logger.info("5173短信平台发送给手机终端{}结果：{}", i, json);
        }


    }

    static class GetRunnable implements Runnable {
        private final CloseableHttpClient httpClient;
        private CountDownLatch countDownLatch;
        private final HttpGet httpget;
        private int i;

        public GetRunnable(CloseableHttpClient httpClient, HttpGet httpget, int i, CountDownLatch countDownLatch) {
            this.httpClient = httpClient;
            this.httpget = httpget;
            this.i = i;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            CloseableHttpResponse response = null;
            try {
//                response = httpClient.execute(httpget);
                //启动线程抓取
                response = httpClient.execute(httpget);
                InputStream content = response.getEntity().getContent();
                String json = IOUtils.toString(content);
                logger.info("5173短信平台发送给手机终端{}结果：{}", i, json);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();

                try {
                    if (response != null)
                        response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Autowired
    ISellerDBDAO sellerDBDAO;
    @Autowired
    CreateZBaoUtil createZBaoUtil;

    @Test
    public void tt(){
        SellerInfo seller=sellerDBDAO.findByAccountAndUid("","");
//        createZBaoUtil.createZBaoAccount(seller);
    }

    @Autowired
    AutoCancellJob autoCancellJob;
    @Test
    public void test211(){
        autoCancellJob.autoCancell();
    }
}
