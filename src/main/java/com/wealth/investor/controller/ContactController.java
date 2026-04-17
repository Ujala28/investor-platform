package com.wealth.investor.controller;

import com.wealth.investor.dto.request.CreateContactRequest;
import com.wealth.investor.entity.Contact;
import com.wealth.investor.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investors/{investorId}/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<Contact> addContact(
            @PathVariable Long investorId,
            @Valid @RequestBody CreateContactRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactService.addContact(investorId, request));
    }

    @GetMapping
    public ResponseEntity<List<Contact>> getContacts(@PathVariable Long investorId) {
        return ResponseEntity.ok(contactService.getContacts(investorId));
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId) {
        contactService.deleteContact(contactId);
        return ResponseEntity.noContent().build();
    }
}