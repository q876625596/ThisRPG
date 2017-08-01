package com.thisrpg.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/7/31 0031.
 */

@Entity
public class User {
    @Id
    private Long id;
    private String name;
    private int gender;
    private String create_time;

    @Generated(hash = 2094293998)
    public User(Long id, String name, int gender, String create_time) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.create_time = create_time;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
