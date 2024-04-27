package com.example.compoint.service;

import com.example.compoint.entity.LanguageEntity;
import com.example.compoint.repository.LanguageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepo languageRepo;

    public Set<LanguageEntity> getLanguagesByIds(Set<Long> languageIds) {
        Iterable<LanguageEntity> iterable = languageRepo.findAllById(languageIds);
        Set<LanguageEntity> set = new HashSet<>();
        iterable.forEach(set::add);
        return set;
    }
}
