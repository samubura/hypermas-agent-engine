package mas.storage;

import mas.model.AgentSource;
import mas.model.MasDefinition;
import mas.storage.exceptions.ReservedIdException;

import java.io.IOException;

/**
 * Interface that exposes methods to save the models of agent source and MAS definitions to a persistence storage
 * used by the jacamo app.
 */
public interface StorageWriter {

  /**
   * Saves the AgentSource to a persistent location identified by its id.
   * Overwrites if there was already a saved agent with the same id.
   * @param agent the agent to be saved.
   */
  void saveAgent (AgentSource agent) throws IOException, ReservedIdException;

  /**
   * Saves the MasDefinition to a persistent location identified by its id.
   * Overwrites if there was already a saved MasDefinition with the same id.
   * @param mas the mas definition to be saved.
   */
  void saveMas(MasDefinition mas) throws IOException, ReservedIdException;

}
