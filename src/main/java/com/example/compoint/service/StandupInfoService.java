package com.example.compoint.service;

import com.example.compoint.dtos.StandupInfoDTO;
import com.example.compoint.entity.StandupInfoEntity;
import com.example.compoint.mappers.StandupInfoMapper;
import com.example.compoint.repository.StandupInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StandupInfoService {

    @Autowired
    private StandupInfoRepo standupInfoRepo;

    public void change(Long id, String value) throws Exception {
        StandupInfoEntity standupInfo = standupInfoRepo.findById(id).orElseThrow(() -> new Exception("Standup not found"));
        int currentRating = standupInfo.getRating();

        switch (value) {
            case "increase":
                standupInfo.setRating(currentRating + 1);
                break;
            case "decrease":
                standupInfo.setRating(currentRating - 1);
                break;
            default:
                throw new IllegalArgumentException("Invalid operation");
        }

        standupInfoRepo.save(standupInfo);
    }

    public StandupInfoDTO update(Long id, StandupInfoEntity standupInfoEntity) throws Exception {
        StandupInfoEntity standupInfo = standupInfoRepo.findById(id).orElseThrow(() -> new Exception("Standup not found"));

        standupInfo.setRating(standupInfoEntity.getRating());
        standupInfo.setGenre(standupInfoEntity.getGenre());
        standupInfo.setPg(standupInfoEntity.getPg());
        standupInfo.setViews(standupInfoEntity.getViews());
        standupInfo.setImagePath(standupInfoEntity.getImagePath());
        standupInfo.setLanguages(standupInfoEntity.getLanguages());

        return StandupInfoMapper.INSTANCE.standupInfoEntityToStandupInfoDTO(standupInfoRepo.save(standupInfo)) ;
    }
}
