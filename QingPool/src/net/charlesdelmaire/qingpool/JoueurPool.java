package net.charlesdelmaire.qingpool;

public class JoueurPool{
	String nomJoueur;
	long idPool;
	long idPart;	
	
	public JoueurPool(){		
	}
	
	public JoueurPool(String nomJoueur, long idPool, long idPart){
		this.nomJoueur = nomJoueur;
		this.idPool = idPool;
		this.idPart = idPart;
	}
	
	public void setNomJoueur(String nomJoueur){
		this.nomJoueur = nomJoueur;
	}
	
	public void setIdPool(long idPool){
		this.idPool = idPool;
	}
	
	public void setIdPart(long idPart){
		this.idPart = idPart;
	}
	
	public String getNomJoueur(){
		return this.nomJoueur;
	}
	
	public long getIdPool(){
		return this.idPool;
	}
	
	public long getIdPart(){
		return this.idPart;
	}
}