package com.example.compoint.service;

import com.example.compoint.entity.StandupEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.StandupAlreadyExist;
import com.example.compoint.exception.StandupNotFound;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.model.Standup;
import com.example.compoint.repository.StandupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StandupService {

    @Autowired
    private StandupRepo standupRepo;

    @Autowired
    private UserService userService;

    public StandupEntity create (StandupEntity standup, Long userId) throws StandupAlreadyExist, UserNotFound {
        Optional<UserEntity> userOptional = userService.getById(userId);
        UserEntity user = userOptional.orElseThrow(() -> new UserNotFound("Пользователь не найден"));
        standup.setUser(user);

        if (standupRepo.findByName(standup.getName())!=null) {
           throw new StandupAlreadyExist("Стендап уже существует");
      }
        return standupRepo.save(standup);
    }

    public Standup getOne (String name) throws StandupNotFound {
        StandupEntity standupEntity = standupRepo.findByName(name);
        if (standupEntity != null) {
            return Standup.toModel(standupEntity);
        } else {
            throw new StandupNotFound("Стендап не существует");
        }
    }

    public Standup getOne (Long id) throws StandupNotFound {
        Optional<StandupEntity> optionalStandup = standupRepo.findById(id);
        if (optionalStandup.isPresent()) {
            return Standup.toModel(optionalStandup.get());
        } else {
            throw new StandupNotFound("Стендап не существует");
        }
    }

    public List<Standup> getAll () {
        List<Standup> standups = new ArrayList<>();
        standupRepo.findAll().forEach(standupEntity -> standups.add(Standup.toModel(standupEntity)));
        return standups;
    }
}
