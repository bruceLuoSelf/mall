package com.wzitech.gamegold.facade.service.receiving.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by 339928 on 2018/1/2.
 */
//@XmlRootElement(name = "AutoImageUrls")
public class Urls {


    private List<String> url;

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public Urls() {
    }

    public Urls(List url) {
        this.url = url;
    }

    @XmlElement(name = "Url")
    public List<String> getUrl() {
        return url;
    }

}
