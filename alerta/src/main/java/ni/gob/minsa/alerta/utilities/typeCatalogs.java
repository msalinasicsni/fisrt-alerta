package ni.gob.minsa.alerta.utilities;

/**
 * Created by Herrold on 11/06/14 13:41
 */
public enum typeCatalogs {

    EstadoCivil("ESTCV"),
    Ocupacion("Ocupacion"),
    Sexo("SEXO"),
    Escolaridad("ESCDA"),
    TipoIdentificacion("TPOID"),
    Etnia("ETNIA"),
    Procedencia("PROCDNCIA"),
    TipoSeguro("TPOAS"),
    TipoEncuesta("TIPOENCUESTA"),
    TipoOrdinal("ORDINALENCUESTA"),
    TipoTransporte("TPTRANSPORTE"),
    IdentAgente("IDENTAGNT"),
    TecnicaMuestra("TECNICAMX"),
    Ordinal("ORDINAL"), //MSalinas
    DistritosMng("DISTRIT"), //MSalinas
    AreasMng("AREAMNG"), //MSalinas
    ModeloEncuesta("TIPOMODENCU"); //MSalinas

    String discriminator;

    private typeCatalogs(String discriminator){
        this.discriminator = discriminator;
    }

    public String getDiscriminator(){
        return discriminator;
    }
}