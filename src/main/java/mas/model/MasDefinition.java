package mas.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Model for the definition of MAS configuration.
 */
public class MasDefinition {

  private final String id;
  protected final Set<AgentDefinition> agents;

  public MasDefinition(String id, Set<AgentDefinition> agents){
    this.id = id;
    this.agents = new HashSet<>(agents);
  }

  public String getId(){
    return this.id;
  }

  public Set<AgentDefinition> getAgents(){
    return Collections.unmodifiableSet(this.agents);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MasDefinition that = (MasDefinition) o;
    return id.equals(that.id) &&
      agents.equals(that.agents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, agents);
  }
}
