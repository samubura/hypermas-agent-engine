package mas.runtime.bridge;

import mas.model.AgentDefinition;
import mas.runtime.bridge.exception.FailedCommunicationException;

import java.io.IOException;

public interface MasBridge {

  void addAgent(AgentDefinition agent) throws FailedCommunicationException;

  void removeAgent(String agentName) throws FailedCommunicationException;

}
