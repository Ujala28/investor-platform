package com.wealth.investor.service;

import com.wealth.investor.dto.request.CreateContactRequest;
import com.wealth.investor.entity.Contact;
import com.wealth.investor.entity.Investor;
import com.wealth.investor.entity.enums.InvestorType;
import com.wealth.investor.entity.enums.RelationshipType;
import com.wealth.investor.repository.ContactRepository;
import com.wealth.investor.repository.InvestorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;
    private final InvestorRepository investorRepository;

    public Contact addContact(Long investorId, CreateContactRequest request) {

        log.info("Adding contact for investorId: {}", investorId);

        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new EntityNotFoundException("Investor not found"));

        // business rule
        if (investor.getInvestorType() == InvestorType.CORPORATE &&
                request.getRelationshipType() == RelationshipType.SPOUSE) {
            throw new RuntimeException("Corporate investor cannot have spouse");
        }

        Contact contact = new Contact();
        contact.setName(request.getName());
        contact.setRelationshipType(request.getRelationshipType());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setInvestor(investor);

        Contact saved = contactRepository.save(contact);

        log.info("Contact added successfully: id={}, investorId={}", saved.getId(), investorId);

        return saved;
    }

    public List<Contact> getContacts(Long investorId) {

        log.info("Fetching contacts for investorId: {}", investorId);

        if (!investorRepository.existsById(investorId)) {
            throw new EntityNotFoundException("Investor not found");
        }

        List<Contact> contacts = contactRepository.findByInvestorId(investorId);

        log.info("Fetched {} contacts for investorId={}", contacts.size(), investorId);

        return contacts;
    }

    public void deleteContact(Long contactId) {

        log.info("Deleting contact with id: {}", contactId);

        if (!contactRepository.existsById(contactId)) {
            throw new EntityNotFoundException("Contact not found");
        }

        contactRepository.deleteById(contactId);

        log.info("Contact deleted successfully: id={}", contactId);
    }
}