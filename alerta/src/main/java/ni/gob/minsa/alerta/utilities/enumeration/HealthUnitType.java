package ni.gob.minsa.alerta.utilities.enumeration;

/**
 * Created by FIRSTICT on 10/6/2014.
 */
public enum HealthUnitType {
    //códigos de las unidades primarias: Puesto de Salud, Centro de Salud, Policlínica.
    UnidadesPrimarias("3,4,5"),
    UnidadesPrimHosp("3,4,5,11,12,13,14,17,18"),
    UnidadesTodas("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35"),
    UnidadesSIVE("1,2,3,4,5,6,7,10,11,12,13,14,15,16,17,35");

    String discriminator;

    private HealthUnitType(String discriminator){
        this.discriminator = discriminator;
    }

    public String getDiscriminator(){
        return discriminator;
    }

}
