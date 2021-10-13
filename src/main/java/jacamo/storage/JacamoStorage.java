package jacamo.storage;

import jacamo.model.AgentSource;
import jacamo.model.MasDefinition;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class JacamoStorage implements JacamoStorageInterface {

  private final JacamoStorageWriter fileWriter;
  private final JacamoStorageInterface storage;

  public JacamoStorage(Path folderPath){
    this.fileWriter = new JacamoFileWriter(folderPath);
    this.storage = new JacamoMemoryStorage(); //TODO change this
  }

  @Override
  public Set<String> getAvailableAgents() {
    return storage.getAvailableAgents();
  }

  @Override
  public Set<String> getAvailableMas() {
    return storage.getAvailableMas();
  }

  @Override
  public Optional<MasDefinition> getMas(String masId) {
    return storage.getMas(masId);
  }

  @Override
  public Optional<AgentSource> getAgent(String agentId) {
    return storage.getAgent(agentId);
  }

  @Override
  public boolean saveMas(MasDefinition mas) {
    return storage.saveMas(mas) && fileWriter.saveMas(mas);
  }

  @Override
  public boolean saveAgent(AgentSource agent) {
    return storage.saveAgent(agent) && fileWriter.saveAgent(agent);
  }
}
