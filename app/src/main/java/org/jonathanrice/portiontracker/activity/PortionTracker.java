package org.jonathanrice.portiontracker.activity;
/*
* Copyright (C) 2011 Jonathan Rice
* Licensed under the GNU Lesser General Public License (LGPL)
* http://www.gnu.org/licenses/lgpl.html
*/
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jonathanrice.portiontracker.R;
import org.jonathanrice.portiontracker.dao.DatabaseHelper;
import org.jonathanrice.portiontracker.orm.Portion;
import org.jonathanrice.portiontracker.orm.PortionGoal;
import org.jonathanrice.portiontracker.ui.NumberPicker;

import java.lang.reflect.Field;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.app.DatePickerDialog;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class PortionTracker extends OrmLiteBaseActivity<DatabaseHelper> {
	
	private final String LOG_TAG = getClass().getSimpleName();
	
	protected Portion currentPortion;
	protected PortionGoal currentPortionGoal;
	
	protected Date activeDay = null;
	
	protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    static final int DATE_DIALOG_ID = 0;
    static final int GOAL_DIALOG_ID = 1;
    static final int WEIGHT_DIALOG_ID = 2;
    
    protected Dialog numberPickerDialog;
    protected NumberPicker numberPicker;

    // the call back received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                	saveDailyPortion();
                    setDay(year, monthOfYear, dayOfMonth);
                	initFromDB();
                    resetPortionText();
                }
            };
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        hideScrollBar();
        initFromDB();
        initCurrentDateButton();
        initWeightButton();
        initNumberPicker();        
        resetPortionText();
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.Setting:        	
            return true;
        case R.id.Graph:
        	Intent intent = new Intent(this, GraphPortion.class);
        	this.startActivity(intent);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	protected void initFromDB() {
    	if (activeDay == null) activeDay = new Date();
    	
    	try {
			currentPortion = getHelper().getPortionDao().selectOrCreatePortionForDay(activeDay);
			currentPortionGoal = getHelper().getPortionGoalDao().selectOrCreateGoal();
			
			if (currentPortion.getWeight() <= 0) {
				currentPortion.setWeight(currentPortionGoal.getWeight());
			}
		} catch (SQLException e) {
			Log.e(LOG_TAG, "initFromDB", e);
		} 
	}
    
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
        	return onCreateDateDiaglog();
        case GOAL_DIALOG_ID:
        	return onCreateGoalDialog();        
        case WEIGHT_DIALOG_ID:
        	return onCreateWeightDialog();
        }	
        return null;
    }

	protected Dialog onCreateGoalDialog() {
		Button ok = (Button) numberPickerDialog.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
		    @Override
		        public void onClick(View v) {
		    		if (numberPicker == null || numberPicker.getCallingView() == null) {
		    			numberPickerDialog.dismiss();
		    			return; // TODO this fixes a NPE, but we need to be able persist the numberPicker somehow :( otherwise the update won't work if you switch view or some such.  We could just close the dialog...
		    		}
		    		String viewName = getViewNameFromId(numberPicker.getCallingView().getId());
		    		currentPortionGoal.setLong(viewName, numberPicker.getCurrent());
		    		numberPickerDialog.dismiss();
		    		resetPortionText();
		        }
		    });        	
		setCurrentGoalOnNumberPicker(numberPicker.getCallingView());
		return numberPickerDialog;
	}

	protected Dialog onCreateDateDiaglog() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(activeDay);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return new DatePickerDialog(this,
		            mDateSetListener,
		            year, month, day);
	}
	
	protected Dialog onCreateWeightDialog() {
		Button ok = (Button) numberPickerDialog.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
		    @Override
		        public void onClick(View v) {
	    			if (numberPicker == null || numberPicker.getCallingView() == null) {
	    				numberPickerDialog.dismiss();
	    				return; // TODO this fixes a NPE, but we need to be able persist the numberPicker somehow :( otherwise the update won't work if you switch view or some such.  We could just close the dialog...
	    			}
		    		String viewName = getViewNameFromId(numberPicker.getCallingView().getId());
		    		currentPortion.setLong(viewName, numberPicker.getCurrent());
		    		numberPickerDialog.dismiss();
		    		resetPortionText();
		        }
		    });        	
		setCurrentGoalOnNumberPicker(numberPicker.getCallingView());
		return numberPickerDialog;
	}
	
	protected void setCurrentGoalOnNumberPicker(View view) {
		if (view == null) {
			return;
		}
		String viewName = getViewNameFromId(view.getId());
		long currentValue = currentPortionGoal.getLong(viewName);
		numberPicker.setCurrent((int)currentValue);
	}
	protected void initNumberPicker() {
		if (numberPickerDialog == null) {
			numberPickerDialog = new Dialog(this);
			numberPickerDialog.setTitle("Goal");
			numberPickerDialog.setContentView(R.layout.number_picker_main); 
		}       	
		if (numberPicker == null ) {
			numberPicker = (NumberPicker) numberPickerDialog.findViewById(R.id.NumberPicker);
		}
	}

	@Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
    }
    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
        saveDailyPortion();
    }
    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
    }
    protected void saveDailyPortion() {
    	try {
			getHelper().getPortionDao().update(currentPortion);
		} catch (SQLException e) {
			Log.e(LOG_TAG, "unable to save current portion", e);
		}
		try {
			if (currentPortion.getWeight() > 0) {
				currentPortionGoal.setWeight(currentPortion.getWeight());
			}
			getHelper().getPortionGoalDao().update(currentPortionGoal);
		} catch (SQLException e) {
			Log.e(LOG_TAG, "unable to save current portion goal", e);
		}
    }
	protected void hideScrollBar() {
		ScrollView sView = (ScrollView)findViewById(R.id.scroller);
//      Hide the Scroll bar
        sView.setVerticalScrollBarEnabled(false);
        sView.setHorizontalScrollBarEnabled(false);
	}

	protected void initCurrentDateButton() {
		if (activeDay == null) {
			activeDay= new Date();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEEE, MMMMMM d, yyyy");
        Button dateButton = (Button) this.findViewById(R.id.DateButton);
        dateButton.setText(dateFormat.format(activeDay));
	}
	protected void initWeightButton() {
		// TODO Auto-generated method stub
		Button weightButton = (Button) this.findViewById(R.id.WeightButton);
		weightButton.setText("Weight: " + currentPortion.getWeight());
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
	public void buttonPressVibrate() {
    	Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);    	 
    	v.vibrate(45);
	}
	
	protected void updateButtonWarning(Button button, long current, long goal) {
		if (current == goal) {
			button.setBackgroundResource(R.color.yellowbutton);
		} else if (current > goal) {
			button.setBackgroundResource(R.color.redbutton);
		} else {
			button.setBackgroundResource(R.color.greenbutton);
		}
	}
	
	protected void updateGoalButtons() {
    	Button button = (Button)this.findViewById(R.id.WaterGoal);
    	button.setText(Long.toString(currentPortionGoal.getWater()));
    	updateButtonWarning(button, currentPortion.getWater(), currentPortionGoal.getWater());
    	
    	button = (Button)this.findViewById(R.id.ProteinGoal);
    	button.setText(Long.toString(currentPortionGoal.getProtein()));
    	updateButtonWarning(button, currentPortion.getProtein(), currentPortionGoal.getProtein());
    	
    	button = (Button)this.findViewById(R.id.DairyGoal);
    	button.setText(Long.toString(currentPortionGoal.getDairy()));
    	updateButtonWarning(button, currentPortion.getDairy(), currentPortionGoal.getDairy());
    	
    	button = (Button)this.findViewById(R.id.FruitGoal);
    	button.setText(Long.toString(currentPortionGoal.getFruit()));
    	updateButtonWarning(button, currentPortion.getFruit(), currentPortionGoal.getFruit());
    	
    	button = (Button)this.findViewById(R.id.VegetableGoal);
    	button.setText(Long.toString(currentPortionGoal.getVegetable()));
    	updateButtonWarning(button, currentPortion.getVegetable(), currentPortionGoal.getVegetable());
    	
    	button = (Button)this.findViewById(R.id.FatGoal);
    	button.setText(Long.toString(currentPortionGoal.getFat()));
    	updateButtonWarning(button, currentPortion.getFat(), currentPortionGoal.getFat());
    	
    	button = (Button)this.findViewById(R.id.CarbGoal);
    	button.setText(Long.toString(currentPortionGoal.getCarb()));
    	updateButtonWarning(button, currentPortion.getCarb(), currentPortionGoal.getCarb());
    	
    	button = (Button)this.findViewById(R.id.SnackGoal);
    	button.setText(Long.toString(currentPortionGoal.getSnack()));
    	updateButtonWarning(button, currentPortion.getSnack(), currentPortionGoal.getSnack());
    	
    	button = (Button)this.findViewById(R.id.CondimentGoal);
    	button.setText(Long.toString(currentPortionGoal.getCondiment()));
    	updateButtonWarning(button, currentPortion.getCondiment(), currentPortionGoal.getCondiment());
	}
	
    public void portionMore(View view) {
    	String viewName = getViewNameFromId(view.getId());
    	long currentValue = currentPortion.getLong(viewName);
    	if (currentValue == Long.MIN_VALUE) {
    		Log.e(LOG_TAG, "unable to save update current goal");
    		return;
    	}
		currentPortion.setLong(viewName, currentValue + 1);
    	resetPortionText();
    	buttonPressVibrate();    	
    }
    
    public void portionLess(View view) {
    	String viewName = getViewNameFromId(view.getId());
    	long currentValue = currentPortion.getLong(viewName);
    	if (currentValue == Long.MIN_VALUE) {
    		Log.e(LOG_TAG, "unable to save update current goal");
    		return;
    	}
		currentPortion.setLong(viewName, currentValue - 1);
    	resetPortionText();
    	buttonPressVibrate();    	
    }

    public void dateButtonClick(View view) {
    	showDialog(DATE_DIALOG_ID);
    }
    
    public void weightButtonClick(View view) {
    	numberPicker.setCallingView(view);
		setCurrentGoalOnNumberPicker(view);
		showDialog(WEIGHT_DIALOG_ID);
    }
    
	public void goalClick(View view) {
		numberPicker.setCallingView(view);
		setCurrentGoalOnNumberPicker(view);
		showDialog(GOAL_DIALOG_ID);
    }
	
    protected void resetPortionText() {
    	updateGoalButtons();
    	initCurrentDateButton();
    	initWeightButton();
    	updatePortionText();
	}

	protected void updatePortionText() {
		TextView text = (TextView)this.findViewById(R.id.WaterPortions);
    	text.setText(Long.toString(currentPortion.getWater()));
    	text = (TextView)this.findViewById(R.id.ProteinPortions);
    	text.setText(Long.toString(currentPortion.getProtein()));
    	text = (TextView)this.findViewById(R.id.DairyPortions);
    	text.setText(Long.toString(currentPortion.getDairy()));
    	text = (TextView)this.findViewById(R.id.FruitPortions);
    	text.setText(Long.toString(currentPortion.getFruit()));
    	text = (TextView)this.findViewById(R.id.VegetablePortions);
    	text.setText(Long.toString(currentPortion.getVegetable()));
    	text = (TextView)this.findViewById(R.id.FatPortions);
    	text.setText(Long.toString(currentPortion.getFat()));
    	text = (TextView)this.findViewById(R.id.CarbPortions);
    	text.setText(Long.toString(currentPortion.getCarb()));
    	text = (TextView)this.findViewById(R.id.SnackPortions);
    	text.setText(Long.toString(currentPortion.getSnack()));
    	text = (TextView)this.findViewById(R.id.CondimentPortions);
    	text.setText(Long.toString(currentPortion.getCondiment()));
	}
	protected void setDay(int year, int monthOfYear, int dayOfMonth) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, monthOfYear, dayOfMonth);
		activeDay = cal.getTime();
	}
	
	public static String getViewNameFromId(int id) {
		Field[] fields = R.id.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				int fieldInt = field.getInt(null);
				if (fieldInt == id) {
					return field.getName();
				}
			} catch (IllegalArgumentException e) {
				//Don't care, skip
			} catch (IllegalAccessException e) {
				//Don't care, skip
			} catch (NullPointerException e) {
				//Don't care, skip
			}
		}
		return null;
	}
}