package com.mufeng.entity;

import lombok.*;

/**
 * @author MFGN
 * @data 2022/3/13 1:41
 * @description 学生
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student3 {
    private String id;
    private String name;
    private String mobile;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
