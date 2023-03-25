package ru.marilka888.jeweller.service;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.marilka888.jeweller.common.exception.BadRequestException;
import ru.marilka888.jeweller.common.exception.InnerException;
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
    @Counted(value = "jeweller.shop.userService.ERROR.findProfile", recordFailuresOnly = true)
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
            log.warn("В БД не найден пользователь с email: {}", principal.getName());
            throw new UserNotFoundException();
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

    @CacheEvict(value = {"userProfile", "allUsers", "userById"})
    @Counted(value = "jeweller.shop.userService.ERROR.updateUser", recordFailuresOnly = true)
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
            log.warn("В БД не найден пользователь с email: {}", request.getEmail());
            throw new UserNotFoundException();
        } catch (NullPointerException e) {
            log.warn("В запросе на обновление пользователя не заполнены обязательные поля");
            throw new BadRequestException();
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

    @Cacheable(value = "allUsers")
    @Counted(value = "jeweller.shop.userService.ERROR.findAll", recordFailuresOnly = true)
    public Page<User> findAll(Pageable pageable) {
        try {
            return userRepository.findAll(pageable);
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

    @Cacheable(value = "userById")
    @Counted(value = "jeweller.shop.userService.ERROR.findUser", recordFailuresOnly = true)
    public User findUser(Integer id) {
        try {
            return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        } catch (UserNotFoundException e) {
            log.warn("В БД не найден пользователь с id: {}", id);
            throw new UserNotFoundException();
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
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