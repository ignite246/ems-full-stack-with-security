package com.rahul.learning.ems.backend.repos;

import com.rahul.learning.ems.backend.entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
}
