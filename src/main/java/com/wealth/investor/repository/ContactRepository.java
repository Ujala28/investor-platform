package com.wealth.investor.repository;

import com.wealth.investor.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByInvestorId(Long investorId);
}