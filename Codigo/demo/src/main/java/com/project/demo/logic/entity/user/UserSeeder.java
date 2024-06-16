package com.project.demo.logic.entity.user;

import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@DependsOn("roleSeeder")
public class UserSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createDefaultUser();
    }

    private void createDefaultUser() {
        User defaultUser = new User();
        defaultUser.setName("Luis");
        defaultUser.setLastname("Monge");
        defaultUser.setEmail("lmongec@ucenfotec.com");
        defaultUser.setPassword("lmongec123");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        Optional<User> optionalUser = userRepository.findByEmail(defaultUser.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user.setName(defaultUser.getName());
        user.setLastname(defaultUser.getLastname());
        user.setEmail(defaultUser.getEmail());
        user.setPassword(passwordEncoder.encode(defaultUser.getPassword()));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }
}
