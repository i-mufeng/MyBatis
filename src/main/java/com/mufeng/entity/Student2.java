package com.mufeng.entity;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author MFGN
 * @data 2022/3/13 1:41
 * @description 学生
 */

public class Student2 {
    private Integer id;
    private String name;
    private String mobile;
    private LocalDateTime createTime;

    //private Date createTime;

    public Student2() {
    }

    public Student2(Integer id, String name, String mobile, LocalDateTime createTime) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Student2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
