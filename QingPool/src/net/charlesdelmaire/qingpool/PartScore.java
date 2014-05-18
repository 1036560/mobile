package net.charlesdelmaire.qingpool;

public class PartScore {

	int id;
	int idPool;
	int idPart;
	int score;

	// Constructeur

	public PartScore() {

	}

	public PartScore(int id, int idPool, int idPart, int score) {
		this.id = id;
		this.idPool = idPool;
		this.idPart = idPart;
		this.score = score;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdPool() {
		return idPool;
	}

	public void setIdPool(int idPool) {
		this.idPool = idPool;
	}

	public int getIdPart() {
		return idPart;
	}

	public void setIdPart(int idPart) {
		this.idPart = idPart;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
