package org.jonathanrice.portiontracker.dao;
/*
* Copyright (C) 2011 Jonathan Rice
* Licensed under the GNU Lesser General Public License (LGPL)
* http://www.gnu.org/licenses/lgpl.html
*/
import java.sql.SQLException;
import java.util.List;

import org.jonathanrice.portiontracker.orm.PortionGoal;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

public class PortionGoalDaoImpl extends BaseDaoImpl<PortionGoal, Integer> {

	public PortionGoalDaoImpl(Class<PortionGoal> dataClass) throws SQLException {
		super(dataClass);
	}

	public PortionGoalDaoImpl(ConnectionSource connectionSource,
			Class<PortionGoal> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public PortionGoalDaoImpl(ConnectionSource connectionSource,
			DatabaseTableConfig<PortionGoal> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
	}

	public PortionGoal selectOrCreateGoal() throws SQLException {
		List<PortionGoal> portionGoalList = queryForAll();		
		if (portionGoalList != null && portionGoalList.size() > 0) {
			return portionGoalList.get(0);
		}		
		return createDefaultGoal();
	}

	protected PortionGoal createDefaultGoal() throws SQLException {
		PortionGoal portionGoal = new PortionGoal();
		portionGoal.setGoalName("default");
		portionGoal.setWater(8);
		portionGoal.setProtein(7);
		portionGoal.setDairy(3);
		portionGoal.setFruit(1);
		portionGoal.setVegetable(4);
		portionGoal.setFat(1);
		portionGoal.setCarb(1);
		portionGoal.setSnack(3);
		portionGoal.setCondiment(2);
		create(portionGoal);
		return portionGoal;
	}
}
