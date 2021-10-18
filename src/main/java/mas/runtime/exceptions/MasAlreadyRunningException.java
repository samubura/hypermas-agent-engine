package mas.runtime.exceptions;

public class MasAlreadyRunningException extends Exception{

  public final String masId;

  public MasAlreadyRunningException(String masId) {
    super();
    this.masId = masId;
  }
}
