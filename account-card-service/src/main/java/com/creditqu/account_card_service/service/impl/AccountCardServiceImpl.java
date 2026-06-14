package com.creditqu.account_card_service.service.impl;

import com.creditqu.account_card_service.dto.CardResponseDTO;
import com.creditqu.account_card_service.dto.CreateAccountRequestDTO;
import com.creditqu.account_card_service.dto.CreateAccountResponseDTO;
import com.creditqu.account_card_service.dto.VirtualAccountResponseDTO;
import com.creditqu.account_card_service.entity.Card;
import com.creditqu.account_card_service.entity.CardAccount;
import com.creditqu.account_card_service.entity.VirtualAccount;
import com.creditqu.account_card_service.repository.CardAccountRepository;
import com.creditqu.account_card_service.repository.CardRepository;
import com.creditqu.account_card_service.repository.VirtualAccountRepository;
import com.creditqu.account_card_service.service.AccountCardService;
import com.creditqu.account_card_service.util.CardNumberGeneratorUtil;
import com.creditqu.account_card_service.util.CryptoUtil;
import com.creditqu.account_card_service.util.ExpiryDateGenerator;
import com.creditqu.account_card_service.util.VirtualAccountGeneratorUtil;
import com.creditqu.common_module.constant.AccountStatus;
import com.creditqu.common_module.constant.CardStatus;
import com.creditqu.common_module.constant.VirtualAccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountCardServiceImpl implements AccountCardService {

    private final CardAccountRepository cardAccountRepository;
    private final CardRepository cardRepository;
    private final VirtualAccountRepository virtualAccountRepository;
    private final CardNumberGeneratorUtil cardNumberGenerator;
    private final VirtualAccountGeneratorUtil vaGenerator;
    private final CryptoUtil cryptoUtil;
    private final ExpiryDateGenerator expiryDateGenerator;

    private static final String ACCOUNT_NUMBER_PREFIX = "ACC";

    @Override
    @Transactional
    public CreateAccountResponseDTO createAccountAndCard(CreateAccountRequestDTO request) {
        log.info("Creating account and card for customer: {}", request.getCustomerId());

        String accountNumber = generateAccountNumber(request.getCustomerId());
        log.debug("Generated account number: {}", accountNumber);

        BigDecimal availableLimit = request.getApprovedLimit();

        CardAccount account = CardAccount.builder()
                .accountNumber(accountNumber)
                .customerId(request.getCustomerId())
                .applicationId(request.getApplicationId())
                .productCode(request.getProductCode())
                .currentLimit(request.getApprovedLimit())
                .outstandingBalance(BigDecimal.ZERO)
                .availableLimit(availableLimit)
                .billingCycleDay(1)
                .dueDateOffset(14)
                .status(AccountStatus.ACTIVE)
                .build();

        CardAccount savedAccount = cardAccountRepository.save(account);
        log.info("Card account created with ID: {}", savedAccount.getId());

        String cardNumber = cardNumberGenerator.generateUniqueCardNumber(request.getProductCode());
        String cvv = cryptoUtil.generateRandomCvv();
        String cvvHash = cryptoUtil.hashCvv(cvv);

        ExpiryDateGenerator.ExpiryDate expiryDate = expiryDateGenerator.generateExpiryDate();

        Card card = Card.builder()
                .cardNumber(cardNumber)
                .account(savedAccount)
                .cardholderName(request.getCustomerName())
                .expiryMonth(expiryDate.getMonth())
                .expiryYear(expiryDate.getYear())
                .cvvHash(cvvHash)
                .isPrimary(true)
                .status(CardStatus.INACTIVE)
                .issuedBy("SYSTEM")
                .build();

        Card savedCard = cardRepository.save(card);
        log.info("Card created with ID: {}, number: {}", savedCard.getId(), maskCardNumber(cardNumber));

        String bankCode = vaGenerator.getRandomBankCode();
        String vaNumber = vaGenerator.generateVirtualAccountNumber(
                request.getCustomerId(), savedAccount.getId(), bankCode);

        VirtualAccount virtualAccount = VirtualAccount.builder()
                .account(savedAccount)
                .virtualAccountNumber(vaNumber)
                .bankCode(bankCode)
                .status(VirtualAccountStatus.ACTIVE)
                .expiredAt(vaGenerator.calculateExpiryDate())
                .build();

        VirtualAccount savedVA = virtualAccountRepository.save(virtualAccount);
        log.info("Virtual account created: {} for bank: {}", savedVA.getVirtualAccountNumber(), bankCode);

        return CreateAccountResponseDTO.builder()
                .accountId(savedAccount.getId())
                .accountNumber(savedAccount.getAccountNumber())
                .cardId(savedCard.getId())
                .cardNumber(savedCard.getCardNumber())
                .cardNumberMasked(maskCardNumber(savedCard.getCardNumber()))
                .expiryMonth(savedCard.getExpiryMonth())
                .expiryYear(savedCard.getExpiryYear())
                .currentLimit(savedAccount.getCurrentLimit())
                .virtualAccountNumber(savedVA.getVirtualAccountNumber())
                .bankCode(bankCode)
                .status(savedCard.getStatus().name())
                .build();
    }

    @Override
    public CardResponseDTO getCardById(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found: " + cardId));
        return mapToCardResponse(card);
    }

    @Override
    public CardResponseDTO getCardByNumber(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found: " + maskCardNumber(cardNumber)));
        return mapToCardResponse(card);
    }

    @Override
    @Transactional
    public CardResponseDTO activateCard(Long cardId, String pin) {
        log.info("Activating card: {}", cardId);

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found: " + cardId));

        if (card.getStatus() != CardStatus.INACTIVE) {
            throw new RuntimeException("Card cannot be activated. Current status: " + card.getStatus());
        }

        String pinHash = cryptoUtil.hashPin(pin);
        card.setPinHash(pinHash);
        card.setStatus(CardStatus.ACTIVE);
        card.setActivatedAt(LocalDateTime.now());

        Card savedCard = cardRepository.save(card);
        log.info("Card activated: {}", maskCardNumber(savedCard.getCardNumber()));

        return mapToCardResponse(savedCard);
    }

    @Override
    @Transactional
    public CardResponseDTO blockCard(Long cardId, String reason) {
        log.info("Blocking card: {} reason: {}", cardId, reason);

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found: " + cardId));

        card.setStatus(CardStatus.BLOCKED);
        card.setBlockedAt(LocalDateTime.now());
        card.setBlockReason(reason);

        Card savedCard = cardRepository.save(card);
        log.info("Card blocked: {}", maskCardNumber(savedCard.getCardNumber()));

        if (card.getIsPrimary()) {
            CardAccount account = card.getAccount();
            account.setStatus(AccountStatus.BLOCKED);
            cardAccountRepository.save(account);
            log.info("Account {} also blocked", account.getAccountNumber());
        }

        return mapToCardResponse(savedCard);
    }

    @Override
    @Transactional
    public CardResponseDTO updatePin(Long cardId, String newPin) {
        log.info("Updating PIN for card: {}", cardId);

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found: " + cardId));

        String newPinHash = cryptoUtil.hashPin(newPin);
        card.setPinHash(newPinHash);

        Card savedCard = cardRepository.save(card);
        log.info("PIN updated for card: {}", maskCardNumber(savedCard.getCardNumber()));

        return mapToCardResponse(savedCard);
    }

    @Override
    public VirtualAccountResponseDTO getVirtualAccount(Long accountId) {
        VirtualAccount va = virtualAccountRepository.findByAccountId(accountId)
                .stream()
                .filter(v -> v.getStatus() == VirtualAccountStatus.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No active VA found for account: " + accountId));

        return VirtualAccountResponseDTO.builder()
                .id(va.getId())
                .virtualAccountNumber(va.getVirtualAccountNumber())
                .bankCode(va.getBankCode())
                .status(va.getStatus().name())
                .expiredAt(va.getExpiredAt())
                .build();
    }

    private String generateAccountNumber(Long customerId) {
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(7);
        String customerPart = String.format("%08d", customerId);
        return ACCOUNT_NUMBER_PREFIX + timestamp + customerPart;
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 8) {
            return "****";
        }
        return cardNumber.substring(0, 4) + "****" + cardNumber.substring(cardNumber.length() - 4);
    }

    private CardResponseDTO mapToCardResponse(Card card) {
        return CardResponseDTO.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .cardNumberMasked(maskCardNumber(card.getCardNumber()))
                .cardholderName(card.getCardholderName())
                .expiryMonth(card.getExpiryMonth())
                .expiryYear(card.getExpiryYear())
                .status(card.getStatus().name())
                .isPrimary(card.getIsPrimary())
                .issuedAt(card.getIssuedAt())
                .activatedAt(card.getActivatedAt())
                .build();
    }
}
