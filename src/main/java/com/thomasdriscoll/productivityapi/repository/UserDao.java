package com.thomasdriscoll.productivityapi.repository;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="t_users")
@IdClass(UserKey.class)
public class UserDao {
    @Id
    @Column(name="id")
    String userId;

    @Id
    @Column(name="email")
    String email;

    @Column(name="firstName")
    String firstName;

    @Column(name="lastName")
    String lastName;

    @Column(name="password")
    String password;
}

class UserKey implements Serializable {
    String userId;
    String email;
}
