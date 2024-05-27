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
    @Value("${system.username:user}")
    private String systemUsername;

    @Value("${system.password:password}")
    private String systemPassword;

    @Value("${system.user.init.enabled:false}")
    private Boolean initEnabled;

    public Boolean isInitEnabled() {
        return initEnabled;
    }

    @Autowired
    private UserService userService;

    @EventListener(
            value = ApplicationReadyEvent.class,
            condition = "@systemUserInitialization.isInitEnabled()"
    )
    public void appReady(ApplicationReadyEvent ignoredEvent) {
        userService.createUserWithRoleIfNotExist(systemUsername, systemPassword, UserRole.SYSTEM);
    }

}
