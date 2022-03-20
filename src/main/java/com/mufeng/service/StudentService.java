package com.mufeng.service;

import com.mufeng.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 测试用的学生表(Student)表服务接口
 *
 * @author makejava
 * @since 2022-03-18 16:43:08
 */
public interface StudentService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Student queryById(Integer id);

    /**
     * 分页查询
     *
     * @param student 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<Student> queryByPage(Student student, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param student 实例对象
     * @return 实例对象
     */
    Student insert(Student student);

    /**
     * 修改数据
     *
     * @param student 实例对象
     * @return 实例对象
     */
    Student update(Student student);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 通过主键查询关联信息
     * @param id 主键
     * @return  实例对象
     */
    Student selectManyToOne1(Integer id);
    Student queryById3(Integer id);
}
