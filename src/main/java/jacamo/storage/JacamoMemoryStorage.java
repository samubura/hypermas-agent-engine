package jacamo.storage;

import jacamo.model.AgentDefinition;
import jacamo.model.AgentSource;
import jacamo.model.MasDefinition;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class JacamoMemoryStorage implements JacamoStorageInterface{

  private final Map<String, MasDefinition> masDefinitionMap;
  private final Map<String, AgentSource> agentSourceMap;

  public JacamoMemoryStorage() {
    agentSourceMap = new HashMap<>();
    masDefinitionMap = new HashMap<>();
    this.saveDefault();
  }

  @Override
  public Set<String> getAvailableMas() {
    return masDefinitionMap.keySet();
  }

  @Override
  public Set<String> getAvailableAgents() {
    return agentSourceMap.keySet();
  }

  @Override
  public Optional<MasDefinition> getMas(String masId) {
    if(!masDefinitionMap.containsKey(masId)){
      return Optional.empty();
    }
    return Optional.of(masDefinitionMap.get(masId));
  }

  @Override
  public Optional<AgentSource> getAgent(String agentId) {
    if(!agentSourceMap.containsKey(agentId)){
      return Optional.empty();
    }
    return Optional.of(agentSourceMap.get(agentId));
  }

  @Override
  public boolean saveMas(MasDefinition mas) {
    masDefinitionMap.put(mas.getId(), mas);
    return true;
  }

  @Override
  public boolean saveAgent(AgentSource agent) {
    agentSourceMap.put(agent.getId(), agent);
    return true;
  }

  private void saveDefault(){
    String masId = "sample";
    String agentId = "sample";
    String agentType = "sample";
    Set<AgentDefinition> agents = new HashSet<>();
    agents.add(new AgentDefinition(agentId, agentType));
    this.masDefinitionMap.put(masId, new MasDefinition(masId, agents));

    try {
      Path path = Path.of("jacamo-app", "src", "agt", agentId+".asl");
      String code = FileUtils.readFileToString(path.toFile(), "UTF-8");
      this.agentSourceMap.put(agentId, new AgentSource(agentId, code));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
