package org.jonathanrice.portiontracker.orm;
/*
* Copyright (C) 2011 Jonathan Rice
* Licensed under the GNU Lesser General Public License (LGPL)
* http://www.gnu.org/licenses/lgpl.html
*/
import java.lang.reflect.Field;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public abstract class AbstractPortion {

	@DatabaseField(generatedId = true)
	protected int id;
	@DatabaseField
	protected long weight;
	@DatabaseField
	protected long water;
	@DatabaseField
	protected long protein;
	@DatabaseField
	protected long dairy;
	@DatabaseField
	protected long fruit;
	@DatabaseField
	protected long vegetable;
	@DatabaseField
	protected long fat;
	@DatabaseField
	protected long carb;
	@DatabaseField
	protected long snack;
	@DatabaseField
	protected long condiment;
	@DatabaseField
	protected Date createdDate;
	@DatabaseField
	protected Date modifiedDate;

	public AbstractPortion() {
		super();
		this.setCreatedDate(new Date());
		this.setModifiedDate(new Date());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}
	
	public long getWater() {
		return water;
	}

	public void setWater(long water) {
		this.water = water;
	}

	public long getProtein() {
		return protein;
	}

	public void setProtein(long protein) {
		this.protein = protein;
	}

	public long getDairy() {
		return dairy;
	}

	public void setDairy(long dairy) {
		this.dairy = dairy;
	}

	public long getFruit() {
		return fruit;
	}

	public void setFruit(long fruit) {
		this.fruit = fruit;
	}

	public long getVegetable() {
		return vegetable;
	}

	public void setVegetable(long vegetable) {
		this.vegetable = vegetable;
	}

	public long getFat() {
		return fat;
	}

	public void setFat(long fat) {
		this.fat = fat;
	}

	public long getCarb() {
		return carb;
	}

	public void setCarb(long carb) {
		this.carb = carb;
	}

	public long getSnack() {
		return snack;
	}

	public void setSnack(long snack) {
		this.snack = snack;
	}

	public long getCondiment() {
		return condiment;
	}

	public void setCondiment(long condiment) {
		this.condiment = condiment;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {		
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public void setLong(String search, long value) {
		setLong(search, value, this.getClass());
		
	}
	protected void setLong(String search, long value, Class<?> clazz) {
		if (clazz == null) return;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			if (nameMatches(search, fieldName)) {
				try {
					field.setLong(this, value);
					return;
				} catch (IllegalArgumentException e) {
					//Don't care, skip
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					//Don't care, skip					
				}
			}
		}
		Class<?> superClass = clazz.getSuperclass();
	    if (superClass != null) {
	    	setLong(search, value, superClass);
	    	return;
	    }
	}
	
	public long getLong(String search) {
		return getLong(search, this.getClass());
	}
	
	// This returns Long.MIN_VALUE if nothing is found
	protected long getLong(String search, Class<?> clazz) {
		if (clazz == null) return Long.MIN_VALUE;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldName = field.getName();
				//Type type = field.getGenericType();
				if (nameMatches(search, fieldName)) {
					return field.getLong(this);
				}
			} catch (IllegalArgumentException e) {
				//Don't care, skip
			} catch (IllegalAccessException e) {
				//Don't care, skip
			} catch (NullPointerException e) {
				//Don't care, skip
			}
		}
		Class<?> superClass = clazz.getSuperclass();
	    if (superClass != null) {
	    	return getLong(search, superClass);
	    }
		return Long.MIN_VALUE;
	}
	
	private boolean nameMatches(String search, String fieldName) {
		if (search == null) return false;
		if (fieldName == null) return false;
		if (fieldName.equalsIgnoreCase(search)) return true;
		if (search.length() <= 1) return false;  //if there is only 1 character there is no reason to continue
		
		String newSearch = firstWordFromCamelCase(search);
		
		if (fieldName.equalsIgnoreCase(newSearch)) return true;
		return false;
	}

	//Grab the first camel cased word  i.e. searchForMe would return just "search"
	protected String firstWordFromCamelCase(String search) {
		String upper = search.toUpperCase();
		StringBuilder sb = new StringBuilder(search.substring(0, 1));
		for (int i = 1; i < search.length(); i++) {
			if (search.charAt(i) == upper.charAt(i)) { //We have Found a upper cased mismatch stop
				break;
			}
			sb.append(search.substring(i, i + 1));
		}
		String newSearch = sb.toString();
		return newSearch;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		//TODO
		return sb.toString();
	}

}