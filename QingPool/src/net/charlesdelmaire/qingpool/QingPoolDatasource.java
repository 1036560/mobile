package net.charlesdelmaire.qingpool;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class QingPoolDatasource {

	private SQLiteDatabase db;
	private UtilitaireBD utildb;
	private String[] colPart = { UtilitaireBD.KEY_ID_PART, UtilitaireBD.KEY_NOM_PART }; 
	private String[] colPool = { UtilitaireBD.KEY_ID_POOL, UtilitaireBD.KEY_NOM_POOL, UtilitaireBD.KEY_ID_PART };
	private String[] colListeJoueur = { UtilitaireBD.KEY_NOM_JOUEUR, UtilitaireBD.KEY_ID_POOL, UtilitaireBD.KEY_ID_PART };
	
	public QingPoolDatasource(Context context) {
		utildb = new UtilitaireBD(context);
	}
	
	public void open() throws SQLException{
		db = this.utildb.getWritableDatabase();
	}
	
	public void close(){
		utildb.close();
	}
	
 // ------------------------ Méthodes de la table PARTICIPANT ----------------//

	public Participant createPart(Participant part) {
       ContentValues values = new ContentValues();
       values.put(UtilitaireBD.KEY_ID_PART, part.getIdPart());
       values.put(UtilitaireBD.KEY_NOM_PART, part.getNomPart());
       long insertID = db.insert(UtilitaireBD.TABLE_PART, null, values);       
       Cursor cursor = db.query(UtilitaireBD.TABLE_PART, colPart, UtilitaireBD.KEY_ID_PART + " = " + insertID, null, null, null, null);
       cursor.moveToFirst();
       Participant newPart = cursorToPart(cursor);
       cursor.close();
       return newPart;
    }

    public List<Participant> getTousPart() {
       List<Participant> parts = new ArrayList<Participant>();       
       Cursor cursor = db.query(UtilitaireBD.TABLE_PART, colPart, null, null, null, null, null);
       cursor.moveToFirst();
       while(!cursor.isAfterLast()) {
    	   Participant unPart = cursorToPart(cursor);
    	   parts.add(unPart);
    	   cursor.moveToNext();
       }
       cursor.close();
       return parts;
    }

   public long getPartCompte() {
       String uneReq = "SELECT  * FROM " + UtilitaireBD.TABLE_PART;       
       Cursor cursor = db.rawQuery(uneReq, null);
       long count = cursor.getCount();
       cursor.close();       
       return count;
   }
   
   public void deletePart(Participant part) {
       long id = part.getIdPart();
       System.out.print("Participant supprimé avec l'identifiant : " + id);
       db.delete(UtilitaireBD.TABLE_PART, UtilitaireBD.KEY_ID_PART + " = " + id, null);
   }
   
   private Participant cursorToPart(Cursor cursor) {
	    Participant newPart = new Participant();
	    newPart.setId(cursor.getLong(0));
	    newPart.setNomPart(cursor.getString(1));
	    return newPart;
   }

  // ------------------------ Méthodes de la table POOL ----------------//

  public Pool createPool(Pool pool) {
	  ContentValues values = new ContentValues();
      values.put(UtilitaireBD.KEY_ID_POOL, pool.getIdPool());
      values.put(UtilitaireBD.KEY_NOM_POOL, pool.getNomPool());
      long insertID = db.insert(UtilitaireBD.TABLE_POOL, null, values);       
      Cursor cursor = db.query(UtilitaireBD.TABLE_POOL, colPool, UtilitaireBD.KEY_ID_POOL + " = " + insertID, null, null, null, null);
      cursor.moveToFirst();
      Pool newPool = cursorToPool(cursor);
      cursor.close();
      return newPool;
  }

  public List<Pool> getTousPool() {
	  List<Pool> pools = new ArrayList<Pool>();       
      Cursor cursor = db.query(UtilitaireBD.TABLE_POOL, colPart, null, null, null, null, null);
      cursor.moveToFirst();
      while(!cursor.isAfterLast()) {
   	   Pool unPool = cursorToPool(cursor);
   	   pools.add(unPool);
   	   cursor.moveToNext();
      }
      cursor.close();
      return pools;
  }
  
  public long getPoolCompte() {
      String uneReq = "SELECT  * FROM " + UtilitaireBD.TABLE_POOL;       
      Cursor cursor = db.rawQuery(uneReq, null);
      long count = cursor.getCount();
      cursor.close();       
      return count;
  }
  
  public void deletePool(Pool pool) {
	  long id = pool.getIdPool();
      System.out.print("Pool supprimé avec l'identifiant : " + id);
      db.delete(UtilitaireBD.TABLE_POOL, UtilitaireBD.KEY_ID_POOL + " = " + id, null);
  }
  
  private Pool cursorToPool(Cursor cursor) {
	    Pool newPool = new Pool();
	    newPool.setIdPool(cursor.getLong(0));
	    newPool.setNomPool(cursor.getString(1));
	    return newPool;
 }

  // ------------------------ Méthodes de la table ListeJoueurPool ----------------//

  public JoueurPool createJoueur(String nomJoueur, long pool_id, long part_id) {
	  ContentValues values = new ContentValues();
      values.put(UtilitaireBD.KEY_NOM_JOUEUR, nomJoueur);
      values.put(UtilitaireBD.KEY_ID_POOL, pool_id);
      values.put(UtilitaireBD.KEY_ID_PART, part_id);
      //long insertID = db.insert(UtilitaireBD.TABLE_LISTE, null, values);       
      Cursor cursor = db.query(UtilitaireBD.TABLE_LISTE, colListeJoueur, UtilitaireBD.KEY_NOM_JOUEUR + " = " + nomJoueur, null, null, null, null);
      cursor.moveToFirst();
      JoueurPool newJoueur = cursorToJoueur(cursor);
      cursor.close();
      return newJoueur;
  }

  public List<JoueurPool> getTousJoueurs() {
	  List<JoueurPool> joueurs = new ArrayList<JoueurPool>();       
      Cursor cursor = db.query(UtilitaireBD.TABLE_LISTE, colListeJoueur, null, null, null, null, null);
      cursor.moveToFirst();
      while(!cursor.isAfterLast()) {
   	   JoueurPool unJoueur = cursorToJoueur(cursor);
   	   joueurs.add(unJoueur);
   	   cursor.moveToNext();
      }
      cursor.close();
      return joueurs;
  }
  
  public List<JoueurPool> getTousJoueursPool(long id_pool) {
	  List<JoueurPool> joueurs = new ArrayList<JoueurPool>();       
      Cursor cursor = db.query(UtilitaireBD.TABLE_LISTE, colListeJoueur, UtilitaireBD.KEY_ID_POOL + " " + id_pool, null, null, null, null);
      cursor.moveToFirst();
      while(!cursor.isAfterLast()) {
   	   JoueurPool unJoueur = cursorToJoueur(cursor);
   	   joueurs.add(unJoueur);
   	   cursor.moveToNext();
      }
      cursor.close();
      return joueurs;
  }
  
  public List<JoueurPool> getTousJoueursPart(long id_part) {
	  List<JoueurPool> joueurs = new ArrayList<JoueurPool>();       
      Cursor cursor = db.query(UtilitaireBD.TABLE_LISTE, colListeJoueur, UtilitaireBD.KEY_ID_POOL + " " + id_part, null, null, null, null);
      cursor.moveToFirst();
      while(!cursor.isAfterLast()) {
   	   JoueurPool unJoueur = cursorToJoueur(cursor);
   	   joueurs.add(unJoueur);
   	   cursor.moveToNext();
      }
      cursor.close();
      return joueurs;
  }
  
  public void deleteJoueur(JoueurPool nomJoueur) {	  
      System.out.print("Pool supprimé avec l'identifiant : " + nomJoueur);
      db.delete(UtilitaireBD.TABLE_LISTE, UtilitaireBD.KEY_NOM_JOUEUR + " = " + nomJoueur, null);
  }
  
  private JoueurPool cursorToJoueur(Cursor cursor) {
	    JoueurPool newJoueur = new JoueurPool();
	    newJoueur.setNomJoueur(cursor.getString(0));
	    newJoueur.setIdPool(cursor.getLong(1));
	    newJoueur.setIdPart(cursor.getLong(2));
	    return newJoueur;
  }  
}