package jacamo.storage;

import jacamo.model.AgentSource;
import jacamo.model.MasDefinition;

/**
 * Interface that exposes methods to save the models of agent source and MAS definitions to a persistence storage
 * used by the jacamo app.
 */
public interface JacamoStorageWriter {

  /**
   * Saves the AgentSource to a persistent location identified by its id.
   * Overwrites if there was already a saved agent with the same id.
   * @param agent the agent to be saved.
   * @return true if the operation was successful.
   */
  boolean saveAgent(AgentSource agent);

  /**
   * Saves the MasDefinition to a persistent location identified by its id.
   * Overwrites if there was already a saved MasDefinition with the same id.
   * @param mas the mas definition to be saved.
   * @return true if the operation was successful.
   */
  boolean saveMas(MasDefinition mas);

}
