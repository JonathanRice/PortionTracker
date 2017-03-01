package org.jonathanrice.portiontracker.orm;
/*
* Copyright (C) 2011 Jonathan Rice
* Licensed under the GNU Lesser General Public License (LGPL)
* http://www.gnu.org/licenses/lgpl.html
*/
import java.util.Date;

import org.jonathanrice.portiontracker.dao.PortionDaoImpl;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * A simple demonstration object we are creating and persisting to the database.
 */
@DatabaseTable(daoClass = PortionDaoImpl.class)
public class Portion extends AbstractPortion {

	@DatabaseField
	protected Date day;
	
	public Portion() {
		// needed by ormlite
		this.setDay(new Date());
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}
}
