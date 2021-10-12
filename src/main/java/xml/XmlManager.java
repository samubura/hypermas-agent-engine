package xml;

import java.util.Optional;

public interface XmlManager {

  boolean saveXml(String agentId, String xml);

  Optional<String> getXml(String agentId);
}
