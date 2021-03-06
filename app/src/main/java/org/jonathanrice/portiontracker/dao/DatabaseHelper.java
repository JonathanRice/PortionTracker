package org.jonathanrice.portiontracker.dao;
/*
* Copyright (C) 2011 Jonathan Rice
* Licensed under the GNU Lesser General Public License (LGPL)
* http://www.gnu.org/licenses/lgpl.html
*/
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.jonathanrice.portiontracker.orm.Portion;
import org.jonathanrice.portiontracker.orm.PortionGoal;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "portionTracker.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 6;

	// the DAO object we use to access the SimpleData table
	private PortionDaoImpl portionDao = null;
	private PortionGoalDaoImpl portionGoalDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create
	 * the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Portion.class);
			TableUtils.createTable(connectionSource, PortionGoal.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
	 * the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Portion.class, true);
			TableUtils.dropTable(connectionSource, PortionGoal.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the Database Access Object (DAO) for our Portion class. It will create it or just give the cached
	 * value.
	 */
	public PortionDaoImpl getPortionDao() throws SQLException {
		if (portionDao == null) {
			portionDao = getDao(Portion.class);
		}
		return portionDao;
	}

	/**
	 * Returns the Database Access Object (DAO) for our Portion class. It will create it or just give the cached
	 * value.
	 */
	public PortionGoalDaoImpl getPortionGoalDao() throws SQLException {
		if (portionGoalDao == null) {
			portionGoalDao = getDao(PortionGoal.class);
		}
		return portionGoalDao;
	}
	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		portionDao = null;
		portionGoalDao = null;
	}
}
