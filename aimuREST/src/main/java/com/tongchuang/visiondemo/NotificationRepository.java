package com.tongchuang.visiondemo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tongchuang.visiondemo.object.Notification;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    List<Notification> findByUserId();
}
