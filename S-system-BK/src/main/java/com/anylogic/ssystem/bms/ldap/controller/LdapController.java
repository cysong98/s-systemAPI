package com.anylogic.ssystem.bms.ldap.controller;

import com.anylogic.ssystem.bms.ldap.service.LdapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ldap")
public class LdapController {

    @Autowired
    private LdapService ldapService;
    @GetMapping("/getAllGroups")
    public ResponseEntity<List<Map<String, Object>>> getAllGroups() {
        List<Map<String, Object>> ldapAllData = ldapService.getAllGroups();
        return new ResponseEntity<>(ldapAllData, HttpStatus.OK);
    }
}
