package jacamo.model;

/**
 * Model for the defintion of an agent instance used within a MAS.
 */
public class AgentDefinition {
  private final String name;
  private final String type;

  public AgentDefinition(String name, String type){
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }
}
