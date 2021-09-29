package com.example.demo.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Data
@Entity
@Table(name = "user")
@SQLDelete(sql = "update user set is_delete = 1 where id =?")
@Where(clause = "is_delete = 0")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column(name = "email")
    @NotBlank(message = "Email is mandatory")
    @Min(value = 8, message = "Email min 8 character")
    private String email;

    @Column(name = "phone")
    @NotBlank(message = "Phone is mandatory")
    private String phone;

    @Column(name = "address")
    @NotBlank(message = "Address is mandatory")
    private String address;

    @Column(name = "created_date")
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "last_update")
    @UpdateTimestamp
    private Date lastUpdate;

    @Column(name = "is_delete")
    private int isDelete = 0;
}
