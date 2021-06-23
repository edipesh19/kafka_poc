package org.demo.kafka.poller;

import com.oracle.dicom.agent.mediator.dto.AgentMessageDTO;
import org.dipesh.kafka.client.IMessageHandler;


public class MessageHandler implements IMessageHandler {
    @Override
    public void handleMessage(AgentMessageDTO agentMessageDTO) {
        System.out.println("AgentMessageDTO messageId = " + agentMessageDTO.getMsgId()
            + " WorkspaceId = " + agentMessageDTO.getAggregatorId()
            + " AgentId = " + agentMessageDTO.getAgentId());
    }
}
