package com.anylogic.ssystem.bms.ldap.service;

import com.anylogic.ssystem.bms.com.mapper.ComOrganizationinfoMapper;
import com.anylogic.ssystem.bms.com.model.InsertOrganizationBIAReqVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.*;
import javax.naming.directory.Attribute;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service("LdapService")
public class LdapServiceImpl implements LdapService {

    @Autowired
    private ComOrganizationinfoMapper organizationinfoMapper;

    @Autowired
    private LdapTemplate ldapTemplate;

    @Value("${baseDN}")
    private String baseDN;

    @Value("${filter}")
    private String filter;

    @Transactional
    @Override
    public List<Map<String, Object>> getAllGroups() {
        String[] organizationAttributes = {"ou","ouCode","ucOrgFullName", "parentOuCode", "ucChiefTitle","ouLevel"};
        return searchAndSave(organizationAttributes);
    }

    private List<Map<String, Object>> searchAndSave(String... attributeNames) {
        try {
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            return ldapTemplate.search(baseDN, filter, searchControls, (AttributesMapper<Map<String, Object>>) attributes -> {
                Map<String, Object> organizationAttributes = new HashMap<>();
                for (String attributeName : attributeNames) {
                    Attribute attribute = attributes.get(attributeName);
                    if (attribute != null) {
                        organizationAttributes.put(attributeName, attribute.get().toString());
                    }
                }
//                saveLDAPDept(organizationAttributes);
                return organizationAttributes;
            });
        } catch (NamingException e) {
            log.error("LDAP NamingException", e);
            throw new RuntimeException("LDAP NamingException", e);
        } catch (DataAccessException e) {
            log.error("LDAP DataAccessException", e);
            throw new RuntimeException("LDAP DataAccessException", e);
        } catch (Exception e) {
            log.error("LDAP Exception", e);
            throw new RuntimeException("LDAP Exception", e);
        }
    }

    private void saveLDAPDept(Map<String, Object> organizationAttributes) {
        try {
            if (organizationAttributes.isEmpty()) {
                return;
            }
            String orgid = getStringOrNull(organizationAttributes, "ouCode");
            String orgname = getStringOrNull(organizationAttributes, "ucOrgFullName");
            String parentorgid = "0000000".equals(orgid) ? null : getStringOrNull(organizationAttributes, "parentOuCode");
            String toporgid = "0000000".equals(orgid) ? null : "0000000";
            String chiefposition = getStringOrNull(organizationAttributes, "ucChiefTitle");
            String ouLevel = getStringOrNull(organizationAttributes, "ouLevel");
            int orgdepth = "0000000".equals(orgid) ? 1 : Integer.parseInt(ouLevel)+1;

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("orgid", orgid);
            int existingRecordCount = organizationinfoMapper.checkLDAPDeptid(paramMap);

            if (existingRecordCount == 0) {
                InsertOrganizationBIAReqVO build = InsertOrganizationBIAReqVO.builder()
                        .orgid(orgid)
                        .orgname(orgname)
                        .parentorgid(parentorgid)
                        .toporgid(toporgid)
                        .orgtype("1")
                        .chiefposition(chiefposition)
                        .orgdepth(new BigDecimal(orgdepth))
                        .build();
                organizationinfoMapper.insertOrganizationBIA(build);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private String getStringOrNull(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

}
