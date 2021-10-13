package jacamo.storage;

import jacamo.model.AgentSource;
import jacamo.model.JacamoSerializer;
import jacamo.model.MasDefinition;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

class JacamoFileWriter implements JacamoStorageWriter {
  private final Path agentFolder;
  private final Path jacamoRootFolder;
  public static final String CHARSET = "UTF-8";

  public JacamoFileWriter(Path jacamoRoot){
    this.jacamoRootFolder = jacamoRoot;
    this.agentFolder = Path.of(jacamoRoot.toString(), "src", "agt");
  }

  @Override
  public boolean saveMas(MasDefinition mas){
    return writeFile(getMasFile(mas.getId()), JacamoSerializer.toJacamoString(mas));
  }

  @Override
  public boolean saveAgent(AgentSource agent) {
    return writeFile(this.getAgentFile(agent.getId()), agent.getCode());
  }

  private File getAgentFile(String agentId){
    return Path.of(agentFolder.toString(), agentId+".asl").toFile();
  }

  private File getMasFile(String masId){
    return Path.of(jacamoRootFolder.toString(), masId+".jcm").toFile();
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


}
