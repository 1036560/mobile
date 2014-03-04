package net.charlesdelmaire.qingpool;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class UtilitaireBD extends SQLiteOpenHelper {
	
    
    private static final String LOG = "DatabaseHelper";
 
    // Version
    private static final int DATABASE_VERSION = 1;
 
    // Nom
    private static final String DATABASE_NAME = "qingPool";
 
    // Tables
    private static final String TABLE_PART = "participant";
    private static final String TABLE_POOL = "pool";
    private static final String TABLE_LISTE = "listeJoueurPool";
 
    // Colonnes communes
    private static final String KEY_ID_PART = "idPart";
    private static final String KEY_ID_POOL = "idPool";
 
    // PARTICIPANT
    private static final String KEY_NOM_PART = "nomParticipant";
 
    // POOL
    private static final String KEY_NOM_POOL = "nomPool";
 
    // LISTEJOUEURPOOL
    private static final String KEY_NOM_JOUEUR = "nomJoueur";    
 
    // Création des tables
    // PARTICIPANT
    private static final String CREATE_TABLE_PART = "CREATE TABLE "
            + TABLE_PART + "(" + KEY_ID_PART + " INTEGER PRIMARY KEY," + KEY_NOM_PART
            + " TEXT" + ")";
 
    // POOL
    private static final String CREATE_TABLE_POOL = "CREATE TABLE " + TABLE_POOL
            + "(" + KEY_ID_POOL + " INTEGER PRIMARY KEY," + KEY_NOM_POOL + " TEXT," 
    		+ KEY_ID_PART + " INTEGER FOREIGN KEY" + ")";
 
    // LISTEJOUEURPOOL
    private static final String CREATE_TABLE_LISTE = "CREATE TABLE "
            + TABLE_LISTE + "(" + KEY_NOM_JOUEUR + " TEXT PRIMARY KEY,"
            + KEY_ID_POOL + " INTEGER," + KEY_ID_PART + " INTEGER,"
            + ")";
 
    public UtilitaireBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
 
        // creating required tables
        db.execSQL(CREATE_TABLE_PART);
        db.execSQL(CREATE_TABLE_POOL);
        db.execSQL(CREATE_TABLE_LISTE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POOL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTE);
 
        // create new tables
        onCreate(db);
    }
    
 // ------------------------ Méthodes de la table PARTICIPANT ----------------//
    
    /**
    * Création d'un participant
    */
   public long createPart(Participant part, String nomPart) {
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues values = new ContentValues();
       values.put(KEY_ID_PART, part.getIdPart());
       values.put(KEY_NOM_PART, part.getNomPart());       

       // insert row
       long part_id = db.insert(TABLE_PART, null, values);
       

       return part_id;
   }

   /**
    * Retourne un participant
    */
   public Participant getPart(long part_id) {
       SQLiteDatabase db = this.getReadableDatabase();

       String uneReq = "SELECT  * FROM " + TABLE_PART + " WHERE "
               + KEY_ID_PART + " = " + part_id;

       Log.e(LOG, uneReq);

       Cursor c = db.rawQuery(uneReq, null);

       if (c != null)
           c.moveToFirst();

       Participant part = new Participant();
       part.setId(c.getInt(c.getColumnIndex(KEY_ID_PART)));
       part.setNomPart((c.getString(c.getColumnIndex(KEY_NOM_PART))));
       

       return part;
   }

   /**
    * Retourne une liste de tous les participants enregistrés
    * */
   public List<Participant> getTousPart() {
       List<Participant> parts = new ArrayList<Participant>();
       String uneReq = "SELECT  * FROM " + TABLE_PART;

       Log.e(LOG, uneReq);

       SQLiteDatabase db = this.getReadableDatabase();
       Cursor c = db.rawQuery(uneReq, null);

       // looping through all rows and adding to list
       if (c.moveToFirst()) {
           do {
        	   Participant part = new Participant();
               part.setId(c.getInt(c.getColumnIndex(KEY_ID_PART)));
               part.setNomPart((c.getString(c.getColumnIndex(KEY_NOM_PART))));

               // adding to todo list
               parts.add(part);
           } while (c.moveToNext());
       }

       return parts;
   }

   /**
    * Retourne une liste de tous les participants d'un pool
    * */
   public List<String> getTousPartPool(String nom_pool) {
       List<String> parts = new ArrayList<String>();

       String uneReq = "SELECT  nomParticipant FROM " + TABLE_PART + " pt, "
               + TABLE_POOL + " po, " + " WHERE po."
               + KEY_ID_PART + " = pt." + KEY_ID_PART;
               

       Log.e(LOG, uneReq);

       SQLiteDatabase db = this.getReadableDatabase();
       Cursor c = db.rawQuery(uneReq, null);

       // looping through all rows and adding to list
       if (c.moveToFirst()) {
           do {
               String nomPart;
               
               nomPart = (c.getString(c.getColumnIndex(KEY_NOM_PART)));
               

               // adding to todo list
               parts.add(nomPart);
           } while (c.moveToNext());
       }

       return parts;
   }

   /**
    * Retourne le nombre de participant(s) pour un pool
    */
   public int getPartCompte() {
       String uneReq = "SELECT  * FROM " + TABLE_PART + " pt, "
               + TABLE_POOL + " po, " + " WHERE po."
               + KEY_ID_PART + " = pt." + KEY_ID_PART;
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.rawQuery(uneReq, null);

       int count = cursor.getCount();
       cursor.close();

       // return count
       return count;
   }

   /**
    * Updating a todo
    
   public int updateToDo(Todo todo) {
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues values = new ContentValues();
       values.put(KEY_TODO, todo.getNote());
       values.put(KEY_STATUS, todo.getStatus());

       // updating row
       return db.update(TABLE_TODO, values, KEY_ID + " = ?",
               new String[] { String.valueOf(todo.getId()) });
   }*/

   /**
    * Deleting a todo
    */
   public void deletePart(long part_id) {
       SQLiteDatabase db = this.getWritableDatabase();
       db.delete(TABLE_PART, KEY_ID_PART + " = ?",
               new String[] { String.valueOf(part_id) });
   }

   // ------------------------ Méthodes de la table POOL ----------------//

   /**
    * Création du pool
    */
   public long createPool(Pool pool) {
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues values = new ContentValues();
       values.put(KEY_ID_POOL, pool.getIdPool());
       values.put(KEY_NOM_POOL, pool.getNomPool());
       values.put(KEY_ID_PART, pool.getIdPart());

       // insert row
       long pool_id = db.insert(TABLE_POOL, null, values);

       return pool_id;
   }

   /**
    * getting all tags
    * */
   public List<Pool> getTousPool() {
       List<Pool> pools = new ArrayList<Pool>();
       String uneReq = "SELECT  * FROM " + TABLE_POOL;

       Log.e(LOG, uneReq);

       SQLiteDatabase db = this.getReadableDatabase();
       Cursor c = db.rawQuery(uneReq, null);

       // looping through all rows and adding to list
       if (c.moveToFirst()) {
           do {
               Pool po = new Pool();
               po.setIdPool(c.getInt((c.getColumnIndex(KEY_ID_POOL))));
               po.setNomPool(c.getString(c.getColumnIndex(KEY_NOM_POOL)));
               po.setIdPart(c.getInt(c.getColumnIndex(KEY_ID_PART)));

               // adding to tags list
               pools.add(po);
           } while (c.moveToNext());
       }
       return pools;
   }

   /**
    * Mise-à-jour d'un Pool
    */
   public int updatePool(Pool pool) {
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues values = new ContentValues();
       values.put(KEY_ID_POOL, pool.getIdPool());
       values.put(KEY_NOM_POOL, pool.getNomPool());
       values.put(KEY_ID_PART, pool.getIdPart());

       // updating row
       return db.update(TABLE_POOL, values, KEY_ID_POOL + " = ?",
               new String[] { String.valueOf(pool.getIdPool()) });
   }

   /**
    * Deleting a tag
    */
   public void deletePool(Pool pool) {
       SQLiteDatabase db = this.getWritableDatabase();
       // now delete the tag
       db.delete(TABLE_POOL, KEY_ID_POOL + " = ?",
               new String[] { String.valueOf(pool.getIdPool()) });
   }

   // ------------------------ Méthodes de la table ListeJoueurPool ----------------//

   /**
    * Création de la table
    */
   public long createListeJoueur(String nomJoueur, long pool_id, long part_id) {
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues values = new ContentValues();
       values.put(KEY_NOM_JOUEUR, nomJoueur);
       values.put(KEY_ID_POOL, pool_id);
       values.put(KEY_ID_PART, part_id);

       long id = db.insert(TABLE_LISTE, null, values);

       return id;
   }

   /**
    * Mise-à-jour de la liste des joueurs
    */
   public int updateListeJoueur(String nomJoueur, long pool_id, long part_id) {
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues values = new ContentValues();
       values.put(KEY_NOM_JOUEUR, nomJoueur);
       values.put(KEY_ID_POOL, pool_id);
       values.put(KEY_ID_PART, part_id);

       // updating row
       return db.update(TABLE_LISTE, values, KEY_NOM_JOUEUR + " = ?",
               new String[] { String.valueOf(nomJoueur) });
   }

   /**
    * Suppression d'un joueur
    */
   public void deleteJoueur(String nomJoueur) {
       SQLiteDatabase db = this.getWritableDatabase();
       db.delete(TABLE_LISTE, KEY_NOM_JOUEUR + " = ?",
               new String[] { String.valueOf(nomJoueur) });
   }

   // closing database
   public void closeDB() {
       SQLiteDatabase db = this.getReadableDatabase();
       if (db != null && db.isOpen())
           db.close();
   }   
}