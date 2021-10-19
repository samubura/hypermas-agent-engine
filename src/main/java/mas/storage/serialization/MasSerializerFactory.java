package mas.storage.serialization;

public interface MasSerializerFactory {

  static MasSerializer createJacamoRestSerializer(){
    return new JacamoRestSerializer();
  }
}
