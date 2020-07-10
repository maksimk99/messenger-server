package com.epam.messenger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class Message {
    @TableGenerator(name = "Message_Gen", initialValue = 18)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "Message_Gen")
    private Integer messageId;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private Timestamp dateSent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    @JsonIgnore
    private Chat chat;
    @Column(name = "chat_id", insertable = false, updatable = false)
    private Integer chatId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnore
    private User sender;
    @Column(name = "sender_id", insertable = false, updatable = false)
    private Integer senderId;
}
