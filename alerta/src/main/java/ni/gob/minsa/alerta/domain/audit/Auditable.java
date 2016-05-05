package ni.gob.minsa.alerta.domain.audit;

public interface Auditable {
	
	public boolean isFieldAuditable(String fieldname);

    public String getActor();

    public void setActor(String actor);

}
