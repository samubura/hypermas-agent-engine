package jacamo.storage;

import jacamo.model.AgentSource;
import jacamo.model.MasDefinition;

import java.util.List;
import java.util.Optional;
import java.util.Set;

//TODO implement this with a DB of some sort (mongo?)

public class JacamoDBStorage implements JacamoStorageInterface {
  @Override
  public Set<String> getAvailableAgents() {
    return null;
  }

  @Override
  public Set<String> getAvailableMas() {
    return null;
  }

  @Override
  public Optional<MasDefinition> getMas(String masId) {
    return Optional.empty();
  }

  @Override
  public Optional<AgentSource> getAgent(String agentId) {
    return Optional.empty();
  }

  @Override
  public boolean saveMas(MasDefinition mas) {
    return false;
  }

  @Override
  public boolean saveAgent(AgentSource agent) {
    return false;
  }
}
