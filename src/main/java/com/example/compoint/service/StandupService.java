package com.example.compoint.service;

import com.example.compoint.config.UserDetailsImpl;
import com.example.compoint.dtos.StandupDTO;
import com.example.compoint.entity.StandupEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.AccessDenied;
import com.example.compoint.exception.StandupAlreadyExist;
import com.example.compoint.exception.StandupNotFound;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.mappers.StandupMapper;
import com.example.compoint.mappers.UserMapper;
import com.example.compoint.repository.StandupRepo;
import com.example.compoint.repository.WatchLaterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StandupService {

    @Autowired
    private StandupRepo standupRepo;

    @Autowired
    private UserService userService;

    public StandupDTO create (StandupEntity standup, Long userId) throws StandupAlreadyExist, UserNotFound {
        Optional<UserEntity> userOptional = userService.getById(userId);
        UserEntity user = userOptional.orElseThrow(() -> new UserNotFound("User not found"));

        standup.setUser(user);

        if (standupRepo.findByName(standup.getName()).isPresent()) {
           throw new StandupAlreadyExist("Standup already exist");
        }
        standup.setRating(0);

        StandupEntity createdStandup = standupRepo.save(standup);
        return StandupMapper.INSTANCE.standupEntityToStandupDTO(createdStandup);
    }

    public List<StandupDTO> getAll () {
        List<StandupEntity> standups = new ArrayList<>();
        standupRepo.findAll().forEach(standups::add);

        return standups.stream()
                .map(StandupMapper.INSTANCE::standupEntityToStandupDTO)
                .collect(Collectors.toList());
    }

    public List<StandupDTO> getAllByUserId(Long userId) {
        List<StandupEntity> standups = new ArrayList<>();
        standupRepo.findByUserId(userId).forEach(standups::add);
        return standups.stream()
                .map(StandupMapper.INSTANCE::standupEntityToStandupDTO)
                .collect(Collectors.toList());
    }

    public StandupDTO getByName(String name) throws StandupNotFound {
        return standupRepo.findByName(name)
                .map(StandupMapper.INSTANCE::standupEntityToStandupDTO)
                .orElseThrow(() -> new StandupNotFound("Standup not found"));
    }

    public StandupDTO getById(Long id) throws StandupNotFound {
        return standupRepo.findById(id)
                .map(StandupMapper.INSTANCE::standupEntityToStandupDTO)
                .orElseThrow(() -> new StandupNotFound("Standup not found"));
    }

    public StandupEntity update(Long id, StandupEntity standup) throws StandupNotFound, StandupAlreadyExist{
    return null;
    }

    public String delete(Long id) throws StandupNotFound, AccessDenied {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long currentUserId = userDetails.getId();


        Optional<StandupEntity> optionalStandup = standupRepo.findById(id);
        if (!optionalStandup.isPresent()) {
            throw new StandupNotFound("Standup not found");
        }

        StandupEntity standup = optionalStandup.get();
        boolean isAdmin = userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));

        if (!(isAdmin || standup.getUser().getId().equals(currentUserId))) {
            throw new AccessDenied("Access Denied");
        }

        standupRepo.deleteById(id);
        return "Standup with ID " + id + " has been deleted successfully";
    }

    public List<StandupDTO> getTopMonth() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        return standupRepo.findTop5ByCreatedAtAfterOrderByRatingDesc(oneMonthAgo).stream()
                .map(StandupMapper.INSTANCE::standupEntityToStandupDTO)
                .collect(Collectors.toList());
    }

    public List<StandupDTO> getTopDay() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);

        return standupRepo.findTop5ByCreatedAtAfterOrderByRatingDesc(oneDayAgo).stream()
                .map(StandupMapper.INSTANCE::standupEntityToStandupDTO)
                .collect(Collectors.toList());
    }

    public List<StandupDTO> getTopYear() {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);

        return standupRepo.findTop5ByCreatedAtAfterOrderByRatingDesc(oneYearAgo).stream()
                .map(StandupMapper.INSTANCE::standupEntityToStandupDTO)
                .collect(Collectors.toList());
    }

}
