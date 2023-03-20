package com.example.myhome.home.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

// --- СООБЩЕНИЯ ---

@Data
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //отправитель
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    //получатель(-и)
    @ManyToMany
    @JoinTable(
            name = "messages_users",
            joinColumns = @JoinColumn(name = "owner_id"),
            inverseJoinColumns = @JoinColumn(name = "message_id"))
    private List<Owner> receivers;

    //Тема сообщения
    private String subject;

    //Содержание
    private String text;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}
