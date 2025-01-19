package com.vaccine.initdb;

import com.vaccine.entity.Role;
import com.vaccine.entity.User;
import com.vaccine.enums.EnumRoleName;
import com.vaccine.repository.RoleRepository;
import com.vaccine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInit {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public CommandLineRunner database() {
        return args -> {
            if (roleRepository.count() == 0) {
                Role roleAdmin = new Role(EnumRoleName.ROLE_ADMIN, null);
                Role roleStaff = new Role(EnumRoleName.ROLE_STAFF, null);
                Role roleUser = new Role(EnumRoleName.ROLE_USER, null);
                Role roleManager = new Role(EnumRoleName.ROLE_MANAGER, null);

                roleRepository.save(roleAdmin);
                roleRepository.save(roleStaff);
                roleRepository.save(roleUser);
                roleRepository.save(roleManager);
            }

            if (userRepository.count() == 0) {
                Role role = roleRepository.getRoleByRoleName(EnumRoleName.ROLE_USER);
                User user = User.builder()
                        .fullName("HOANG SON HA")
                        .accessToken(null)
                        .refreshToken(null)
                        .email("hoangsonhadev@gmail.com")
                        .password(bCryptPasswordEncoder.encode("123456"))
                        .enabled(true)
                        .nonLocked(true)
                        .role(role)
                        .build();
                userRepository.save(user);
            }
        };
    }
}
