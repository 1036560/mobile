package net.charlesdelmaire.qingpool;

public class Pool {
	
	//Attributs
	int idPool;
	String nomPool;
	int idPart;
	String motDePasse;
	int nbMaxPart;

	//Constructeur par défaut
	public Pool() {
	}

	//Constructeur
	public Pool(int idPool, String nomPool, int idPart, String motDePasse,
			int nbNaxPart) {
		this.idPool = idPool;
		this.nomPool = nomPool;
		this.idPart = idPart;
		this.motDePasse = motDePasse;
		this.nbMaxPart = nbNaxPart;
	}

	//Get Set
	public void setIdPool(int idPool) {
		this.idPool = idPool;
	}

	public void setNomPool(String nomPool) {
		this.nomPool = nomPool;
	}

	public void setIdPart(int idPart) {
		this.idPart = idPart;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public void setNbMaxPart(int nbMaxPart) {
		this.nbMaxPart = nbMaxPart;
	}

	public int getIdPool() {
		return this.idPool;
	}

	public String getNomPool() {
		return this.nomPool;
	}

	public int getIdPart() {
		return this.idPart;
	}

	public String getMotDePasse() {
		return this.motDePasse;
	}

	public int getNbMaxPart() {
		return nbMaxPart;
	}

}