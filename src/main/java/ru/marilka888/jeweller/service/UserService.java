package ru.marilka888.jeweller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.marilka888.jeweller.model.User;
import ru.marilka888.jeweller.repository.UserRepository;

import java.security.Principal;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findUser(Long id) {
        try {
            return new User();
            //return userRepository.findById(id).get();
        } catch (NullPointerException e) {
            throw new RuntimeException(); //todo exception
        }
    }

    public User findProfile(Principal principal) {
        try {
            return userRepository.findByEmail(principal.getName()).get();
        } catch (NullPointerException e) {
            throw new RuntimeException(); //todo exception
        }
    }

    public void updateUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
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