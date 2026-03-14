package com.polyglot.analytics.config;

import com.polyglot.analytics.model.Role;
import com.polyglot.analytics.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create default roles if they don't exist
        createRoleIfNotFound("ROLE_USER");
        createRoleIfNotFound("ROLE_MODERATOR");
        createRoleIfNotFound("ROLE_ADMIN");
        
        System.out.println("✅ Default roles initialized successfully!");
    }

    private void createRoleIfNotFound(String roleName) {
        if (!roleRepository.findByName(roleName).isPresent()) {
            Role role = new Role(roleName);
            roleRepository.save(role);
            System.out.println("Created role: " + roleName);
        }
    }
}
