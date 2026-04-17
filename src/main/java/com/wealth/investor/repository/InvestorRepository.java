package com.wealth.investor.repository;

import com.wealth.investor.entity.Investor;
import com.wealth.investor.entity.enums.InvestorType;
import com.wealth.investor.entity.enums.KycStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvestorRepository extends JpaRepository<Investor, Long> {

    Optional<Investor> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Investor> findAll(Pageable pageable);

    Page<Investor> findAllByInvestorType(InvestorType investorType, Pageable pageable);

    Page<Investor> findAllByKycStatus(KycStatus kycStatus, Pageable pageable);

    Page<Investor> findByInvestorTypeAndKycStatus(
            InvestorType investorType,
            KycStatus kycStatus,
            Pageable pageable
    );
}