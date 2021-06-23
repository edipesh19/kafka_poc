package org.dipesh.kafka.client;

import com.oracle.dicom.agent.mediator.dto.AgentMessageDTO;

public interface IMessageHandler {
    void handleMessage(AgentMessageDTO agentMessageDTO);
}
