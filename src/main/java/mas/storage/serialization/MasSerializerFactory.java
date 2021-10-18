package mas.storage.serialization;

public interface MasSerializerFactory {

  static MasSerializer createJacamoSerializer(){
    return new JacamoSerializer();
  }
}
