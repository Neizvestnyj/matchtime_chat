package com.example.matchtimechat;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment; // Импортируем правильный класс
import org.springframework.stereotype.Component;

@Component
public class ProfileChecker {

    private static final Logger logger = LoggerFactory.getLogger(ProfileChecker.class);

    @Autowired
    private Environment environment;

    @PostConstruct
    public void checkActiveProfiles() {
        String[] activeProfiles = environment.getActiveProfiles(); // Этот метод доступен у org.springframework.core.env.Environment

        if (activeProfiles.length > 0) {
            logger.info("Active profiles:");
            for (String profile : activeProfiles) {
                logger.info(profile);
            }
        } else {
            logger.warn("No active profiles found");
        }
    }
}
