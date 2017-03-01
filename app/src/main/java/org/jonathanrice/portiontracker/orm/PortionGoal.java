package org.jonathanrice.portiontracker.orm;
/*
* Copyright (C) 2011 Jonathan Rice
* Licensed under the GNU Lesser General Public License (LGPL)
* http://www.gnu.org/licenses/lgpl.html
*/
import java.util.Calendar;
import java.util.Date;

import org.jonathanrice.portiontracker.dao.PortionGoalDaoImpl;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * A simple demonstration object we are creating and persisting to the database.
 */
@DatabaseTable(daoClass = PortionGoalDaoImpl.class)
public class PortionGoal extends AbstractPortion {

	@DatabaseField(index = true)
	protected String goalName;
	@DatabaseField
	protected Date dayToComplete;
	
	public PortionGoal() {
		// needed by ormlite
		Calendar cal = Calendar.getInstance();
		cal.setTime( new Date() );
		cal.add( Calendar.DATE, 90 );
		setDayToComplete(new Date(cal.getTimeInMillis()));
	}

	public String getGoalName() {
		return goalName;
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	public Date getDayToComplete() {
		if (dayToComplete == null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime( new Date() );
			cal.add( Calendar.DATE, 90 );
			setDayToComplete(new Date(cal.getTimeInMillis()));
		}
		return dayToComplete;
	}

	public void setDayToComplete(Date dayToComplete) {
		this.dayToComplete = dayToComplete;
	}

}
