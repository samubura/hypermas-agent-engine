package mas.storage.jacamo;

import mas.model.AgentSource;
import mas.model.MasDefinition;
import mas.storage.serialization.MasSerializer;
import mas.storage.StorageWriter;
import mas.storage.serialization.MasSerializerFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

class JacamoFileWriter implements StorageWriter {
  private final Path agentFolder;
  private final Path jcmFolder;
  private final MasSerializer serializer;
  private final String charset;

  public JacamoFileWriter(Path jcmFolder, Path agentFolder, String charset){
    this.jcmFolder = jcmFolder;
    this.agentFolder = agentFolder;
    this.serializer = MasSerializerFactory.createJacamoRestSerializer();
    this.charset = charset;
  }

  @Override
  public void saveMas(MasDefinition mas) throws IOException {
    writeFile(getMasFile(mas.getId()), serializer.toString(mas));
  }

  @Override
  public void saveAgent(AgentSource agent) throws IOException {
    writeFile(this.getAgentFile(agent.getId()), serializer.toString(agent));
  }

  private File getAgentFile(String agentId){
    return Path.of(agentFolder.toString(), agentId+".asl").toFile();
  }

  private File getMasFile(String masId){
    return Path.of(jcmFolder.toString(), masId+".jcm").toFile();
  }

  private void writeFile(File file, String content) throws IOException {
      FileUtils.writeStringToFile(file, content, charset);
  }
}
