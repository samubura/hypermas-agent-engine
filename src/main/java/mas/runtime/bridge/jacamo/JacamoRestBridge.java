package mas.runtime.bridge.jacamo;

import io.vertx.core.json.JsonObject;
import mas.model.AgentDefinition;
import mas.runtime.bridge.MasBridge;
import mas.runtime.bridge.exception.FailedCommunicationException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;


public class JacamoRestBridge implements MasBridge {

  private static final String BASE_PATH = "http://localhost:34567"; //TODO set this somewhere in a config

  private final HttpClient client;

  public JacamoRestBridge(){
    this.client = HttpClientBuilder.create().build();
  }

  @Override
  public void addAgent(AgentDefinition agent) throws FailedCommunicationException {
    HttpPost req = new HttpPost(URI.create(BASE_PATH+"/agents/"+agent.getName()));
    req.setEntity(new StringEntity(toJson(agent), ContentType.APPLICATION_JSON));
    try {
      HttpResponse res = client.execute(req);
      if(res.getStatusLine().getStatusCode() > 400) {
        throw new FailedCommunicationException();
      }
    } catch (IOException e) {
      throw new FailedCommunicationException();
    }
  }

  @Override
  public void removeAgent(String agentName) throws FailedCommunicationException {
    HttpDelete req = new HttpDelete(URI.create(BASE_PATH+"/agents/"+agentName));
    try {
      HttpResponse res = client.execute(req);
      if(res.getStatusLine().getStatusCode()>400){
        throw new FailedCommunicationException();
      }
    } catch (IOException e) {
      throw new FailedCommunicationException();
    }
  }

  private String toJson(AgentDefinition agent){
    return new JsonObject()
      .put("name", agent.getName())
      .put("type", agent.getType())
      .encode();
  }
}
