package jacamo.storage;

import jacamo.model.AgentSource;
import jacamo.model.MasDefinition;

import java.util.Optional;
import java.util.Set;

public interface JacamoStorageReader {

  Set<String> getAvailableMas();

  Set<String> getAvailableAgents();

  Optional<MasDefinition> getMas(String masId);

  Optional<AgentSource> getAgent(String agentId);
}
