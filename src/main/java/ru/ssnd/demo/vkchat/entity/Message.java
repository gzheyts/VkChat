package ru.ssnd.demo.vkchat.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@Builder
public class Message {
    @Id
    private String identifier;

    private Integer id;

    private Date sentAt;

    private String text;

    private Sender sender;
}
