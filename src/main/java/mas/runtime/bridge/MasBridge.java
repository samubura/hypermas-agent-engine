package mas.runtime.bridge;

import mas.model.AgentDefinition;

public interface MasBridge {

  void addAgent(AgentDefinition agent);

  void removeAgent(String agentName);

}
