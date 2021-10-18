package mas.storage.exceptions;

public class ReservedIdException extends Exception {

  public String id;

  public ReservedIdException(String id){
    super();
    this.id = id;
  }
}
