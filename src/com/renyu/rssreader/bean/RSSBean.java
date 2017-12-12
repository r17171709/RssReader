package com.renyu.rssreader.bean;

/**
 * Created by renyu on 2016/12/9.
 */
public class RSSBean {
    String tile;
    String link;

    public RSSBean(String tile, String link) {
        this.tile = tile;
        this.link = link;
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
