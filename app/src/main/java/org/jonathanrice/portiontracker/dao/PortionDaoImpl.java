package org.jonathanrice.portiontracker.dao;
/*
* Copyright (C) 2011 Jonathan Rice
* Licensed under the GNU Lesser General Public License (LGPL)
* http://www.gnu.org/licenses/lgpl.html
*/
import java.util.Calendar;
import java.util.Date;
import java.sql.SQLException;
import java.util.List;

import org.jonathanrice.portiontracker.orm.Portion;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

public class PortionDaoImpl extends BaseDaoImpl<Portion, Integer> {

	public PortionDaoImpl(Class<Portion> dataClass) throws SQLException {
		super(dataClass);
	}

	public PortionDaoImpl(ConnectionSource connectionSource,
			Class<Portion> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public PortionDaoImpl(ConnectionSource connectionSource,
			DatabaseTableConfig<Portion> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
	}
	
	public Portion selectOrCreatePortionForDay(Date day) throws SQLException {
		List<Portion> portionList = this.queryForEq("day", stripTimeFromDate(day));
		if (portionList != null && portionList.size() > 0) {
			return portionList.get(0);
		}
		return createPortionForDay(stripTimeFromDate(day));
	}
	
	private Portion createPortionForDay(Date day) throws SQLException {
		Portion portion = new Portion();
		
		portion.setDay(day);
		portion.setWater(0);
		portion.setProtein(0);
		portion.setDairy(0);
		portion.setFruit(0);
		portion.setVegetable(0);
		portion.setFat(0);
		portion.setCarb(0);
		portion.setSnack(0);
		portion.setCondiment(0);
		
		create(portion);
		return portion;
	}
	
	public static Date stripTimeFromDate(Date date) {	  
		// Get Calendar object set to the date and time of the given Date object  
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);  
		  
		// Set time fields to zero  
		cal.set(Calendar.HOUR_OF_DAY, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MILLISECOND, 0);  		  
		// Put it back in the Date object  
		return cal.getTime();
	}

}
