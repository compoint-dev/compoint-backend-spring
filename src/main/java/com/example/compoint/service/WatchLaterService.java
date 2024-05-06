package com.example.compoint.service;


import com.example.compoint.entity.StandupEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.entity.WatchLaterEntity;
import com.example.compoint.repository.StandupRepo;
import com.example.compoint.repository.UserRepo;
import com.example.compoint.repository.WatchLaterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WatchLaterService {
    @Autowired
    private WatchLaterRepo watchLaterRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StandupRepo standupRepo;

    public WatchLaterEntity addToWatchLater(Long standupId, Long userId) throws Exception {
        if (watchLaterRepo.existsByUserIdAndStandupId(userId, standupId)) {
            throw new Exception("This standup is already in the watch later list.");
        }

        UserEntity user = userRepo.findById(userId).orElseThrow(() -> new Exception("User not found"));
        StandupEntity standup = standupRepo.findById(standupId).orElseThrow(() -> new Exception("Standup not found"));

        WatchLaterEntity watchLater = new WatchLaterEntity();
        watchLater.setUser(user);
        watchLater.setStandup(standup);

        return watchLaterRepo.save(watchLater);
    }

    //TODO:Проверить на существующий watchlater
    public String removeFromWatchLater(Long standupId, Long userId) throws Exception {
        UserEntity user = userRepo.findById(userId).orElseThrow(() -> new Exception("User not found"));
        StandupEntity standup = standupRepo.findById(standupId).orElseThrow(() -> new Exception("Standup not found"));

        WatchLaterEntity watchLater = watchLaterRepo.findByUserIdAndStandupId(userId, standupId);
        watchLaterRepo.delete(watchLater);

        return "Deleted watchlater";
    }
}
