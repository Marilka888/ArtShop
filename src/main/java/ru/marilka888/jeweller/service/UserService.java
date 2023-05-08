package ru.marilka888.jeweller.service;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.marilka888.jeweller.common.exception.BadRequestException;
import ru.marilka888.jeweller.common.exception.InnerException;
import ru.marilka888.jeweller.common.exception.UserNotFoundException;
import ru.marilka888.jeweller.model.User;
import ru.marilka888.jeweller.model.request.UserRequest;
import ru.marilka888.jeweller.model.response.OrderResponse;
import ru.marilka888.jeweller.model.response.UserResponse;
import ru.marilka888.jeweller.repository.UserRepository;

import java.security.Principal;
import java.util.List;

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

            return UserResponse.builder()
                    .role(user.getRole())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .age(user.getAge())
                    .dateOfCreated(user.getDateOfCreated())
                    .orders(getOrders(user))
                    .build();

        } catch (UserNotFoundException e) {
            log.warn("В БД не найден пользователь с email: {}", principal.getName());
            throw new UserNotFoundException();
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка: {}, {}", e.getMessage(), e.getStackTrace());
            throw new InnerException();
        }
    }

    @CacheEvict(value = {"userProfile", "allUsers", "userById"}, allEntries=true)
    @Counted(value = "jeweller.shop.userService.ERROR.updateUser", recordFailuresOnly = true)
    public void updateUser(UserRequest request) {
        try {
            User userDb = userRepository.findByEmail(request.getEmail()).orElseThrow(UserNotFoundException::new);
            User updatedUser = User.builder()
                    .id(userDb.getId())
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
    public List<UserResponse> findAll() {
        try {
            List<User> users = userRepository.findAll();
            return users.stream().map(user -> UserResponse.builder()
                            .role(user.getRole())
                            .firstname(user.getFirstname())
                            .lastname(user.getLastname())
                            .phone(user.getPhone())
                            .email(user.getEmail())
                            .age(user.getAge())
                            .dateOfCreated(user.getDateOfCreated())
                            .build())
                    .toList();

        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

    @Cacheable(value = "userById")
    @Counted(value = "jeweller.shop.userService.ERROR.findUser", recordFailuresOnly = true)
    public UserResponse findUser(Integer id) {
        try {
            User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

            return UserResponse.builder()
                    .role(user.getRole())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .age(user.getAge())
                    .dateOfCreated(user.getDateOfCreated())
                    .orders(getOrders(user))
                    .build();
        } catch (UserNotFoundException e) {
            log.warn("В БД не найден пользователь с id: {}", id);
            throw new UserNotFoundException();
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

    private static List<OrderResponse> getOrders(User user) {
        return user.getOrders()
                .stream()
                .map(order -> OrderResponse.builder()
                        .id(order.getId())
                        .userId(order.getUser().getId())
                        .favour(order.favour)
                        .description(order.getDescription())
                        .stage(order.getStage())
                        .sum(order.getSum())
                        .status(order.isStatus())
                        .dateOfCreated(order.getDateOfCreated())
                        .build())
                .toList();
    }
}