package mas.model.exceptions;

public class AgentNameNotUniqueException extends Throwable {
  public final String name;
  public AgentNameNotUniqueException(String name) {
    this.name = name;
  }
}
