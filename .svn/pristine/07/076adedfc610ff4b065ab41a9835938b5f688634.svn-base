package com.wzitech.gamegold.common.main;

import com.wzitech.gamegold.common.entity.IImageRedisDAO;
import com.wzitech.gamegold.common.utils.StreamIOHelper;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 339928 on 2017/11/24.
 */
@Component
public class GetImageUrlFromMain implements IGetImageUrlFromMain {

    private static final String IMAGE_URL = "http://lm.5173esb.com/service/GetCategoryObject";

    @Autowired
    IImageRedisDAO iImageRedsiDAO;

    @Override
    public String getImage(String gameId) {
        if (StringUtils.isBlank(gameId)){
            return "";
        }
        String imageUrl = iImageRedsiDAO.getImage(gameId);
        if (StringUtils.isBlank(imageUrl)) {
            InputStream inputStream = null;
            try {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                StringBuffer stringBuffer = new StringBuffer(IMAGE_URL);
                stringBuffer.append("?id=" + gameId);
                HttpGet httpGet = new HttpGet(stringBuffer.toString());
                httpGet.addHeader("Content-Type", "application/json");
                CloseableHttpResponse response = null;
                response = httpclient.execute(httpGet);
                inputStream = response.getEntity().getContent();
                String responseStr = StreamIOHelper.inputStreamToStr(inputStream, "utf-8");
                StringBuffer sb = new StringBuffer(responseStr);
                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(0);
                JSONObject jsonObject = JSONObject.fromObject(sb.toString());
                imageUrl = jsonObject.getString("SmallIconUrl");
                iImageRedsiDAO.setImageUrl(gameId, imageUrl);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return imageUrl;
    }

}
