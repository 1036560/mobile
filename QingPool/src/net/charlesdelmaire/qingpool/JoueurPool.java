package net.charlesdelmaire.qingpool;

public class JoueurPool{
	String nomJoueur;
	int idPool;
	int idPart;	
	
	public JoueurPool(){		
	}
	
	public JoueurPool(String nomJoueur, int idPool, int idPart){
		this.nomJoueur = nomJoueur;
		this.idPool = idPool;
		this.idPart = idPart;
	}
	
	public void setNomJoueur(String nomJoueur){
		this.nomJoueur = nomJoueur;
	}
	
	public void setIdPool(int idPool){
		this.idPool = idPool;
	}
	
	public void setIdPart(int idPart){
		this.idPart = idPart;
	}
	
	public String getNomJoueur(){
		return this.nomJoueur;
	}
	
	public int getIdPool(){
		return this.idPool;
	}
	
	public int getIdPart(){
		return this.idPart;
	}
}