package tuo.service;

import tuo.model.User;
import tuo.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> findByEmail(String email);

    User save(UserRegistrationDto registration);
}
