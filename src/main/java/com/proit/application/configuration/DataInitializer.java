package com.proit.application.configuration;

import com.proit.application.entity.Role;
import com.proit.application.entity.UserEntity;
import com.proit.application.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public DataInitializer(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        UserEntity user = new UserEntity()
                .setUsername("user")
                .setPassword(passwordEncoder.encode("user1"))
                .setEmail("user@mail.com")
                .setRoles(new HashSet<Role>(Collections.singleton(Role.USER)));

        UserEntity admin = new UserEntity()
                .setUsername("admin")
                .setPassword(passwordEncoder.encode("admin1"))
                .setEmail("admin@mail.com")
                .setRoles(new HashSet<Role>(Arrays.asList(Role.USER, Role.ADMIN)));

        List<UserEntity> users = new LinkedList<>();
        if(!userRepository.existsByUsername("user")) users.add(user);
        if(!userRepository.existsByUsername("admin")) users.add(admin);

        if(users.size() > 0) userRepository.saveAll(users);
    }
}
