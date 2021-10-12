package jacamo;

import java.util.List;
import java.util.Optional;

public interface JacamoStorageManager {

  static JacamoStorageManager create(){
    return new JacamoFileManager();
  }

  List<String> getAvailableAgents();

  List<String> getAvailableMas();

  boolean saveMas(MasDefinition mas);

  Optional<MasDefinition> getMas(String masId);

  boolean saveAgentCode(String agentId, String code);

  Optional<String> getAgentCode(String agentId);

}
