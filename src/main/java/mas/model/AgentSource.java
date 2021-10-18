package mas.model;

import java.util.Objects;

/**
 * Model for the definition of an agent source. Contains the id of the agent and the source code.
 */
public class AgentSource {
  private final String id;
  private final String code;

  public AgentSource(String id, String code){
    this.id = id;
    this.code = code;
  }

  public String getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AgentSource that = (AgentSource) o;
    return id.equals(that.id) &&
      code.equals(that.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code);
  }
}
