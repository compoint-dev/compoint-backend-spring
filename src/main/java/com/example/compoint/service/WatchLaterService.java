package com.example.compoint.service;


import com.example.compoint.dtos.WatchLaterDTO;
import com.example.compoint.entity.StandupEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.entity.WatchLaterEntity;
import com.example.compoint.exception.AlreadyWatchLater;
import com.example.compoint.exception.StandupNotFound;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.mappers.WatchLaterMapper;
import com.example.compoint.repository.StandupRepo;
import com.example.compoint.repository.UserRepo;
import com.example.compoint.repository.WatchLaterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WatchLaterService {

    @Autowired
    private WatchLaterRepo watchLaterRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StandupRepo standupRepo;

    public WatchLaterDTO addToWatchLater(Long standupId, Long userId) throws AlreadyWatchLater, UserNotFound, StandupNotFound {
        if (watchLaterRepo.existsByUserIdAndStandupId(userId, standupId)) {
            throw new AlreadyWatchLater("This standup is already in the watch later list.");
        }

        UserEntity user = userRepo.findById(userId).orElseThrow(() -> new UserNotFound("User not found"));
        StandupEntity standup = standupRepo.findById(standupId).orElseThrow(() -> new StandupNotFound("Standup not found"));

        WatchLaterEntity watchLater = new WatchLaterEntity();
        watchLater.setUser(user);
        watchLater.setStandup(standup);

        watchLater = watchLaterRepo.save(watchLater);

        return WatchLaterMapper.INSTANCE.watchLaterEntityToWatchLaterDTO(watchLater);
    }

    //TODO:Проверить на существующий watchlater
    public String removeFromWatchLater(Long standupId, Long userId) throws UserNotFound, StandupNotFound {
        UserEntity user = userRepo.findById(userId).orElseThrow(() -> new UserNotFound("User not found"));
        StandupEntity standup = standupRepo.findById(standupId).orElseThrow(() -> new StandupNotFound("Standup not found"));

        WatchLaterEntity watchLater = watchLaterRepo.findByUserIdAndStandupId(userId, standupId);
        watchLaterRepo.delete(watchLater);

        return "Deleted watchlater";
    }
}
