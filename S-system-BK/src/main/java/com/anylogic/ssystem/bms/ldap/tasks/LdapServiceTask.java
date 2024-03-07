package com.anylogic.ssystem.bms.ldap.tasks;

import com.anylogic.ssystem.bms.com.mapper.ComOrganizationinfoMapper;
import com.anylogic.ssystem.bms.ldap.service.LdapService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.beans.factory.annotation.*;

@Component("BatchBean")
@EnableScheduling
public class LdapServiceTask implements SchedulingConfigurer {

    @Autowired
    private LdapService ldapService;

    @Autowired
    private ComOrganizationinfoMapper organizationinfoMapper;

    private static ScheduledTaskRegistrar scheduledTaskRegistrar;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        LdapServiceTask.scheduledTaskRegistrar = taskRegistrar;
    }

    @Scheduled(cron = "0 0 6 * * ?")  // 매일 오전 6시에 1회 실행
    public void getLDAPSnyc(){
        try {
            organizationinfoMapper.deleteLDAPDept();
            ldapService.getAllGroups();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
