package com.example.compoint.service;

import com.example.compoint.config.UserDetailsImpl;
import com.example.compoint.dtos.StandupRequest;
import com.example.compoint.dtos.StandupResponse;
import com.example.compoint.entity.LanguageEntity;
import com.example.compoint.entity.StandupEntity;
import com.example.compoint.entity.StandupInfoEntity;
import com.example.compoint.entity.UserEntity;
import com.example.compoint.exception.AccessDenied;
import com.example.compoint.exception.StandupAlreadyExist;
import com.example.compoint.exception.StandupNotFound;
import com.example.compoint.exception.UserNotFound;
import com.example.compoint.mappers.StandupMapper;
import com.example.compoint.repository.StandupInfoRepo;
import com.example.compoint.repository.StandupRepo;
import com.example.compoint.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StandupService {

    @Autowired
    private StandupRepo standupRepo;

    @Autowired
    private StandupInfoRepo standupInfoRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LanguageService languageService;

    public StandupResponse create(StandupRequest standup, Long userId) throws StandupAlreadyExist, UserNotFound {
        UserEntity user = userRepo.findById(userId).orElseThrow(() -> new UserNotFound("User not found"));

        if (standupRepo.existsByName(standup.getName())) {
            throw new StandupAlreadyExist("Standup already exists");
        }

        //Fill base info
        StandupEntity standupEntity = new StandupEntity();
        standupEntity.setName(standup.getName());
        standupEntity.setDescription(standup.getDescription());
        standupEntity.setPrice(standup.getPrice());

        //Fill additional info
        StandupInfoEntity standupInfo = new StandupInfoEntity();
        Set<LanguageEntity> languages = languageService.getLanguagesByIds(standupInfo.getLanguages().stream().map(LanguageEntity::getId).collect(Collectors.toSet()));
        standupInfo.setRating(0);
        standupInfo.setLanguages(languages);
        standupInfo.setStandup(standupEntity);

        standupEntity.setUser(user);
        standupEntity.setStandupInfo(standupInfo);

        return StandupMapper.INSTANCE.standupEntityToStandupDTO(standupRepo.save(standupEntity));
    }

    public List<StandupResponse> getAll() {
        List<StandupEntity> standups = new ArrayList<>();
        standupRepo.findAll().forEach(standups::add);

        return standups.stream()
                .map(StandupMapper.INSTANCE::standupEntityToStandupDTO)
                .collect(Collectors.toList());
    }

    public List<StandupResponse> getAllByUserId(Long userId) throws UserNotFound {
        UserEntity user = userRepo.findById(userId).orElseThrow(() -> new UserNotFound("User not found"));

        List<StandupEntity> standups = new ArrayList<>();
        standupRepo.findByUserId(userId).forEach(standups::add);
        return standups.stream()
                .map(StandupMapper.INSTANCE::standupEntityToStandupDTO)
                .collect(Collectors.toList());
    }

    public StandupResponse getByName(String name) throws StandupNotFound {
        return standupRepo.findByName(name)
                .map(StandupMapper.INSTANCE::standupEntityToStandupDTO)
                .orElseThrow(() -> new StandupNotFound("Standup not found"));
    }

    public StandupResponse getById(Long id) throws StandupNotFound {
        return standupRepo.findById(id)
                .map(StandupMapper.INSTANCE::standupEntityToStandupDTO)
                .orElseThrow(() -> new StandupNotFound("Standup not found"));
    }

    //TODO: Доделать
    public StandupEntity update(Long id, StandupEntity standup) throws StandupNotFound, StandupAlreadyExist {
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

//    public List<StandupDTO> getTopMonth() {
//        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
//        List<StandupInfoEntity> v = standupInfoRepo.findTop5ByCreatedAtAfterOrderByRatingDesc(oneMonthAgo);
//
//
//        return v.stream()
//                .map(StandupInfoMapper.INSTANCE::standupInfoEntityToStandupInfoDTO)
//                .collect(Collectors.toList());
//    }
//
//    public List<StandupInfoDTO> getTopDay() {
//        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
//
//        return standupInfoRepo.findTop5ByCreatedAtAfterOrderByRatingDesc(oneDayAgo).stream()
//                .map(StandupInfoMapper.INSTANCE::standupInfoEntityToStandupInfoDTO)
//                .collect(Collectors.toList());
//    }
//
//    public List<StandupInfoDTO> getTopYear() {
//        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
//
//        return standupInfoRepo.findTop5ByCreatedAtAfterOrderByRatingDesc(oneYearAgo).stream()
//                .map(StandupInfoMapper.INSTANCE::standupInfoEntityToStandupInfoDTO)
//                .collect(Collectors.toList());
//    }

}
