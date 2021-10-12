package jacamo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class JacamoFileManager implements JacamoStorageManager {

  private static final Path JACAMO_ROOT_FOLDER = FileSystems.getDefault().getPath("jacamo");
  private static final Path AGENT_FOLDER = Path.of(JACAMO_ROOT_FOLDER.toString(), "src", "agt");
  public static final String CHARSET = "UTF-8";

  @Override
  public List<String> getAvailableAgents() {
    return getNamesFromFileSystem(AGENT_FOLDER, (dir,name) -> name.endsWith(".asl"));
  }

  @Override
  public List<String> getAvailableMas() {
    return getNamesFromFileSystem(JACAMO_ROOT_FOLDER, (dir,name) -> name.endsWith(".jcm"));
  }

  @Override
  public boolean saveMas(MasDefinition mas){
    return writeFile(getMasFile(mas.getId()), mas.toJacamoString());
  }

  @Override
  public Optional<MasDefinition> getMas(String masId) {
    try {
      List<String> agents = FileUtils.readLines(getMasFile(masId), CHARSET).stream()
        .filter(l -> l.contains("agent "))
        .map(l -> l.replace("agent ", "").trim()).collect(Collectors.toList());
      return Optional.of(new MasDefinition(masId, agents));
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  @Override
  public boolean saveAgentCode(String agentId, String code) {
    return writeFile(this.getAgentFile(agentId), code);
  }

  @Override
  public Optional<String> getAgentCode(String agentId) {
    try {
      String code = FileUtils.readFileToString(this.getAgentFile(agentId), CHARSET);
      return Optional.of(code);
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  private File getAgentFile(String agentId){
    return Path.of(AGENT_FOLDER.toString(), agentId+".asl").toFile();
  }

  private File getMasFile(String masId){
    return Path.of(JACAMO_ROOT_FOLDER.toString(), masId+".jcm").toFile();
  }

  private boolean writeFile(File file, String content){
    try {
      FileUtils.writeStringToFile(file, content, CHARSET);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  private List<String> getNamesFromFileSystem(Path folder, FilenameFilter filter){
    String[] array = folder.toFile().list(filter);
    if(array != null){
      return Arrays.stream(array).map(FilenameUtils::removeExtension).collect(Collectors.toList());
    } else {
      return new ArrayList<>();
    }
  }


}
