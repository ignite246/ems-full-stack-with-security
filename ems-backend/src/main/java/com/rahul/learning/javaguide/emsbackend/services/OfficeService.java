package com.rahul.learning.javaguide.emsbackend.services;

import com.rahul.learning.javaguide.emsbackend.dtos.OfficeDTO;

import java.util.List;

public interface OfficeService {

    OfficeDTO getOfficeById(Long id);

    List<OfficeDTO> getAllOffices();

    OfficeDTO createOffice(OfficeDTO officeDTO);

    OfficeDTO updateOffice(OfficeDTO officeDTO);

    String deleteOffice(Long officeId);
}