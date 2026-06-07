package com.creditqu.credit_scoring_service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class ExternalBureau {

    @Value("${scoring.external.bureau.enabled:false}")
    private boolean externalBureauEnabled;

    @Value("${scoring.external.bureau.mock:true}")
    private boolean mockMode;

    private final Random random = new Random();

    // TODO: Check credit score (SLIK OJK / BI Checking)
    public int checkExternalBureau(String nik) {
        log.info("Checking external bureau for NIK: {}", maskNik(nik));

        if (!externalBureauEnabled || mockMode) {
            int mockScore = 400 + random.nextInt(451);
            log.info("Mock external score: {}", mockScore);
            return mockScore;
        }

        return 650;
    }

    private String maskNik(String nik) {
        if (nik == null || nik.length() < 8) {
            return "****";
        }
        return nik.substring(0, 4) + "****" + nik.substring(nik.length() - 4);
    }
}
