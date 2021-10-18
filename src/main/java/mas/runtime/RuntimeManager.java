package mas.runtime;

import mas.model.AgentDefinition;
import mas.model.exceptions.AgentNameNotUniqueException;
import mas.model.MasDefinition;
import mas.runtime.exceptions.MasAlreadyRunningException;
import mas.runtime.exceptions.MasStartFailureException;
import mas.runtime.exceptions.NoMasRunningException;

import java.util.Optional;
import java.util.Set;

public interface RuntimeManager {

  /**
   * Starts the Mas specified by the id.
   * @param mas the definition of the mas to be run.
   */
  void start(MasDefinition mas) throws MasAlreadyRunningException, MasStartFailureException;

  /**
   * Stops the mas that is currently in execution.
   */
  void stop() throws NoMasRunningException;

  void addAgent(AgentDefinition agent) throws NoMasRunningException, AgentNameNotUniqueException;

  void removeAgent(String agentName) throws NoMasRunningException;

  Set<AgentDefinition> getAgents() throws NoMasRunningException;

  Optional<MasDefinition> getRuntime();

}
