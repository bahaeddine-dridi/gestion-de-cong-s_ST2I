package com.ST2I.spring.login.repository;

import com.ST2I.spring.login.models.Entities.Notification;
import com.ST2I.spring.login.models.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserTo(User user); // Find all notifications for a recipient
}