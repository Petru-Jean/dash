package org.dash.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "message_create")
public class MessageCreateEventEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "message_id")
    String messageId;

    @Column(name = "user_id")
    String userId;

    @Column(name = "content")
    String content;

    @Column(name = "channel_id")
    String channelId;

    @Column(name = "nickname")
    String nickname;

    @Column(name = "username")
    String username;

    @Column(name = "timestamp")
    ZonedDateTime timestamp;

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
