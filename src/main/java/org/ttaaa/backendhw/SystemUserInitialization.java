package org.ttaaa.backendhw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.ttaaa.backendhw.model.entity.UserRole;
import org.ttaaa.backendhw.service.UserService;

@Component
public class SystemUserInitialization {
    @Value("${system.username}")
    private String systemUsername;

    @Value("${system.password}")
    private String systemPassword;

    @Autowired
    private UserService userService;

    @EventListener
    public void appReady(ApplicationReadyEvent ignoredEvent) {
        userService.createUserIfNotExist(systemUsername, systemPassword, UserRole.SYSTEM);
    }
}
