package mas.storage.serialization;

import mas.model.AgentDefinition;
import mas.model.MasDefinition;

public interface MasSerializer {

  String toString(AgentDefinition agent);

  String toString(MasDefinition mas);
}
