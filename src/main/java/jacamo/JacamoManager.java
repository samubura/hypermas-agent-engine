package jacamo;

import jacamo.storage.JacamoFileWriter;
import jacamo.storage.JacamoStorageWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;

/**
 * A Singleton to manage all jacamo related stuff.
 * Esxposes a <a href="#{@link}">{@link JacamoStorageWriter}</a> to manage the persistent data used by the jacamo application and
 * methods to run and stop a MAS.
 */
public class JacamoManager {

  private static final Path JACAMO_ROOT_FOLDER = FileSystems.getDefault().getPath("jacamo-rest");

  private static final String RESERVED_AGENT_NAME = "empty";
  private static final String RESERVED_MAS_NAME = "default";
  private static JacamoManager INSTANCE = null;

  private final JacamoStorageWriter storage;
  private Optional<Process> runningMasProcess;
  private final String runnableCommand;

  /**
   * Lazily instantiate the singleton instance and return it.
   * @return the singleton instance of the JacamoManager.
   */
  public static JacamoManager getInstance(){
    if(INSTANCE == null){
      INSTANCE = new JacamoManager();
    }
    return INSTANCE;
  }

  private JacamoManager(){
    this.storage = new JacamoFileWriter(JACAMO_ROOT_FOLDER);
    this.runnableCommand = getRunnableCommand();
  }

  /**
   * Gives access to the storage used by this JacamoManager.
   * @return the JacamoStorageWriter used to save agents and mas.
   */
  public JacamoStorageWriter getStorage(){
    return this.storage;
  }

  /**
   * Check if there is alredy a MAS running.
   * @return true if there is already a MAS running.
   */
  public boolean isMasRunning() {
    return this.runningMasProcess.isPresent();
  }

  /**
   * Starts the Mas specified by the id.
   * @param id the id of the Mas to be executed.
   */
  public void runMas(String id){
    System.out.println("Starting mas " + id);
    if (!this.isMasRunning()) {
      try {
        this.runningMasProcess = Optional.of(
          new ProcessBuilder()
            .directory(JACAMO_ROOT_FOLDER.toFile())
            .command(runnableCommand, "run", "--args", "/src/jcm"+id+".jcm")
            .redirectErrorStream(true)
            .start()
        );
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Stops the mas that is currently in execution.
   */
  public void stopMas(){
    System.out.println("mas stopped");
    if (this.isMasRunning()){
      this.runningMasProcess
        .map(Process::descendants)
        .ifPresent(processHandleStream -> processHandleStream.forEach(ProcessHandle::destroy));
      this.runningMasProcess = Optional.empty();
    }
  }

  /**
   * Checks whether the provided id for an agent is valid.
   * @param id the id of the new agent.
   * @return true if the id is considered a valid one.
   */
  public boolean acceptAgentId(String id) {
    return !id.equals(RESERVED_AGENT_NAME);
  }

  /**
   * Checks wheter the provided id for a MAS is valid.
   * @param id the id of the new MAS.
   * @return true if the id is considered a valid one.
   */
  public boolean acceptMasId(String id){
    return !id.equals(RESERVED_MAS_NAME);
  }

  private String getRunnableCommand(){
    if(System.getProperty("os.name").toLowerCase().contains("windows")){
      return "gradlew.bat";
    } else {
      return "./gradlew";
    }
  }
}
