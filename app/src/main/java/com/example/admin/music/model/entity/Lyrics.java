package com.example.admin.music.model.entity; ;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lyrics {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_user")
    @Expose
    private String idUser;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("singer")
    @Expose
    private String singer;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("download")
    @Expose
    private Integer download;

    public Lyrics() {
        idUser = "";
        name = "";
        singer = "";
        content = "";
        download = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDownload() {
        return download;
    }

    public void setDownload(Integer download) {
        this.download = download;
    }

}