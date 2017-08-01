package com.thisrpg.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/7/31 0031.
 */

@Entity
public class Players {

    private Long uid;
    private String name;
    private int gender;
    private String create_time;




    @Generated(hash = 1872262480)
    public Players(Long uid, String name, int gender, String create_time) {
        this.uid = uid;
        this.name = name;
        this.gender = gender;
        this.create_time = create_time;
    }

    @Generated(hash = 1221525943)
    public Players() {
    }




    public Long getId() {
        return uid;
    }

    public void setId(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
