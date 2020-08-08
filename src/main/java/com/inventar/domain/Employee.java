package com.inventar.domain;

import com.inventar.enums.Department;
import com.inventar.enums.Jobposition;

public class Employee {

    private long id;

    private String surname;
    private String name;
    private String patronymic;
    private Department department;
    private String room;
    private Jobposition jobposition;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Jobposition getJobposition() {
        return jobposition;
    }

    public void setJobposition(Jobposition jobposition) {
        this.jobposition = jobposition;
    }
}
