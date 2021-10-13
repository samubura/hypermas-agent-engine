package jacamo.model;

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
