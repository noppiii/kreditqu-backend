package com.creditqu.account_card_service.service;

import com.creditqu.account_card_service.dto.CardResponseDTO;
import com.creditqu.account_card_service.dto.CreateAccountRequestDTO;
import com.creditqu.account_card_service.dto.CreateAccountResponseDTO;
import com.creditqu.account_card_service.dto.VirtualAccountResponseDTO;

public interface AccountCardService {
    CreateAccountResponseDTO createAccountAndCard(CreateAccountRequestDTO request);
    CardResponseDTO getCardById(Long cardId);
    CardResponseDTO getCardByNumber(String cardNumber);
    CardResponseDTO activateCard(Long cardId, String pin);
    CardResponseDTO blockCard(Long cardId, String reason);
    CardResponseDTO updatePin(Long cardId, String newPin);
    VirtualAccountResponseDTO getVirtualAccount(Long accountId);
}