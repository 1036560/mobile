package net.charlesdelmaire.qingpool;

import net.charlesdelmaire.*;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class UtilitaireBD extends SQLiteOpenHelper {    
    // Version
    private static final int DATABASE_VERSION = 1;
 
    // Nom
    private static final String DATABASE_NAME = "qingPool.db";
 
    // Tables
    public static final String TABLE_PART = "participant";
    public static final String TABLE_POOL = "pool";
    public static final String TABLE_LISTE = "listeJoueurPool";
 
    // Colonnes communes
    public static final String KEY_ID_PART = "idPart";
    public static final String KEY_ID_POOL = "idPool";
 
    // PARTICIPANT
    public static final String KEY_NOM_PART = "nomParticipant";
 
    // POOL
    public static final String KEY_NOM_POOL = "nomPool";
 
    // LISTEJOUEURPOOL
    public static final String KEY_NOM_JOUEUR = "nomJoueur";    
 
    // Création des tables
    // PARTICIPANT
    private static final String CREATE_TABLE_PART = "CREATE TABLE "
            + TABLE_PART + "(" + KEY_ID_PART + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NOM_PART
            + " TEXT NOT NULL" + ");";
 
    // POOL
    private static final String CREATE_TABLE_POOL = "CREATE TABLE " + TABLE_POOL
            + "(" + KEY_ID_POOL + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NOM_POOL + " TEXT," 
    		+ KEY_ID_PART + " INTEGER FOREIGN KEY" + ");";
 
    // LISTEJOUEURPOOL
    private static final String CREATE_TABLE_LISTE = "CREATE TABLE "
            + TABLE_LISTE + "(" + KEY_NOM_JOUEUR + " TEXT PRIMARY KEY NOT NULL,"
            + KEY_ID_POOL + " INTEGER," + KEY_ID_PART + " INTEGER,"
            + ");";
 
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
}