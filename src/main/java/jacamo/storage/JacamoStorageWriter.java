package jacamo.storage;

import jacamo.model.AgentSource;
import jacamo.model.MasDefinition;

public interface JacamoStorageWriter {

  boolean saveAgent(AgentSource agent);

  boolean saveMas(MasDefinition mas);

}
