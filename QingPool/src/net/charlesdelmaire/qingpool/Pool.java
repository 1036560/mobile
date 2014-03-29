package net.charlesdelmaire.qingpool;

public class Pool{
	long idPool;
	String nomPool;
	long idPart;
	
	
	public Pool(){}
	
	public Pool(long idPool, String nomPool, long idPart){
		this.idPool = idPool;
		this.nomPool = nomPool;
		this.idPart = idPart;		
	}
	
	public void setIdPool(long idPool) {
		this.idPool = idPool;
	}
	
	public void setNomPool(String nomPool){
		this.nomPool = nomPool;
	}
	
	public void setIdPart(long idPart){
		this.idPart = idPart;
	}
	
	public long getIdPool(){
		return this.idPool;
	}
	
	public String getNomPool(){
		return this.nomPool;
	}
	
	public long getIdPart(){
		return this.idPart;
	}
}