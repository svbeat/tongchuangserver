package com.tongchuang.visiondemo.notification;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    List<Notification> findByUserId();
}
