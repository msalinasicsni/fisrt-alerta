package ni.gob.minsa.alerta.utilities.enumeration;

/**
 * Created by FIRSTICT on 10/6/2014.
 */
public enum HealthUnitType {
    //c�digos de las unidades primarias: Puesto de Salud, Centro de Salud, Policl�nica.
    UnidadesPrimarias("3,4,5");

    String discriminator;

    private HealthUnitType(String discriminator){
        this.discriminator = discriminator;
    }

    public String getDiscriminator(){
        return discriminator;
    }

}
