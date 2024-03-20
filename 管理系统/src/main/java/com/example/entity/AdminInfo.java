package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "admin_info")
public class AdminInfo extends Account{

    public boolean equals(AdminInfo obj) {
        if (this.getName().equals(obj.getName()) && this.getAge().equals(obj.getAge())) {
            return true;
        }
        return false;
    }

}
