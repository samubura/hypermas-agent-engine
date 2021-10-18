package mas.storage.serialization;

import mas.model.AgentDefinition;
import mas.model.MasDefinition;

public interface MasDeserializer {

  MasDefinition getMasFromString(String masId, String masString);

  AgentDefinition getAgentFromString(String agentDef);
}
