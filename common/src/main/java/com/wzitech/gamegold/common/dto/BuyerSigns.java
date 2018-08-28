package com.wzitech.gamegold.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by chengXY on 2017/10/31.
 */
public class BuyerSigns {
    /**
     * 服务Id id内容全部小写
     * */
    @JsonProperty("Id")
    private String id;

    /**
     * 服务名称
     * */
    @JsonProperty("Name")
    private String name;

    /**
     * 服务价格
     * */
    @JsonProperty("Price")
    private BigDecimal price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BuyerSigns{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
