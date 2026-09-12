package com.rahul.learning.ems.backend.services;

import com.rahul.learning.ems.backend.dtos.ExperienceDTO;

public interface ExperienceService {
    ExperienceDTO updateExperience(Long experienceId, ExperienceDTO experienceDTO);
}
