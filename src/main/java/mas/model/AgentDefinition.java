package mas.model;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AgentDefinition that = (AgentDefinition) o;
    return name.equals(that.name) &&
      type.equals(that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type);
  }
}
