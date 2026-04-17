package com.wealth.investor.service;

import com.wealth.investor.dto.request.CreateInvestorRequest;
import com.wealth.investor.dto.request.UpdateInvestorRequest;
import com.wealth.investor.dto.response.InvestorResponse;
import com.wealth.investor.dto.response.PaginatedResponse;
import com.wealth.investor.entity.Investor;
import com.wealth.investor.entity.enums.InvestorType;
import com.wealth.investor.entity.enums.KycStatus;
import com.wealth.investor.repository.InvestorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class InvestorService {

    private final InvestorRepository investorRepository;
    private static final Logger log = LoggerFactory.getLogger(InvestorService.class);

    //CREATE
    public InvestorResponse createInvestor(CreateInvestorRequest request) {

        log.info("Creating investor with email: {}", request.getEmail());

        if (investorRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        int age = Period.between(request.getDateOfBirth(), LocalDate.now()).getYears();
        if (age < 18) {
            throw new IllegalArgumentException("Investor must be at least 18 years old");
        }

        Investor investor = new Investor();
        investor.setFullName(request.getFullName());
        investor.setEmail(request.getEmail());
        investor.setPhone(request.getPhone());
        investor.setDateOfBirth(request.getDateOfBirth());
        investor.setInvestorType(request.getInvestorType());
        investor.setPanNumber(request.getPanNumber());
        investor.setKycStatus(request.getKycStatus());

        Investor saved = investorRepository.save(investor);

        log.info("Investor created successfully: id={}", saved.getId());

        return mapToResponse(saved);
    }

    //GET (CACHEABLE)
    @Cacheable(value = "investors", key = "#id")
    public InvestorResponse getInvestor(Long id) {

        log.info("Fetching investor from DB: {}", id);

        Investor investor = getInvestorEntity(id);

        log.info("Investor fetched successfully: id={}", investor.getId());

        return mapToResponse(investor);
    }

    //UPDATE (CACHE PUT)
    @CachePut(value = "investors", key = "#id")
    public InvestorResponse updateInvestor(Long id, UpdateInvestorRequest request) {

        log.info("Updating investor with id: {}", id);

        Investor investor = getInvestorEntity(id);

        if (request.getFullName() != null) investor.setFullName(request.getFullName());

        if (request.getEmail() != null) {
            if (investorRepository.existsByEmail(request.getEmail())
                    && !investor.getEmail().equals(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            investor.setEmail(request.getEmail());
        }

        if (request.getPhone() != null) investor.setPhone(request.getPhone());

        if (request.getDateOfBirth() != null) {
            int age = Period.between(request.getDateOfBirth(), LocalDate.now()).getYears();
            if (age < 18) {
                throw new IllegalArgumentException("Investor must be at least 18 years old");
            }
            investor.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getInvestorType() != null) investor.setInvestorType(request.getInvestorType());

        if (request.getPanNumber() != null) investor.setPanNumber(request.getPanNumber());

        if (request.getKycStatus() != null) {
            validateKycTransition(investor.getKycStatus(), request.getKycStatus());
            investor.setKycStatus(request.getKycStatus());
        }

        Investor updated = investorRepository.save(investor);

        log.info("Investor updated successfully: id={}", updated.getId());

        return mapToResponse(updated);
    }

    //DELETE (CACHE EVICT)
    @CacheEvict(value = "investors", key = "#id")
    public void deleteInvestor(Long id) {

        log.info("Deleting investor with id: {}", id);

        if (!investorRepository.existsById(id)) {
            throw new EntityNotFoundException("Investor not found");
        }

        investorRepository.deleteById(id);

        log.info("Investor deleted successfully: id={}", id);
    }

    // HELPER METHODS

    private Investor getInvestorEntity(Long id) {
        return investorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Investor not found"));
    }

    private void validateKycTransition(KycStatus current, KycStatus next) {

        //REJECTED → VERIFIED not allowed
        if (current == KycStatus.REJECTED && next == KycStatus.VERIFIED) {
            throw new IllegalStateException("Cannot move from REJECTED to VERIFIED");
        }

        //VERIFIED → PENDING not allowed
        if (current == KycStatus.VERIFIED && next == KycStatus.PENDING) {
            throw new IllegalStateException("Invalid transition from VERIFIED to PENDING");
        }

        // Allowed transitions:
        // PENDING → VERIFIED
        // VERIFIED → REJECTED
    }

    private InvestorResponse mapToResponse(Investor investor) {
        return InvestorResponse.builder()
                .id(investor.getId())
                .fullName(investor.getFullName())
                .email(investor.getEmail())
                .phone(investor.getPhone())
                .dateOfBirth(investor.getDateOfBirth())
                .investorType(investor.getInvestorType())
                .panNumber(investor.getPanNumber())
                .kycStatus(investor.getKycStatus())
                .build();
    }

    // PAGINATION + FILTER
    public PaginatedResponse<InvestorResponse> getInvestors(
            InvestorType investorType,
            KycStatus kycStatus,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Investor> investors;

        if (investorType != null && kycStatus != null) {
            investors = investorRepository.findByInvestorTypeAndKycStatus(investorType, kycStatus, pageable);
        } else if (investorType != null) {
            investors = investorRepository.findAllByInvestorType(investorType, pageable);
        } else if (kycStatus != null) {
            investors = investorRepository.findAllByKycStatus(kycStatus, pageable);
        } else {
            investors = investorRepository.findAll(pageable);
        }

        return PaginatedResponse.<InvestorResponse>builder()
                .data(investors.getContent().stream().map(this::mapToResponse).toList())
                .page(investors.getNumber())
                .size(investors.getSize())
                .totalElements(investors.getTotalElements())
                .totalPages(investors.getTotalPages())
                .build();
    }
}