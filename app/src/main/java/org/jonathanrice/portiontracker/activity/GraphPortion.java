package org.jonathanrice.portiontracker.activity;
/*
 * Copyright (C) 2011 Jonathan Rice
 * Licensed under the GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/licenses/lgpl.html
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.jonathanrice.portiontracker.dao.DatabaseHelper;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class GraphPortion extends OrmLiteBaseActivity<DatabaseHelper> {

	private GraphicalView mView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {       
		super.onCreate(savedInstanceState);
		mView = createGraphContent();
		setContentView(mView);
	}

	public GraphicalView createGraphContent() {
		String[] titles = new String[] { "New tickets", "Fixed tickets" };
		List<Date[]> dates = new ArrayList<Date[]>();
		List<double[]> values = new ArrayList<double[]>();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			dates.add(new Date[12]);
			dates.get(i)[0] = new Date(108, 9, 1);
			dates.get(i)[1] = new Date(108, 9, 8);
			dates.get(i)[2] = new Date(108, 9, 15);
			dates.get(i)[3] = new Date(108, 9, 22);
			dates.get(i)[4] = new Date(108, 9, 29);
			dates.get(i)[5] = new Date(108, 10, 5);
			dates.get(i)[6] = new Date(108, 10, 12);
			dates.get(i)[7] = new Date(108, 10, 19);
			dates.get(i)[8] = new Date(108, 10, 26);
			dates.get(i)[9] = new Date(108, 11, 3);
			dates.get(i)[10] = new Date(108, 11, 10);
			dates.get(i)[11] = new Date(108, 11, 17);
		}
		values.add(new double[] { 142, 123, 142, 152, 149, 122, 110, 120, 125, 155, 146, 150 });
		values.add(new double[] { 102, 90, 112, 105, 125, 112, 125, 112, 105, 115, 116, 135 });
		length = values.get(0).length;
		int[] colors = new int[] { Color.BLUE, Color.GREEN };
		PointStyle[] styles = new PointStyle[] { PointStyle.POINT, PointStyle.POINT };
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		setChartSettings(renderer, "Project work status", "Date", "Tickets", dates.get(0)[0].getTime(),
				dates.get(0)[11].getTime(), 50, 190, Color.GRAY, Color.LTGRAY);
		renderer.setXLabels(5);
		renderer.setYLabels(10);
		length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
			seriesRenderer.setDisplayChartValues(true);
		}
		XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries("Foo");
		dataSet.addSeries(series);
		return createTimeChart(this, buildDateDataset(titles, dates, values), renderer, "MM/DD");
		/*        return ChartFactory.getTimeChartIntent(context, buildDateDataset(titles, dates, values),
            renderer, "MM/dd/yyyy");*/
	}

	public GraphicalView createTimeChart(Context context,
			XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer, String format) {
		return ChartFactory.getTimeChartView(context, dataset, renderer, format);
	}

	/**
	 * Builds an XY multiple series renderer.
	 * 
	 * @param colors the series rendering colors
	 * @param styles the series point styles
	 * @return the XY multiple series renderers
	 */
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer, colors, styles);
		return renderer;
	}

	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(5f);
		renderer.setMargins(new int[] { 20, 30, 15, 20 });
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
	}

	/**
	 * Sets a few of the series renderer settings.
	 * 
	 * @param renderer the renderer to set the properties to
	 * @param title the chart title
	 * @param xTitle the title for the X axis
	 * @param yTitle the title for the Y axis
	 * @param xMin the minimum value on the X axis
	 * @param xMax the maximum value on the X axis
	 * @param yMin the minimum value on the Y axis
	 * @param yMax the maximum value on the Y axis
	 * @param axesColor the axes color
	 * @param labelsColor the labels color
	 */
	protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
			String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}
	/**
	 * Builds an XY multiple time dataset using the provided values.
	 * 
	 * @param titles the series titles
	 * @param xValues the values for the X axis
	 * @param yValues the values for the Y axis
	 * @return the XY multiple time dataset
	 */
	protected XYMultipleSeriesDataset buildDateDataset(String[] titles, List<Date[]> xValues, List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			TimeSeries series = new TimeSeries(titles[i]);
			Date[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}
}
