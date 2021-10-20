package mas.storage.serialization;

import mas.model.AgentDefinition;
import mas.model.AgentSource;
import mas.model.MasDefinition;

/**
 * Helper static class that serializes the models using the jacamo syntax.
 */
class JacamoRestSerializer implements MasSerializer {

  public String toString(AgentSource source){
    return source.getCode() +
      "{ include(\"$jacamoJar/templates/common-cartago.asl\") }\n" +
      "{ include(\"$jacamoJar/templates/common-moise.asl\") }\n";
  }

  public String toString(AgentDefinition def){
      return "\tagent "+def.getName() + " : " + def.getType() + ".asl\n";
  }

  public String toString(MasDefinition def) {
    return def.getAgents().stream()
      .map(this::toString)
      .reduce("mas "+def.getId()+" {\n", (s, n) -> s+n)
      .concat("\tplatform: jacamo.rest.JCMRest(\"--main 2181 --restPort 34567\")\n") //TODO set this somewhere in a config
      .concat("}\n");
  }
}
