package ru.marilka888.jeweller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.marilka888.jeweller.common.exception.UserNotFoundException;
import ru.marilka888.jeweller.model.User;
import ru.marilka888.jeweller.model.request.UserRequest;
import ru.marilka888.jeweller.model.response.UserResponse;
import ru.marilka888.jeweller.repository.UserRepository;

import java.security.Principal;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Cacheable(value = "userProfile")
    public UserResponse findProfile(Principal principal) {
        try {
            User user = userRepository.findByEmail(principal.getName()).orElseThrow(UserNotFoundException::new);
            UserResponse response = UserResponse.builder()
                    .role(user.getRole())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .age(user.getAge())
                    .dateOfCreated(user.getDateOfCreated())
                    .orders(user.getOrders())
                    .build();
            return response;

        } catch (UserNotFoundException e) {
            throw new RuntimeException(); //todo exception
        }
    }

    @CacheEvict(value = {"userProfile", "allUsers", "userById"})
    public void updateUser(UserRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow(UserNotFoundException::new);

            User updatedUser = User.builder()
                    .role(request.getRole())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .age(request.getAge())
                    .dateOfCreated(request.getDateOfCreated())
                    .build();

            userRepository.save(updatedUser);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(); //todo exception
        } catch (NullPointerException e) {
            throw new RuntimeException(); //todo НЕ ЗАПОЛНЕНЫ ПОЛЯ
        }
    }

    @Cacheable(value = "allUsers")
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Cacheable(value = "userById")
    public User findUser(Integer id) {
        try {
            return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(); //todo exception
        }
    }


//    @Autowired
//    private MailSender mailSender;
//
//    private final PasswordEncoder passwordEncoder;
//
//    public User createUser(User user) {
//        User newUser = User.builder()
//                .phone(user.getPhone())
//                .email(user.getEmail())
//                .password(passwordEncoder.encode(user.getPassword()))
//                .firstname(user.getFirstname())
//                .lastname(user.getLastname())
//                .age(user.getAge())
//                .build();
//
//        setActivationCode(newUser);
//        userRepository.save(newUser);
//        log.info("Saving new User with phone = {}, email = {}", newUser.getPhone(), user.getEmail());
//
//        sendMessage(newUser);
//
//        return newUser;
//    }
//
//    public boolean activateUser(String code) {
//        User user = userRepository.findByActivationCode(code);
//
//        if (user == null) {
//            return false;
//        }
//        user.setActivationCode(null);
//        user.setActivation(true);
//
//        userRepository.save(user);
//
//        return true;
//    }
//
//
//
//    private void setActivationCode(User user) {
//        user.setActive(true);
//        user.setRole(ROLE_USER);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setActivationCode(randomUUID().toString());
//    }
//
//    private void sendMessage(User user) {
//        String message = String.format(
//                "Hello! Welcome to Jeweler. Please, visit next link: http://localhost:8081/activate/%s",
//                user.getActivationCode());
//
//        mailSender.send(user.getEmail(), "Activation code", message);
//    }
}