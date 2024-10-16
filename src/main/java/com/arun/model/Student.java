package com.arun.model;

public class Student {
    private int id;
    private String name;
    private int age;
    private String gender;
    private int teacherId;

    public Student(int id, String name, int age, String gender, int teacherId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.teacherId = teacherId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public int getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

}
