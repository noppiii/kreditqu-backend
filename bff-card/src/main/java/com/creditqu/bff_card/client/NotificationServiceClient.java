package com.creditqu.bff_card.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notification-service")
public interface NotificationServiceClient {

    @PostMapping("/api/notifications/application-received")
    void sendApplicationReceivedEmail(
            @RequestParam("email") String email,
            @RequestParam("name") String name,
            @RequestParam("applicationNumber") String applicationNumber
    );
}
