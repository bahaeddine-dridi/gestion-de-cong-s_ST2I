package com.ST2I.spring.login.controllers;

import com.ST2I.spring.login.models.DTOs.NotificationRequest;
import com.ST2I.spring.login.models.DTOs.NotificationResponse;
import com.ST2I.spring.login.Services.NotificationService;
import com.ST2I.spring.login.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody NotificationRequest request) {
        NotificationResponse response = notificationService.createNotification(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        List<NotificationResponse> notifications = notificationService.getNotificationsForUser(userDetails.getId());
        return ResponseEntity.ok(notifications);
    }
}