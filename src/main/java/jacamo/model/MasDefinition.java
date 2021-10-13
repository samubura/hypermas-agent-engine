package jacamo.model;

import java.util.Set;

public class MasDefinition {

  private final String id;
  private final Set<AgentDefinition> agents;

  public MasDefinition(String id, Set<AgentDefinition> agents){
    this.id = id;
    this.agents = agents;
  }

  public String getId(){
    return this.id;
  }

  public Set<AgentDefinition> getAgents(){
    return this.agents;
  }

}
