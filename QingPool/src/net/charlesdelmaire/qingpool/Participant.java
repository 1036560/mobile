package net.charlesdelmaire.qingpool;

public class Participant {
	
	//Attributs
	private int idPart;
	private String nomPart;
	private String imgPart;
	private String lang;

	//Constructeur par d�faut
	public Participant() {
	}

	//Constructeur
	public Participant(int idPart, String nomPart, String imgPart, String lang) {
		this.idPart = idPart;
		this.nomPart = nomPart;
		this.imgPart = imgPart;
		this.lang = lang;
	}

	//Get Set
	public void setId(int idPart) {
		this.idPart = idPart;
	}

	public void setNomPart(String nomPart) {
		this.nomPart = nomPart;
	}

	public void setImgPart(String imgPart) {
		this.imgPart = imgPart;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public int getIdPart() {
		return this.idPart;
	}

	public String getNomPart() {
		return this.nomPart;
	}

	public String getImgPart() {
		return imgPart;
	}

	public String getLang() {
		return lang;
	}

}