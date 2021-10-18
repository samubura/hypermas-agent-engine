package mas.storage.jacamo;

import mas.model.AgentSource;
import mas.model.MasDefinition;
import mas.storage.StorageManager;
import mas.storage.StorageReader;
import mas.storage.StorageWriter;
import mas.storage.exceptions.ReservedIdException;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JacamoStorageManager implements StorageManager {

  private static final Path JACAMO_PATH = FileSystems.getDefault().getPath("jacamo-rest", "src");
  private static final Path JCM_FOLDER = Path.of(JACAMO_PATH.toString(),  "jcm");
  private static final Path AGT_FOLDER = Path.of(JACAMO_PATH.toString(),  "agt");
  public static final String CHARSET = "UTF-8";

  private static final String RESERVED_AGENT_NAME = "empty";
  private static final String RESERVED_MAS_NAME = "default";

  private final StorageWriter writer;
  private final StorageReader reader;

  public JacamoStorageManager(){
    this.writer = new JacamoFileWriter(JCM_FOLDER, AGT_FOLDER, CHARSET);
    this.reader = new JacamoFileReader(JCM_FOLDER, AGT_FOLDER, CHARSET);
  }

  @Override
  public Set<String> getAvailableMas() {
    return reader.getAvailableMas().stream().filter(n -> !n.equals(RESERVED_MAS_NAME)).collect(Collectors.toSet());
  }

  @Override
  public Set<String> getAvailableAgents() {
    return reader.getAvailableAgents();
  }

  @Override
  public Optional<MasDefinition> getMas(String masId) throws IOException {
    return reader.getMas(masId);
  }

  @Override
  public Optional<AgentSource> getAgent(String agentId) throws IOException {
    return reader.getAgent(agentId);
  }

  @Override
  public void saveAgent(AgentSource agent) throws IOException, ReservedIdException {
    if(agent.getId().equals(RESERVED_AGENT_NAME)){
      throw new ReservedIdException(RESERVED_AGENT_NAME);
    }
    this.writer.saveAgent(agent);
  }

  @Override
  public void saveMas(MasDefinition mas) throws IOException, ReservedIdException {
    if(mas.getId().equals(RESERVED_MAS_NAME)){
      throw new ReservedIdException(RESERVED_MAS_NAME);
    }
    this.writer.saveMas(mas);
  }
}
