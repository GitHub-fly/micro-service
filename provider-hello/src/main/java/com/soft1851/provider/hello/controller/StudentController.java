package com.soft1851.provider.hello.controller;

import com.soft1851.publicmodule.entity.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author xunmi
 * @ClassName StudentController
 * @Description TODO
 * @Date 2020/9/13
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/student")
public class StudentController {

    @GetMapping(value = "/one")
    public Student getOneStudent() {
        return new Student(101, "Tom");
    }

    @GetMapping(value = "/list")
    public List<Student> getStudentList() {
        Student[] students = new Student[] {
                new Student(101, "马云"),
                new Student(101, "马化腾"),
                new Student(101, "马佳琪")
        };
        return Arrays.asList(students);
    }
}
