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

import java.security.Principal;
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
        UserEntity user = userOptional.orElseThrow(() -> new UserNotFound("User not found"));
        standup.setUser(user);

        if (standupRepo.findByName(standup.getName())!=null) {
           throw new StandupAlreadyExist("Standup already exist");
        }
        standup.setRating(0);
        return standupRepo.save(standup);
    }

    public List<StandupEntity> getAll () {
        List<StandupEntity> standups = new ArrayList<>();
        standupRepo.findAll().forEach(standups::add);
        return standups;
    }

    public Standup getByName (String name) throws StandupNotFound {
        StandupEntity standupEntity = standupRepo.findByName(name);
        if (standupEntity != null) {
            return Standup.toModel(standupEntity);
        } else {
            throw new StandupNotFound("Standup not found");
        }
    }

    public Standup getById (Long id) throws StandupNotFound {
        Optional<StandupEntity> optionalStandup = standupRepo.findById(id);
        if (optionalStandup.isPresent()) {
            return Standup.toModel(optionalStandup.get());
        } else {
            throw new StandupNotFound("Standup not found");
        }
    }

    public String delete(Long id) throws StandupNotFound {
        Optional<StandupEntity> optionalStandup = standupRepo.findById(id);
        if (!optionalStandup.isPresent()) {
            throw new StandupNotFound("Standup not found");
        }

        standupRepo.deleteById(id);
        return "Standup with ID " + id + " has been deleted successfully";
    }

}
