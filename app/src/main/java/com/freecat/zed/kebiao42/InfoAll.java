package com.freecat.zed.kebiao42;

import java.util.List;

/**
 * Created by Mr.Z on 2016/3/22.
 */
public class InfoAll {
    /**
     * this class contains
     * course_all 4 all course information
     * student 4 student information
     */
    List<Course> course_all;
    Student student;

    public List<Course> getCourse_all() {
        return course_all;
    }

    public void setCourse_all(List<Course> course_all) {
        this.course_all = course_all;
    }


    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
