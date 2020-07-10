package com.epam.messenger.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.TableGenerator;
import java.sql.Timestamp;
import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
@Entity
public class User {

    @TableGenerator(name = "User_Gen", initialValue = 8)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "User_Gen")
    private Integer userId;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @Column(nullable = false)
    private String password;
    private String avatarUrl;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private Timestamp lastSeen;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_has_contacts",
            joinColumns = {
                    @JoinColumn(name = "user_id", nullable = false)
            }, inverseJoinColumns = {
            @JoinColumn(name = "contact_id", nullable = false)
    })
    @JsonIgnoreProperties({"contacts", "password"})
    private List<User> contacts;
}
