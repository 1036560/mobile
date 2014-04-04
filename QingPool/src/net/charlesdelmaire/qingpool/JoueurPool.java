package net.charlesdelmaire.qingpool;

public class JoueurPool {
	int idJoueur;
	String nomJoueur;
	int idPool;
	int idPart;

	public JoueurPool() {
	}

	public JoueurPool(String nomJoueur, int idPool, int idPart, int idJoueur) {
		this.idJoueur = idJoueur;
		this.nomJoueur = nomJoueur;
		this.idPool = idPool;
		this.idPart = idPart;
	}

	public void setidJoueur(int unJoueur) {
		this.idJoueur = unJoueur;
	}

	public void setNomJoueur(String nomJoueur) {
		this.nomJoueur = nomJoueur;
	}

	public void setIdPool(int idPool) {
		this.idPool = idPool;
	}

	public void setIdPart(int idPart) {
		this.idPart = idPart;
	}

	public String getNomJoueur() {
		return this.nomJoueur;
	}

	public int getIdJoueur() {
		return this.idJoueur;
	}

	public int getIdPool() {
		return this.idPool;
	}

	public int getIdPart() {
		return this.idPart;
	}
}