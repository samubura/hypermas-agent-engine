package mas.model;

import mas.model.exceptions.AgentNameNotUniqueException;

public class MasRuntime extends MasDefinition {

  public MasRuntime(MasDefinition mas){
    super(mas.getId(), mas.getAgents());
  }

  public void addAgent(AgentDefinition agent) throws AgentNameNotUniqueException {
    boolean added = super.agents.add(agent);
    if(!added) {
      throw new AgentNameNotUniqueException(agent.getName());
    }
  }

  public void removeAgent(String agent){
    super.agents.removeIf(a -> a.getName().equals(agent));
  }
}
