package net.charlesdelmaire.qingpool;

public class Participant{
	private int idPart;
	private String nomPart;
	
	
	public Participant(){}
	
	public Participant(int idPart, String nomPart){
		this.idPart = idPart;
		this.nomPart = nomPart;
		
	}
	
	public void setId(int idPart) {
		this.idPart = idPart;
	}
	
	public void setNomPart(String nomPart){
		this.nomPart = nomPart;
	}
	
	public int getIdPart(){
		return this.idPart;
	}
	
	public String getNomPart(){
		return this.nomPart;
	}
}