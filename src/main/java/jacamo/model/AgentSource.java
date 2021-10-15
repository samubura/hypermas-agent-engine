package jacamo.model;

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
}
