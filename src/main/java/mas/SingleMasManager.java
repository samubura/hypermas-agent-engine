package mas;

import mas.exceptions.AgentNotDefinedException;
import mas.exceptions.MasNotValidException;
import mas.model.AgentDefinition;
import mas.model.AgentSource;
import mas.model.MasDefinition;
import mas.model.exceptions.AgentNameNotUniqueException;
import mas.runtime.exceptions.MasAlreadyRunningException;
import mas.runtime.exceptions.MasStartFailureException;
import mas.runtime.exceptions.NoMasRunningException;
import mas.storage.exceptions.ReservedIdException;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public interface SingleMasManager {
  void startRuntime(MasDefinition mas) throws MasAlreadyRunningException, MasStartFailureException, IOException, MasNotValidException;

  void stopRuntime() throws NoMasRunningException;

  void addAgentToRuntime(AgentDefinition agent) throws NoMasRunningException, AgentNameNotUniqueException, AgentNotDefinedException;

  void removeAgentFromRuntime(String agentName) throws NoMasRunningException;

  Set<AgentDefinition> getRuntimeAgents() throws NoMasRunningException;

  Optional<MasDefinition> getRuntime();

  Set<String> getAvailableAgents();

  Optional<AgentSource> getAgent(String agentId) throws IOException;

  void saveAgent(AgentSource agent) throws IOException, ReservedIdException;
}
