package com.wzitech.gamegold.shorder.dto;

/**
 * Created by ljn on 2018/3/13.
 */
public class GameGoodsTypeDTO {

    private Long id;

    private String gameGoodsTypeName;

    public GameGoodsTypeDTO(Long id, String gameGoodsTypeName) {
        this.id = id;
        this.gameGoodsTypeName = gameGoodsTypeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameGoodsTypeName() {
        return gameGoodsTypeName;
    }

    public void setGameGoodsTypeName(String gameGoodsTypeName) {
        this.gameGoodsTypeName = gameGoodsTypeName;
    }
}
