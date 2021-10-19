package mas.runtime;

import mas.model.AgentDefinition;
import mas.model.exceptions.AgentNameNotUniqueException;
import mas.model.MasDefinition;
import mas.model.MasRuntime;
import mas.runtime.bridge.MasBridge;
import mas.runtime.bridge.exception.FailedCommunicationException;
import mas.runtime.exceptions.MasAlreadyRunningException;
import mas.runtime.exceptions.MasStartFailureException;
import mas.runtime.exceptions.NoMasRunningException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractRuntimeManager implements RuntimeManager{

  private Optional<MasRuntime> currentMas;
  private final MasBridge bridge;

  protected AbstractRuntimeManager() {
    this.currentMas = Optional.empty();
    this.bridge = getBridge();
  }

  protected abstract MasBridge getBridge();

  @Override
  public void start(MasDefinition mas) throws MasAlreadyRunningException, MasStartFailureException {
    System.out.println("Starting mas " + mas.getId());
    if(this.isMasRunning()){
      throw new MasAlreadyRunningException(this.currentMas.get().getId());
    }
    this.currentMas = Optional.of(new MasRuntime(mas));
  }

  @Override
  public void stop() throws NoMasRunningException {
    System.out.println("mas stopped");
    if(!this.isMasRunning()){
      throw new NoMasRunningException();
    }
    this.currentMas = Optional.empty();
  }

  @Override
  public void addAgent(AgentDefinition agent) throws NoMasRunningException, AgentNameNotUniqueException, FailedCommunicationException {
    if(!this.isMasRunning()){
      throw new NoMasRunningException();
    }
    bridge.addAgent(agent);
    if(this.currentMas.isPresent()){
      this.currentMas.get().addAgent(agent);
    }
  }

  @Override
  public void removeAgent(String agentName) throws NoMasRunningException, FailedCommunicationException {
    if(!this.isMasRunning()){
      throw new NoMasRunningException();
    }
    bridge.removeAgent(agentName);
    this.currentMas.ifPresent(m -> m.removeAgent(agentName));
  }

  @Override
  public Set<AgentDefinition> getAgents() throws NoMasRunningException {
    if(!this.isMasRunning()){
      throw new NoMasRunningException();
    }
    return this.currentMas.map(MasRuntime::getAgents).orElse(new HashSet<>());
  }

  @Override
  public Optional<MasDefinition> getRuntime() {
    if(this.currentMas.isPresent()){
      return Optional.of(currentMas.get());
    } else {
      return Optional.empty();
    }
  }

  private boolean isMasRunning(){
    return this.currentMas.isPresent();
  }
}
