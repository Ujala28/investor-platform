package com.wealth.investor.controller;

import com.wealth.investor.dto.request.CreateInvestorRequest;
import com.wealth.investor.dto.request.UpdateInvestorRequest;
import com.wealth.investor.dto.response.InvestorResponse;
import com.wealth.investor.dto.response.PaginatedResponse;
import com.wealth.investor.entity.enums.InvestorType;
import com.wealth.investor.entity.enums.KycStatus;
import com.wealth.investor.service.InvestorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/investors")
@RequiredArgsConstructor
public class InvestorController {

    private final InvestorService investorService;
    private static final Logger log = LoggerFactory.getLogger(InvestorController.class);

    @PostMapping
    public ResponseEntity<InvestorResponse> createInvestor(
            @Valid @RequestBody CreateInvestorRequest request) throws BadRequestException {
        log.info("Received create investor request");
        InvestorResponse response = investorService.createInvestor(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestorResponse> getInvestor(@PathVariable Long id) {
        return ResponseEntity.ok(investorService.getInvestor(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InvestorResponse> updateInvestor(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInvestorRequest request) {

        return ResponseEntity.ok(investorService.updateInvestor(id, request));
    }
    @GetMapping
    public ResponseEntity<PaginatedResponse<InvestorResponse>> getInvestors(
            @RequestParam(required = false) InvestorType investorType,
            @RequestParam(required = false) KycStatus kycStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                investorService.getInvestors(investorType, kycStatus, page, size)
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestor(@PathVariable Long id) {
        log.info("Deleting investor with id: {}", id);
        investorService.deleteInvestor(id);
        return ResponseEntity.noContent().build();
    }
}