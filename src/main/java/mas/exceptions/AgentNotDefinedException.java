package mas.exceptions;

public class AgentNotDefinedException extends Exception {
  public String agentType;
  public AgentNotDefinedException(String type) {
    this.agentType = type;
  }
}
