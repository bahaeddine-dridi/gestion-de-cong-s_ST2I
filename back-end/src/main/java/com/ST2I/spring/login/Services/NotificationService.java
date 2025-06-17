package com.ST2I.spring.login.Services;

import com.ST2I.spring.login.models.DTOs.NotificationRequest;
import com.ST2I.spring.login.models.DTOs.NotificationResponse;
import com.ST2I.spring.login.models.DTOs.UserResponse;
import com.ST2I.spring.login.models.Entities.Notification;
import com.ST2I.spring.login.models.Entities.User;
import com.ST2I.spring.login.repository.NotificationRepository;
import com.ST2I.spring.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    public NotificationResponse createNotification(NotificationRequest request) {
        User userTo = userRepository.findById(request.getUserToId())
                .orElseThrow(() -> new RuntimeException("User To not found"));
        User userFrom = userRepository.findById(request.getUserFromId())
                .orElseThrow(() -> new RuntimeException("User From not found"));

        Notification notification = new Notification(
                request.getContent(),
                userTo,
                userFrom
        );
        Notification saved = notificationRepository.save(notification);
        return mapToResponse(saved);
    }

    public List<NotificationResponse> getNotificationsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findByUserTo(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private NotificationResponse mapToResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setContent(notification.getContent());

        UserResponse userToResponse = new UserResponse();
        userToResponse.setId(notification.getUserTo().getId());
        userToResponse.setUsername(notification.getUserTo().getUsername());
        userToResponse.setEmail(notification.getUserTo().getEmail());
        userToResponse.setRoles(notification.getUserTo().getRoles());
        response.setUserTo(userToResponse);

        UserResponse userFromResponse = new UserResponse();
        userFromResponse.setId(notification.getUserFrom().getId());
        userFromResponse.setUsername(notification.getUserFrom().getUsername());
        userFromResponse.setEmail(notification.getUserFrom().getEmail());
        userFromResponse.setRoles(notification.getUserFrom().getRoles());
        response.setUserFrom(userFromResponse);

        response.setCreationDate(notification.getCreationDate());
        return response;
    }
}