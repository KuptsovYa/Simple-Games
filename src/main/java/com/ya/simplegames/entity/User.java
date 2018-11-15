package com.ya.simplegames.entity;

import com.ya.simplegames.config.SQLInjectionSafe;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsers")
    private Long idUsers;

    @SQLInjectionSafe
    @Column(name = "nick")
    private String nick;

    @SQLInjectionSafe
    @Column(name = "name")
    private String name;

    @SQLInjectionSafe
    @Column(name = "password")
    private String password;

    @SQLInjectionSafe
    @Column(name = "role")
    private String role;

    @Column(name = "rating")
    private int rating;

    @SQLInjectionSafe
    @Column(name = "firstName")
    private String firstName;

    @SQLInjectionSafe
    @Column(name = "lastName")
    private String lastName;

    @Column(name = "regDate")
    private Timestamp regDate;

    public User() {
    }

    public User(String nick, String name, String password, String role, int rating, String firstName, String lastName, Timestamp regDate) {
        this.nick = nick;
        this.name = name;
        this.password = password;
        this.role = role;
        this.rating = rating;
        this.firstName = firstName;
        this.lastName = lastName;
        this.regDate = regDate;
    }

    public String getNick() { return nick; }

    public void setNick(String nick) { this.nick = nick; }

    public Timestamp getRegDate() { return regDate; }

    public void setRegDate(Timestamp regDate) { this.regDate = regDate; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getRating() { return rating; }

    public void setRating(int rating) { this.rating = rating; }

    public Long getIdUsers() { return idUsers; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }
}
