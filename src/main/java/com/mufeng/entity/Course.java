package com.mufeng.entity;

import java.io.Serializable;

/**
 * 课程表(Course)实体类
 *
 * @author makejava
 * @since 2022-03-18 16:41:12
 */
public class Course implements Serializable {
    private static final long serialVersionUID = -76040904951755894L;
    
    private Integer courseId;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 课程内容
     */
    private String courseContent;


    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseContent() {
        return courseContent;
    }

    public void setCourseContent(String courseContent) {
        this.courseContent = courseContent;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", courseContent='" + courseContent + '\'' +
                '}';
    }
}

