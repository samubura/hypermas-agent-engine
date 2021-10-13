package jacamo;

import jacamo.storage.JacamoStorage;
import jacamo.storage.JacamoStorageInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;

public class JacamoManager {

  private static final Path JACAMO_ROOT_FOLDER = FileSystems.getDefault().getPath("jacamo-app");

  private static final String RESERVED_AGENT_NAME = "sample";
  private static final String RESERVED_MAS_NAME = "sample";
  private static JacamoManager INSTANCE = null;

  private final JacamoStorageInterface storage;
  private Optional<String> runningMasId;
  private Optional<Process> runningMasProcess;

  public static JacamoManager getInstance(){
    if(INSTANCE == null){
      INSTANCE = new JacamoManager();
    }
    return INSTANCE;
  }

  private JacamoManager(){
    this.runningMasId = Optional.empty();
    this.storage = new JacamoStorage(JACAMO_ROOT_FOLDER);
  }

  public JacamoStorageInterface getStorage(){
    return this.storage;
  }

  public boolean isMasRunning() {
    return this.runningMasId.isPresent();
  }

  public void runMas(String id){
    if (!this.isMasRunning()) {
      this.runningMasId = Optional.of(id);
      try {
        File log = new File("jacamo.log");
        log.createNewFile();
        this.runningMasProcess = Optional.of(
          new ProcessBuilder()
            .directory(JACAMO_ROOT_FOLDER.toFile())
            .command("gradlew.bat", "-q", "--console=\"plain\"")
            .redirectErrorStream(true)
            .redirectOutput(log)
            .start()
        );
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void stopMas(String id){
    if (this.isMasRunning()){
      this.runningMasId = Optional.empty();
      this.runningMasProcess.ifPresent(Process::destroy);
      this.runningMasProcess = Optional.empty();
    }
  }

  public Optional<String> getCurrentRunning(){
    return this.runningMasId;
  }

  public boolean acceptAgentId(String id) {
    return !id.equals(RESERVED_AGENT_NAME);
  }

  public boolean acceptMasId(String id){
    return !id.equals(RESERVED_MAS_NAME);
  }
}
