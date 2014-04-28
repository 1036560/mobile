package net.charlesdelmaire.qingpool;

public class JoueurPool {
	int idJoueur;
	String nomJoueur;
	int idPool;
	int idPart;
	String idJoueur1;

	public JoueurPool() {
	}

	public JoueurPool(String nomJoueur, String idJoueur1, int idPool,
			int idPart, int idJoueur) {
		this.idJoueur = idJoueur;
		this.nomJoueur = nomJoueur;
		this.idJoueur1 = idJoueur1;
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

	public void setIdJoueur1(String idJoueur1) {
		this.idJoueur1 = idJoueur1;
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

	public String getIdJoueur1() {
		return idJoueur1;
	}
}