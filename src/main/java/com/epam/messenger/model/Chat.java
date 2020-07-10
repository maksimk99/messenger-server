package com.epam.messenger.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.TableGenerator;
import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
@Entity
public class Chat {

    @TableGenerator(name = "Chat_Gen", initialValue = 5)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "Chat_Gen")
    private Integer chatId;
    @Column(nullable = false)
    private String chatName;
    private String avatarUrl;
    @ManyToMany
    @JoinTable(name = "chat_has_members",
    joinColumns = {
            @JoinColumn(name = "chat_id", nullable = false)
    }, inverseJoinColumns = {
            @JoinColumn(name = "member_id", nullable = false)
    })
    @JsonIgnoreProperties({"hibernateLazyInitializer", "contacts", "password"})
    private List<User> users;
}
