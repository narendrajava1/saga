package com.naren.user.repo;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String dept;
    private Integer salary;
    private Date time;
}