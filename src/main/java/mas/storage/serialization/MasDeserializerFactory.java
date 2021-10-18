package mas.storage.serialization;

public interface MasDeserializerFactory {

  static MasDeserializer createJacamoDeserializer(){
    return new JacamoDeserializer();
  }
}
