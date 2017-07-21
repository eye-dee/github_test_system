package com.epam.testsystem.github.web.controller;

import com.epam.testsystem.github.service.contact.ContactService;
import com.epam.testsystem.github.service.user.UserService;
import com.epam.testsystem.github.web.mapper.MapperUi;
import com.epam.testsystem.github.web.model.ContactUI;
import com.epam.testsystem.github.web.model.NewContactUI;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "notifications")
@RequiredArgsConstructor
public class NotificationRestController {
    private final ContactService contactService;

    private final MapperUi mapperUi;
    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ContactUI add(@RequestBody final NewContactUI newContactUI) {
        return mapperUi.mapContact(contactService.add(newContactUI.getUserId(), newContactUI.getType(), newContactUI.getInf()));
    }

    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public List<ContactUI> getAllContacts() {
        return mapperUi.mapContacts(contactService.findByUserId(userService.getCurrentUser().getId()));
    }

    @RequestMapping(value = "{contactId}/update", method = RequestMethod.PUT)
    @Transactional
    public boolean updateContact(@PathVariable(value = "contactId") final long contactId,
                                 @RequestBody final NewContactUI newContactUI) {
        return contactService.updateContact(contactId, newContactUI.getInf());
    }

    @RequestMapping(value = "{contactId}", method = RequestMethod.PUT)
    @Transactional
    public boolean toggle(@PathVariable(value = "contactId") final long contactId,
                          @RequestParam(required = false) final boolean enable) {
        return enable ? contactService.enableContact(contactId) : contactService.disableContact(contactId);
    }
}
