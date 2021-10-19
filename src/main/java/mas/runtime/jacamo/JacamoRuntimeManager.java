package mas.runtime.jacamo;

import mas.model.MasDefinition;
import mas.runtime.AbstractRuntimeManager;
import mas.runtime.bridge.MasBridge;
import mas.runtime.bridge.jacamo.JacamoRestBridge;
import mas.runtime.exceptions.MasAlreadyRunningException;
import mas.runtime.exceptions.MasStartFailureException;
import mas.runtime.exceptions.NoMasRunningException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class JacamoRuntimeManager extends AbstractRuntimeManager {

  private static final Path EXECUTION_FOLDER = Path.of("jacamo-rest");

  private Process masProcess;
  private final String runnableCommand;

  public JacamoRuntimeManager() {
    super();
    this.runnableCommand = this.getRunnableCommand();
    this.masProcess = null;
  }

  @Override
  protected MasBridge getBridge() {
    return new JacamoRestBridge();
  }

  @Override
  public void start(MasDefinition mas) throws MasStartFailureException, MasAlreadyRunningException {
    super.start(mas);
    try {
      Path masPath = Path.of("src", "jcm", mas.getId()+".jcm");
      this.masProcess = new ProcessBuilder()
        .inheritIO()
        .directory(EXECUTION_FOLDER.toFile())
        .command(runnableCommand, "run", "--args", masPath.toString(), "-q", "--console=plain")
        .redirectErrorStream(true)
        .start();
    } catch (IOException e) {
      throw new MasStartFailureException(e.getMessage());
    }
  }

  @Override
  public void stop() throws NoMasRunningException {
    super.stop();
    this.masProcess.descendants().forEach(ProcessHandle::destroy);
    this.masProcess = null;
  }

  private String getRunnableCommand(){
    if(System.getProperty("os.name").toLowerCase().contains("windows")){
      return "gradlew.bat";
    } else {
      return "./gradlew";
    }
  }
}
