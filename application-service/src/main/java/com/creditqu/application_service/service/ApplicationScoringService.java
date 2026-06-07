package com.creditqu.application_service.service;

import java.math.BigDecimal;

public interface ApplicationScoringService {

    void processScoring(Long applicationId, Long customerId, BigDecimal monthlyIncome, Integer tenureYears, String employmentType, String nik, Integer age);

}
