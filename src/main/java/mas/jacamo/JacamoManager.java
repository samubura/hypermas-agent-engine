package mas.jacamo;

import mas.SingleMasManager;
import mas.exceptions.AgentNotDefinedException;
import mas.exceptions.MasNotValidException;
import mas.model.AgentDefinition;
import mas.model.AgentSource;
import mas.model.MasDefinition;
import mas.model.exceptions.AgentNameNotUniqueException;
import mas.runtime.RuntimeManager;
import mas.runtime.bridge.exception.FailedCommunicationException;
import mas.runtime.exceptions.MasAlreadyRunningException;
import mas.runtime.exceptions.MasStartFailureException;
import mas.runtime.exceptions.NoMasRunningException;
import mas.runtime.jacamo.JacamoRuntimeManager;
import mas.storage.StorageManager;
import mas.storage.exceptions.ReservedIdException;
import mas.storage.jacamo.JacamoStorageManager;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JacamoManager implements SingleMasManager {

  private static String MAS_NAME = "runtime";

  private static JacamoManager INSTANCE = null;

  private final StorageManager storage;
  private final RuntimeManager runtime;

  /**
   * Lazily instantiate the singleton instance and return it.
   * @return the singleton instance of the JacamoManager.
   */
  public static SingleMasManager getInstance(){
    if(INSTANCE == null){
      INSTANCE = new JacamoManager();
    }
    return INSTANCE;
  }

  private JacamoManager(){
    this.storage = new JacamoStorageManager();
    this.runtime = new JacamoRuntimeManager();
  }

  @Override
  public void startRuntime(MasDefinition mas) throws MasAlreadyRunningException, MasStartFailureException, IOException, MasNotValidException {
    //Overwrite mas name to write always to same location
    mas = new MasDefinition(MAS_NAME, mas.getAgents());
    //check if the mas is correctly defined
    if(!this.validateMasDefinition(mas)){
      throw new MasNotValidException("Mas is not well defined");
    }
    //save the mas file
    try {
      this.storage.saveMas(mas);
    } catch (ReservedIdException e) {
      //this cannot happen since the name is hardcoded
    }
    //start the subprocess
    this.runtime.start(mas);
  }

  @Override
  public void stopRuntime() throws NoMasRunningException {
    this.runtime.stop();
  }

  @Override
  public void addAgentToRuntime(AgentDefinition agent) throws NoMasRunningException, AgentNameNotUniqueException, AgentNotDefinedException, FailedCommunicationException {
    if(!this.validateAgentDefinition(agent)){
      throw new AgentNotDefinedException(agent.getType());
    }
    this.runtime.addAgent(agent);
  }

  @Override
  public void removeAgentFromRuntime(String agentName) throws NoMasRunningException, FailedCommunicationException {
    this.runtime.removeAgent(agentName);
  }

  @Override
  public Set<AgentDefinition> getRuntimeAgents() throws NoMasRunningException {
    return this.runtime.getAgents();
  }

  @Override
  public Optional<MasDefinition> getRuntime() {
    return this.runtime.getRuntime();
  }

  @Override
  public Set<String> getAvailableAgents() {
    return storage.getAvailableAgents();
  }

  @Override
  public Optional<AgentSource> getAgent(String agentId) throws IOException {
    return storage.getAgent(agentId);
  }

  @Override
  public void saveAgent(AgentSource agent) throws IOException, ReservedIdException {
    storage.saveAgent(agent);
  }

  private boolean validateMasDefinition(MasDefinition mas) {
    return this.storage.getAvailableAgents().containsAll(
      mas.getAgents().stream().map(AgentDefinition::getType).collect(Collectors.toSet())
    );
  }

  private boolean validateAgentDefinition(AgentDefinition agent) {
    return this.storage.getAvailableAgents().contains(agent.getType());
  }


}
