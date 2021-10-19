package mas.runtime.bridge.jacamo;

import mas.model.AgentDefinition;
import mas.runtime.bridge.MasBridge;
import mas.runtime.bridge.exception.FailedCommunicationException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JacamoRestBridge implements MasBridge {

  private static final String BASE_PATH = "http://localhost:9000"; //TODO set this somewhere in a config

  private final HttpClient client;

  public JacamoRestBridge(){
    this.client = HttpClient.newHttpClient();
  }

  @Override
  public void addAgent(AgentDefinition agent) throws FailedCommunicationException {
    HttpRequest req = HttpRequest.newBuilder()
      .uri(URI.create(BASE_PATH+"/agents/"+agent.getName()+"?type="+agent.getType()))
      .POST(HttpRequest.BodyPublishers.noBody())
      .build();
    try {
      HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
      if(res.statusCode()>400){
        throw new FailedCommunicationException();
      }
    } catch (IOException | InterruptedException e) {
      throw new FailedCommunicationException();
    }
  }

  @Override
  public void removeAgent(String agentName) throws FailedCommunicationException {
    HttpRequest req = HttpRequest.newBuilder()
      .uri(URI.create(BASE_PATH+"/agents/"+agentName))
      .DELETE()
      .build();
    try {
      HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
      if(res.statusCode()>400){
        throw new FailedCommunicationException();
      }
    } catch (IOException | InterruptedException e) {
      throw new FailedCommunicationException();
    }
  }
}
