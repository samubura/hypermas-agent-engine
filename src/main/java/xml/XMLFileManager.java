package xml;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class XMLFileManager implements XmlManager{

  private static final Path XML_FOLDER = Path.of("xml");
  private static final String CHARSET = "UTF-8";

  @Override
  public boolean saveXml(String agentId, String xml) {
    File file = getFile(agentId);
    try {
      FileUtils.writeStringToFile(file, xml, CHARSET);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public Optional<String> getXml(String agentId) {
    try {
      String xml = FileUtils.readFileToString(this.getFile(agentId), CHARSET);
      return Optional.of(xml);
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  private File getFile(String agentId){
    return Path.of(XML_FOLDER.toString(), agentId+".xml").toFile();
  }
}
