package com.TravelApp.dto;

import java.util.List;

import com.TravelApp.entity.Notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private List<Notification> notification;
    private Integer unread;
    private Integer totalCount;
}
