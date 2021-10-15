package jacamo.model;

/**
 * Helper static class that serializes the models using the jacamo syntax.
 */
public class JacamoSerializer {

  public static String toJacamoString(AgentDefinition def){
      return "\tagent "+def.getName() + " : " + def.getType() + ".asl\n";
  }

  public static String toJacamoString(MasDefinition def) {
    return def.getAgents().stream()
      .map(JacamoSerializer::toJacamoString)
      .reduce("mas "+def.getId()+" {\n", (s, n) -> s+n)
      .concat("}\n");
  }
}
