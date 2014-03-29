package net.charlesdelmaire.qingpool;

public class Participant{
	private long idPart;
	private String nomPart;
	
	
	public Participant(){}
	
	public Participant(long idPart, String nomPart){
		this.idPart = idPart;
		this.nomPart = nomPart;
		
	}
	
	public void setId(long idPart) {
		this.idPart = idPart;
	}
	
	public void setNomPart(String nomPart){
		this.nomPart = nomPart;
	}
	
	public long getIdPart(){
		return this.idPart;
	}
	
	public String getNomPart(){
		return this.nomPart;
	}
}