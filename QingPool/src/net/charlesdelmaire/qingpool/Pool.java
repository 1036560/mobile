package net.charlesdelmaire.qingpool;

public class Pool{
	int idPool;
	String nomPool;
	int idPart;
	
	
	public Pool(){}
	
	public Pool(int idPool, String nomPool, int idPart){
		this.idPool = idPool;
		this.nomPool = nomPool;
		this.idPart = idPart;		
	}
	
	public void setIdPool(int idPool) {
		this.idPool = idPool;
	}
	
	public void setNomPool(String nomPool){
		this.nomPool = nomPool;
	}
	
	public void setIdPart(int idPart){
		this.idPart = idPart;
	}
	
	public int getIdPool(){
		return this.idPool;
	}
	
	public String getNomPool(){
		return this.nomPool;
	}
	
	public int getIdPart(){
		return this.idPart;
	}
}