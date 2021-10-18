package mas.storage.serialization;

import mas.model.AgentDefinition;
import mas.model.MasDefinition;

/**
 * Helper static class that serializes the models using the jacamo syntax.
 */
class JacamoSerializer implements MasSerializer {

  public String toString(AgentDefinition def){
      return "\tagent "+def.getName() + " : " + def.getType() + ".asl\n";
  }

  public String toString(MasDefinition def) {
    return def.getAgents().stream()
      .map(this::toString)
      .reduce("mas "+def.getId()+" {\n", (s, n) -> s+n)
      .concat("}\n");
  }
}
