package net.charlesdelmaire.qingpool;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QingPoolDatasource {

	// Version
	private static final int DATABASE_VERSION = 1;

	// Nom
	private static final String DATABASE_NAME = "qingPool";

	// Tables
	public static final String TABLE_PART = "participant";
	public static final String TABLE_POOL = "Pool";
	public static final String TABLE_LISTE = "listeJoueurPool";

	// PARTICIPANT
	public static final String COL_ID_PART = "idPart";
	public static final String COL_NOM_PART = "nomParticipant";

	// POOL
	public static final String COL_ID_POOL = "idPool";
	public static final String COL_NOM_POOL = "nomPool";
	public static final String COL_ID_PART_POOL = "idPart";

	// LISTEJOUEURPOOL
	public static final String COL_NOM_JOUEUR = "nomJoueur";
	public static final String COL_ID_POOL_JOUEUR = "idPool";
	public static final String COL_ID_PART_JOUEUR = "idPart";

	// INDICES DES COLONNES TABLE PART
	public static final int IDX_ID_PART = 0;
	public static final int IDX_NOM_PART = 1;

	// INDICES DES COLONNES TABLE POOL
	public static final int IDX_ID_POOL = 0;
	public static final int IDX_NOM_POOL = 1;
	public static final int IDX_ID_PART_POOL = 2;

	// INDICES DES COLONNES TABLE LISTEJOUEURPOOL
	public static final int IDX_NOM_JOUEUR = 0;
	public static final int IDX_ID_POOL_JOUEUR = 1;
	public static final int IDX_ID_PART_JOUEUR = 2;
	private SQLiteDatabase db;
	private QingPoolDbHelper utildb;

	// private String[] colPart = { UtilitaireBD.COL_ID_PART,
	// UtilitaireBD.COL_NOM_PART };
	// private String[] colPool = { UtilitaireBD.COL_ID_POOL,
	// UtilitaireBD.COL_NOM_POOL, UtilitaireBD.COL_ID_PART_POOL };
	// private String[] colListeJoueur = { UtilitaireBD.COL_NOM_JOUEUR,
	// UtilitaireBD.COL_ID_POOL_JOUEUR, UtilitaireBD.COL_ID_PART_JOUEUR };

	public QingPoolDatasource(Context context) {
		utildb = new QingPoolDbHelper(context);
	}

	public void open() throws SQLException {
		db = this.utildb.getWritableDatabase();
	}

	public void close() {
		utildb.close();
	}

	// ------------------------ Méthodes de la table PARTICIPANT
	// ----------------//

	public int createPart(Participant part) {
		ContentValues values = partToContentValues(part);
		int newID = (int) db.insert(TABLE_PART, null, values);
		part.setId(newID);
		return newID;
		/*
		 * values.put(QingPoolDbHelper.COL_ID_PART, part.getIdPart());
		 * values.put(QingPoolDbHelper.COL_NOM_PART, part.getNomPart()); int
		 * insertID = (int)db.insert(QingPoolDbHelper.TABLE_PART, null, values);
		 * Cursor cursor = db.query(QingPoolDbHelper.TABLE_PART, null,
		 * QingPoolDbHelper.COL_ID_PART + " = " + insertID, null, null, null,
		 * null); cursor.moveToFirst(); Participant newPart =
		 * cursorToPart(cursor); cursor.close(); return newPart;
		 */
	}

	private ContentValues partToContentValues(Participant part) {
		ContentValues values = new ContentValues();
		values.put(COL_ID_PART, part.getIdPart());
		values.put(COL_NOM_PART, part.getNomPart());
		return values;
	}

	public Participant getParticipant(int part_id) {
		Participant part = null;
		Cursor cursor = db.query(TABLE_PART, null, COL_ID_PART + "=" + part_id,
				null, null, null, null);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			part = cursorToPart(cursor);
		}
		return part;
	}

	public List<Participant> getTousPart() {
		List<Participant> parts = new ArrayList<Participant>();
		Cursor cursor = db
				.query(TABLE_PART, null, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Participant unPart = cursorToPart(cursor);
			parts.add(unPart);
			cursor.moveToNext();
		}
		cursor.close();
		return parts;
	}

	public int getPartCompte() {
		String uneReq = "SELECT  * FROM " + TABLE_PART;
		Cursor cursor = db.rawQuery(uneReq, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	public void deletePart(Participant part) {
		db.delete(TABLE_PART, COL_ID_POOL + "=" + part.getIdPart(), null);
	}

	private Participant cursorToPart(Cursor cursor) {
		Participant newPart = new Participant();
		newPart.setId(cursor.getInt(0));
		newPart.setNomPart(cursor.getString(1));
		return newPart;
	}

	private void update(Participant part) {
		ContentValues values = partToContentValues(part);
		db.update(TABLE_PART, values, COL_ID_PART + "=" + part.getIdPart(),
				null);
	}

	// ------------------------ Mï¿½thodes de la table POOL ----------------//

	public int createPool(Pool pool) {
		ContentValues values = poolToContentValues(pool);
		int newID = (int) db.insert(TABLE_POOL, null, values);
		pool.setIdPool(newID);
		return newID;
		/*
		 * ContentValues values = new ContentValues();
		 * values.put(QingPoolDbHelper.COL_ID_POOL, pool.getIdPool());
		 * values.put(QingPoolDbHelper.COL_NOM_POOL, pool.getNomPool()); int
		 * insertID = db.insert(QingPoolDbHelper.TABLE_POOL, null, values);
		 * Cursor cursor = db.query(QingPoolDbHelper.TABLE_POOL, colPool,
		 * QingPoolDbHelper.COL_ID_POOL + " = " + insertID, null, null, null,
		 * null); cursor.moveToFirst(); Pool newPool = cursorToPool(cursor);
		 * cursor.close(); return newPool;
		 */
	}

	private ContentValues poolToContentValues(Pool pool) {
		ContentValues values = new ContentValues();
		values.put(COL_ID_POOL, pool.getIdPool());
		values.put(COL_NOM_POOL, pool.getNomPool());
		return values;
	}

	public List<Pool> getTousPool() {
		List<Pool> pools = new ArrayList<Pool>();
		Cursor cursor = db
				.query(TABLE_POOL, null, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Pool unPool = cursorToPool(cursor);
			pools.add(unPool);
			cursor.moveToNext();
		}
		cursor.close();
		return pools;
	}

	public Pool getPool(int pool_id) {
		Pool pool = null;
		Cursor cursor = db.query(TABLE_POOL, null, COL_ID_POOL + "=" + pool_id,
				null, null, null, null);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			pool = cursorToPool(cursor);
		}
		return pool;
	}

	public int getPoolCompte() {
		String uneReq = "SELECT  * FROM " + TABLE_POOL;
		Cursor cursor = db.rawQuery(uneReq, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	public void deletePool(Pool pool) {
		int id = pool.getIdPool();
		System.out.print("Pool supprimï¿½ avec l'identifiant : " + id);
		db.delete(TABLE_POOL, COL_ID_POOL + " = " + id, null);
	}

	public int verifPart(String nomPart) {
		String uneReq = "SELECT  * FROM " + TABLE_PART;
		String unNom;

		Cursor cursor = db.rawQuery(uneReq, null);
		cursor.moveToFirst();
		while (cursor.isLast()) {
			unNom = cursor.getString(IDX_NOM_PART);
			if (unNom.equals(nomPart)) {
				return cursor.getInt(IDX_ID_PART);
			}
		}
		return -1;
	}

	private Pool cursorToPool(Cursor cursor) {
		Pool newPool = new Pool();
		newPool.setIdPool(cursor.getInt(IDX_ID_POOL));
		newPool.setNomPool(cursor.getString(IDX_NOM_POOL));
		return newPool;
	}

	private void update(Pool pool) {
		ContentValues values = poolToContentValues(pool);
		db.update(TABLE_POOL, values, COL_ID_POOL + "=" + pool.getIdPool(),
				null);
	}

	public void createJoueur(JoueurPool unJou) {
		ContentValues values = joueurToContentValues(unJou);
		db.insert(TABLE_LISTE, null, values);
		
	}
		
	private ContentValues joueurToContentValues(JoueurPool unJou) {
		ContentValues values = new ContentValues();
		values.put(COL_NOM_JOUEUR, unJou.getNomJoueur());
		values.put(COL_ID_POOL_JOUEUR, unJou.getIdPool());
		values.put(COL_ID_PART_JOUEUR, unJou.getIdPart());
		return values;
	}
	private JoueurPool cursorToJoueur(Cursor cursor) {
		JoueurPool newJoueur = new JoueurPool();
		newJoueur.setNomJoueur(cursor.getString(IDX_NOM_JOUEUR));
		newJoueur.setIdPool(cursor.getInt(IDX_ID_POOL_JOUEUR));
		return newJoueur;
	}

	private static class QingPoolDbHelper extends SQLiteOpenHelper {

		public QingPoolDbHelper(Context context) {
			super(context, "qingPool.sqlite", null, DATABASE_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_POOL + "(" + COL_ID_POOL
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOM_POOL
					+ " TEXT, " + COL_ID_PART_POOL + " INTEGER" + ")");

			db.execSQL("create table " + TABLE_PART + "(" + COL_ID_PART
					+ " integer primary key autoincrement, " + COL_NOM_PART
					+ " text)");

			db.execSQL("create table " + TABLE_LISTE + "(" + COL_NOM_JOUEUR
					+ " text primary key, " + COL_ID_POOL_JOUEUR + " integer, "
					+ COL_ID_PART_JOUEUR + " integer)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// on upgrade drop older tables
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_POOL);

			// create new tables
			onCreate(db);
		}
	}
}