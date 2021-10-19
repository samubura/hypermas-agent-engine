package mas.storage.jacamo;

import mas.model.AgentSource;
import mas.model.MasDefinition;
import mas.storage.StorageReader;
import mas.storage.serialization.MasDeserializer;
import mas.storage.serialization.MasDeserializerFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class JacamoFileReader implements StorageReader {

  private final Path agentFolder;
  private final Path jcmFolder;
  private final String charset;
  private final MasDeserializer deserializer;

  public JacamoFileReader(Path jcmFolder, Path agentFolder, String charset){
    this.jcmFolder = jcmFolder;
    this.agentFolder = agentFolder;
    this.charset = charset;
    this.deserializer = MasDeserializerFactory.createJacamoDeserializer();
  }

  @Override
  public Set<String> getAvailableMas() {
    return getNamesFromFileSystem(jcmFolder, (dir,name) -> name.endsWith(".jcm"));
  }

  @Override
  public Set<String> getAvailableAgents() {
    return getNamesFromFileSystem(agentFolder, (dir,name) -> name.endsWith(".asl"));
  }

  @Override
  public Optional<MasDefinition> getMas(String masId) throws IOException {
    String def = FileUtils.readFileToString(getMasFile(masId), charset);
    return Optional.of(deserializer.getMasFromString(masId, def));
  }

  @Override
  public Optional<AgentSource> getAgent(String agentId) throws IOException {
    if(!this.getAvailableAgents().contains(agentId)){
      return Optional.empty();
    }
    String code = FileUtils.readFileToString(this.getAgentFile(agentId), charset);
    return Optional.of(new AgentSource(agentId, code));
  }

  private File getAgentFile(String agentId){
    return Path.of(agentFolder.toString(), agentId+".asl").toFile();
  }

  private File getMasFile(String masId){
    return Path.of(jcmFolder.toString(), masId+".jcm").toFile();
  }

  private Set<String> getNamesFromFileSystem(Path folder, FilenameFilter filter){
    String[] array = folder.toFile().list(filter);
    if(array != null){
      return Arrays.stream(array).map(FilenameUtils::removeExtension).collect(Collectors.toSet());
    } else {
      return new HashSet<>();
    }
  }
}
