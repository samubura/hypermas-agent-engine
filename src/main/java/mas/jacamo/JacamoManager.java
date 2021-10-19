package mas.jacamo;

import mas.SingleMasManager;
import mas.model.AgentDefinition;
import mas.model.AgentSource;
import mas.model.MasDefinition;
import mas.model.exceptions.AgentNameNotUniqueException;
import mas.runtime.RuntimeManager;
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
  public void startRuntime(MasDefinition mas) throws MasAlreadyRunningException, MasStartFailureException, IOException {
    //Overwrite mas name to write always to same location
    mas = new MasDefinition(MAS_NAME, mas.getAgents());
    //save the mas file
    try {
      this.storage.saveMas(mas);
    } catch (ReservedIdException e) {
      //this cannot happen since the name is hardcoded
    }
    //TODO add business logic controls
    this.runtime.start(mas);
  }

  @Override
  public void stopRuntime() throws NoMasRunningException {
    //TODO add business logic controls
    this.runtime.stop();
  }

  @Override
  public void addAgentToRuntime(AgentDefinition agent) throws NoMasRunningException, AgentNameNotUniqueException {
    //TODO business logic controls
    this.runtime.addAgent(agent);
  }

  @Override
  public void removeAgentFromRuntime(String agentName) throws NoMasRunningException {
    //TODO business logic controls
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


}
