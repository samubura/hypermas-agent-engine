package jacamo;

import java.util.List;

public class MasDefinition {

  private final String id;
  private final List<String> agents;

  public MasDefinition(String id, List<String> agents){
    this.id = id;
    this.agents = agents;
  }

  public String getId(){
    return this.id;
  }

  public List<String> getAgents(){
    return this.agents;
  }

  public String toJacamoString() {
    return this.agents.stream()
      .reduce("mas " + this.id + "{",
        (string, agentName) -> string + "\n  agent " + agentName)
      .concat("\n}\n");
  }
}
