package mas.storage.serialization;

import mas.model.AgentDefinition;
import mas.model.MasDefinition;

import java.util.Set;
import java.util.stream.Collectors;

class JacamoDeserializer implements MasDeserializer{
  @Override
  public MasDefinition getMasFromString(String masId, String masString) {
    Set<AgentDefinition> agents = masString.lines()
      .filter(l -> l.contains("agent "))
      .map(l -> l.replace("agent ", "").trim())
      .map(this::getAgentFromString)
      .collect(Collectors.toSet());
    return new MasDefinition(masId, agents);
  }

  @Override
  public AgentDefinition getAgentFromString(String agentDef) {
    String[] split = agentDef.split(":");
    return new AgentDefinition(split[0].trim(), split[1].replace(".asl", "").trim());
  }
}
