package com.creditqu.account_card_service.controller;

import com.creditqu.account_card_service.dto.CardResponseDTO;
import com.creditqu.account_card_service.dto.CreateAccountRequestDTO;
import com.creditqu.account_card_service.dto.CreateAccountResponseDTO;
import com.creditqu.account_card_service.dto.VirtualAccountResponseDTO;
import com.creditqu.account_card_service.service.AccountCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountCardController {

    private final AccountCardService accountCardService;

    @PostMapping("/create")
    public ResponseEntity<CreateAccountResponseDTO> createAccountAndCard(@Valid @RequestBody CreateAccountRequestDTO request) {
        CreateAccountResponseDTO response = accountCardService.createAccountAndCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/cards/{cardId}")
    public ResponseEntity<CardResponseDTO> getCardById(@PathVariable Long cardId) {
        return ResponseEntity.ok(accountCardService.getCardById(cardId));
    }

    @GetMapping("/cards/number/{cardNumber}")
    public ResponseEntity<CardResponseDTO> getCardByNumber(@PathVariable String cardNumber) {
        return ResponseEntity.ok(accountCardService.getCardByNumber(cardNumber));
    }

    @PostMapping("/cards/{cardId}/activate")
    public ResponseEntity<CardResponseDTO> activateCard(@PathVariable Long cardId, @RequestParam String pin) {
        CardResponseDTO response = accountCardService.activateCard(cardId, pin);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cards/{cardId}/block")
    public ResponseEntity<CardResponseDTO> blockCard(@PathVariable Long cardId, @RequestParam String reason) {
        CardResponseDTO response = accountCardService.blockCard(cardId, reason);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cards/{cardId}/pin")
    public ResponseEntity<CardResponseDTO> updatePin(@PathVariable Long cardId, @RequestParam String newPin) {
        CardResponseDTO response = accountCardService.updatePin(cardId, newPin);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}/virtual-account")
    public ResponseEntity<VirtualAccountResponseDTO> getVirtualAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountCardService.getVirtualAccount(accountId));
    }
}
