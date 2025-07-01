package com.taskmanager.app.notifications;

import lombok.Data;

import java.util.List;

@Data
public class NotificationResponse {

    private List<String> message;
}
