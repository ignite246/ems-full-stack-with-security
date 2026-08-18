package com.rahul.learning.javaguide.emsbackend.repos;

import com.rahul.learning.javaguide.emsbackend.entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
}
