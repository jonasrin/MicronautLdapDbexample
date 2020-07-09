package dk.frontit.learning.service;

import dk.frontit.learning.domain.Role;
import dk.frontit.learning.domain.User;
import io.micronaut.security.authentication.providers.PasswordEncoder;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Singleton
public class RegisterService {

    @Inject
    UserRepositoryImpl userRepository;
    @Inject
    RoleRepositoryImpl roleRepository;
    @Inject
    UserRoleRepositoryImpl userRoleRepository;
    @Inject
    BCryptPasswordEncoderService passwordEncoder ;

    public void register(@Email String email, @NotBlank String userName, @NotBlank String rawPassword, List<String> authorities) {
        User user = userRepository.findByUsername(userName).orElseGet(() -> {
            final String encodedPassword = passwordEncoder.encode(rawPassword);
            return userRepository.save(email, userName, encodedPassword);
        });
        if (Objects.nonNull(user) && authorities.size() > 0) {
            authorities.stream().forEach(auth -> findOrSaveByAuth(auth, user));
        }
    }

    private <R> void findOrSaveByAuth(String authority, User user) {
        Role role = roleRepository.find(authority).orElseGet(() -> roleRepository.save(authority));
        userRoleRepository.find(user, role).orElse(userRoleRepository.save(user, role));
    }

}
