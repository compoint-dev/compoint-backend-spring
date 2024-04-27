package com.example.compoint.service;

import com.example.compoint.config.UserDetailsImpl;
import com.example.compoint.entity.RoleEntity;
import com.example.compoint.entity.StandupEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.RoleNotFound;
import com.example.compoint.exception.UserAlreadyExist;
import com.example.compoint.exception.UserNotAuthorized;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.repository.RoleRepo;
import com.example.compoint.repository.StandupRepo;
import com.example.compoint.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final StandupRepo standupRepo;
    private final PasswordEncoder passwordEncoder;

    public UserEntity create(UserEntity user) throws UserAlreadyExist, RoleNotFound {

        //Проверяет на дубликат юзера
        Optional<UserEntity> optionalUser = userRepo.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExist("User already exists");
        }

        //Шифруем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //Проверяет на наличие роли для юзера
        Optional<RoleEntity> optionalRole = roleRepo.findByName("USER");
        if (optionalRole.isEmpty()) {
            throw new RoleNotFound("Role 'USER' not found");
        }
        //Присваиваем роль "USER" для нового юзера
        user.getRoles().add(optionalRole.get());

        return userRepo.save(user);
    }

    public List<UserEntity> getAll() {
        List<UserEntity> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);
        return users;
    }

    public Optional<UserEntity> getById(Long id) throws UserNotFound {
        Optional<UserEntity> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser;
        } else {
            throw new UserNotFound("User not found");
        }
    }

    public Optional<UserEntity> getByUsername(String username) throws UserNotFound {
        Optional<UserEntity> optionalUser = userRepo.findByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser;
        } else {
            throw new UserNotFound("User not found");
        }
    }

    public UserEntity update(Long id, UserEntity user) throws UserNotFound, UserAlreadyExist {
        Optional<UserEntity> optionalUser = userRepo.findById(id);
        if (!optionalUser.isPresent()) {
            throw new UserNotFound("User not found");
        }

        // Получаем найденого юзера
        UserEntity existingUser = optionalUser.get();

        // Проверяем что username свободен и не равен нашему
        Optional<UserEntity> optionalNewInfo = userRepo.findByUsername(user.getUsername());
        if (optionalNewInfo.isPresent() && !optionalNewInfo.get().getId().equals(existingUser.getId())) {
            throw new UserAlreadyExist("Username already taken");
        }

        // Обновляем данные пользователя
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(existingUser);
    }

    public String delete(Long id) throws UserNotFound {
        Optional<UserEntity> optionalUser = userRepo.findById(id);
        if (!optionalUser.isPresent()) {
            throw new UserNotFound("User not found");
        }

        //Получаем юзера
        UserEntity user = optionalUser.get();

        //Ищем все стендапы удаляемого юзера
        List<StandupEntity> standups = standupRepo.findByUserId(user.getId());
            // Удаляем все стендапы удаляемого юзера
            for (StandupEntity standup : standups) {
                standupRepo.deleteById(standup.getId());
            }

        // Удаление самого юзера
        userRepo.deleteById(id);
        return "User with ID " + id + " has been deleted successfully";
    }

    public Map<String, Long> getCurrentUser (Authentication authentication) throws UserNotAuthorized {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotAuthorized("User not authorized");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Collections.singletonMap("userId", userDetails.getId());
    }
}
