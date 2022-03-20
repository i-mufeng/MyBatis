package com.mufeng.entity;

import java.io.Serializable;

/**
 * 测试用的学生表(Student)实体类
 *
 * @author makejava
 * @since 2022-03-18 16:43:06
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 993785075777953939L;
    
    private Integer id;
    
    private String name;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 关联的课程id
     */
    private Integer courseId;

    /**
     * 关联的课程对象
     */
    private Course course;

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

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", courseId=" + courseId +
                ", course=" + course +
                '}';
    }
}

