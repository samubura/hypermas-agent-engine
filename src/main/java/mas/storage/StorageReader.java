package mas.storage;

import mas.model.AgentSource;
import mas.model.MasDefinition;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public interface StorageReader {

  Set<String> getAvailableMas();

  Set<String> getAvailableAgents();

  Optional<MasDefinition> getMas(String masId) throws IOException;

  Optional<AgentSource> getAgent(String agentId) throws IOException;
}
