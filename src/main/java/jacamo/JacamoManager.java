package jacamo;

public class JacamoManager {

  private static final String RESERVED_AGENT_NAME = "_sample";
  private static final String RESERVED_MAS_NAME = "_sample";
  private static JacamoManager INSTANCE = null;

  private final JacamoStorageManager storage;

  public static JacamoManager getInstance(){
    if(INSTANCE == null){
      INSTANCE = new JacamoManager();
    }
    return INSTANCE;
  }

  private JacamoManager(){
    this.storage = new JacamoFileManager();
  }

  public JacamoStorageManager getStorage(){
    return this.storage;
  }

  public boolean acceptAgentId(String id) {
    return !id.equals(RESERVED_AGENT_NAME);
  }

  public boolean acceptMasId(String id){
    return !id.equals(RESERVED_MAS_NAME);
  }
}
