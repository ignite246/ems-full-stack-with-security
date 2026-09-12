package com.rahul.learning.ems.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles", //table name
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), //column1
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id") //column2
            //both columns will be foreign key in the table "users_roles"
    )
    private Set<Role> roles = new HashSet<>();
}