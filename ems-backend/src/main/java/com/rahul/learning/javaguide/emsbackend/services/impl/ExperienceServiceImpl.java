package com.rahul.learning.javaguide.emsbackend.services.impl;

import com.rahul.learning.javaguide.emsbackend.dtos.ExperienceDTO;
import com.rahul.learning.javaguide.emsbackend.entities.Experience;
import com.rahul.learning.javaguide.emsbackend.mappers.ExperienceMapper;
import com.rahul.learning.javaguide.emsbackend.repos.ExperienceRepository;
import com.rahul.learning.javaguide.emsbackend.services.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;

    @Override
    public ExperienceDTO updateExperience(Long experienceId, ExperienceDTO experienceDTO) {
        final Experience experienceFound = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new RuntimeException("Experience with id: " + experienceId + " not found!"));

        experienceFound.setOrgName(experienceDTO.getOrgName());
        experienceFound.setDateOfJoining(experienceDTO.getDateOfJoining());
        experienceFound.setDateOfLeaving(experienceDTO.getDateOfLeaving());

        final Experience savedExperience = experienceRepository.save(experienceFound);
        return ExperienceMapper.mapToExperienceDTO(savedExperience);
    }
}