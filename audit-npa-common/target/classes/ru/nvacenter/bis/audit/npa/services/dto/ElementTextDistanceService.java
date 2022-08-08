package ru.nvacenter.bis.audit.npa.services.dto;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * Created by oshesternikova on 20.02.2018.
 * Сервис определения расстояния между единицами НПА
 */
@Service
@PreAuthorize("isAuthenticated()")
public class ElementTextDistanceService {
}
