package com.rahul.learning.ems.backend.services;

import com.rahul.learning.ems.backend.dtos.OfficeDTO;

import java.util.List;

public interface OfficeService {

    OfficeDTO getOfficeById(Long id);

    List<OfficeDTO> getAllOffices();

    OfficeDTO createOffice(OfficeDTO officeDTO);

    OfficeDTO updateOffice(OfficeDTO officeDTO);

    String deleteOffice(Long officeId);
}