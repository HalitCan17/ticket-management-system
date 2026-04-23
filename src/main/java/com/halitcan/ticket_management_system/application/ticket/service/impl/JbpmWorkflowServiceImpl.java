package com.halitcan.ticket_management_system.application.ticket.service.impl;

import com.halitcan.ticket_management_system.application.ticket.model.WorkflowSignal;
import com.halitcan.ticket_management_system.application.ticket.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JbpmWorkflowServiceImpl implements WorkflowService {

    @Value("${jbpm.server.url:http://jbpm:8080/kie-server/services/rest/server}")
    private String serverUrl;

    @Value("${jbpm.server.user:admin}")
    private String user;

    @Value("${jbpm.server.password:admin}")
    private String password;

    @Value("${jbpm.container.id:ticket-management_1.0.0-SNAPSHOT}")
    private String containerId;

    @Value("${jbpm.process.id:ticket-management.ticket-process}")
    private String processId;

    private ProcessServicesClient processClient;

    private ProcessServicesClient getClient() {
        if (this.processClient == null) {
            log.info("SAU-2026: jBPM istemcisi başlatılmamış, bağlantı kuruluyor...");
            KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(serverUrl, user, password);
            config.setMarshallingFormat(MarshallingFormat.JSON);
            KieServicesClient client = KieServicesFactory.newKieServicesClient(config);
            this.processClient = client.getServicesClient(ProcessServicesClient.class);
            log.info("SAU-2026: jBPM bağlantısı BAŞARILI!");
        }
        return this.processClient;
    }

    @Override
    public Long startTicketProcess(String ticketPublicId, String ownerEmail) {
        Map<String, Object> params = new HashMap<>();
        params.put("ticketId", ticketPublicId);
        params.put("owner", ownerEmail);

        try {
            // DÜZELTME: processClient değişkeni yerine getClient() metodunu çağırıyoruz
            Long instanceId = getClient().startProcess(containerId, processId, params);
            log.info("Bilet [{}] için jBPM süreci başlatıldı. Instance ID: {}", ticketPublicId, instanceId);
            return instanceId;
        } catch (Exception e) {
            log.error("Süreç başlatma hatası: ", e);
            throw new RuntimeException("jBPM süreci başlatılamadı.");
        }
    }

    @Override
    public void signalProcess(Long processInstanceId, WorkflowSignal signal) {
        if (processInstanceId == null) {
            throw new IllegalStateException("Process Instance ID bulunamadı, sinyal gönderilemez!");
        }
        try {
            getClient().signalProcessInstance(containerId, processInstanceId, signal.getSignalName(), null);
            log.info("Süreç [{}] için [{}] sinyali gönderildi.", processInstanceId, signal.getSignalName());
        } catch (Exception e) {
            log.error("Sinyal gönderme hatası: ", e);
            throw new RuntimeException("İş akışı ilerletilemedi.");
        }
    }
}