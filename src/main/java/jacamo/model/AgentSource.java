package jacamo.model;

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
