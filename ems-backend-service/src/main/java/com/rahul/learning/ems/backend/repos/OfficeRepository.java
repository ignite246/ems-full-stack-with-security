package com.rahul.learning.ems.backend.repos;

import com.rahul.learning.ems.backend.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfficeRepository extends JpaRepository<Office, Long> {

    //Use property-traversal as city, state and country are not direct part of Office
    //Spring Data JPA allows you to navigate through relationships in derived query method names.

    List<Office> findByAddressCity(String city);

    List<Office> findByAddressState(String state);

    List<Office> findByAddressCountry(String country);

    List<Office> findByAddressCityAndAddressState(String city, String state);
}