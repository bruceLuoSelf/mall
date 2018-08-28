package com.wzitech.gamegold.rc8.service.game.dto;

import com.wzitech.gamegold.rc8.dto.Response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author yemq
 */
@XmlRootElement(name = "Result")
public class GameResponse extends Response {
    private List<Game> games;

    public GameResponse() {
    }

    @XmlElementWrapper(name = "Games")
    @XmlElement(name = "Game")
    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    @XmlRootElement(name = "Game")
    public static class Game {
        private String id;
        private String name;

        public Game() {
        }

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
    }
}
