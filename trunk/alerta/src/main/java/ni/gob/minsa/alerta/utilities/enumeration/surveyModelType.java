package ni.gob.minsa.alerta.utilities.enumeration;

/**
 * Created by FIRSTICT on 10/6/2014.
 */
public enum surveyModelType {
    AedesAegypti("TIPOMODENCU|AEDES"),
    LarvariaAedes("TIPOMODENCU|LARVA"),
    DepositoPreferencial("TIPOMODENCU|DEPOS");

    String discriminator;

    private surveyModelType(String discriminator){
        this.discriminator = discriminator;
    }

    public String getDiscriminator(){
        return discriminator;
    }

}
