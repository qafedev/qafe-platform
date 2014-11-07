/**
 * Copyright 2008-2014 Qualogy Solutions B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qualogy.qafe.gwt.client.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.UIObject;
import com.objetdirect.tatami.client.charting.Axis;
import com.objetdirect.tatami.client.charting.BarPlot;
import com.objetdirect.tatami.client.charting.Chart2D;
import com.objetdirect.tatami.client.charting.PiePlot;
import com.objetdirect.tatami.client.charting.Plot;
import com.objetdirect.tatami.client.charting.Plot2D;
import com.objetdirect.tatami.client.charting.Serie;
import com.objetdirect.tatami.client.charting.Themes;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.ui.CategoryAxisGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ChartItemGVO;
import com.qualogy.qafe.gwt.client.vo.ui.LinearAxisGVO;


public class QChart2D extends Chart2D{

	private LinearAxisGVO linearAxis;
	private CategoryAxisGVO categoryAxis;
	private List<ChartItemGVO> chartItems;
	private String legend;

	public QChart2D() {
	}

	public QChart2D(String height, String width) {
		this.setHeight(height);
		this.setWidth(width);
	}

	public LinearAxisGVO getLinearAxis() {
		return linearAxis;
	}

	public void setLinearAxis(LinearAxisGVO linearAxis) {
		this.linearAxis = linearAxis;
	}

	public CategoryAxisGVO getCategoryAxis() {
		return categoryAxis;
	}

	public void setCategoryAxis(CategoryAxisGVO categoryAxis) {
		this.categoryAxis = categoryAxis;
	}

	public List<ChartItemGVO> getChartItems() {
		return chartItems;
	}

	public void setChartItems(List<ChartItemGVO> chartItems) {
		this.chartItems = chartItems;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}

	// CHECKSTYLE.OFF: CyclomaticComplexity
	public void setChartData(UIObject uiObject, List data){
		QChart2D chart = (QChart2D)uiObject;
		List<DataMap> dataToPlot = data;
		String[] colors = {"green", "red", "violet", "indigo", "yellow", "orange", "blue"};
		int colorIndex = 0;
		ArrayList<String> chartItemsToDisplay = new ArrayList<String>();
		for(ChartItemGVO chartItem : chart.getChartItems()){
			chartItemsToDisplay.add(chartItem.getFieldName());
		}

		ArrayList<String> categoryAxisValues = new ArrayList<String>();
		ArrayList<String> linearAxisValues = new ArrayList<String>();

		for(Plot plot: chart.getPlots()) {
			Serie serie = new Serie();
			String serieName = null;
			String categoryAxisName = null;
			String linearAxisName = null;
			for(int chartItemCount = 0; chartItemCount < chartItemsToDisplay.size(); chartItemCount++){
				for(int dataCount = 0; dataCount < data.size(); dataCount++){
					DataMap li = ((DataContainerGVO)data.get(dataCount)).getDataMap();
					serie.addData(Double.parseDouble(li.get(chartItemsToDisplay.get(chartItemCount)).toString()));
					serieName = chartItemsToDisplay.get(chartItemCount).toString();
					if(chartItemCount == 0){
						if(chart.getCategoryAxis() != null){
							if(chart.getCategoryAxis().getFieldName() != null){
								categoryAxisValues.add(li.get(chart.getCategoryAxis().getFieldName()).toString());
							}
							if(chart.getCategoryAxis().getDisplayname() != null){
								categoryAxisName = chart.getCategoryAxis().getDisplayname();
							}
						}
						if(chart.getLinearAxis() != null){
							if(chart.getLinearAxis().getFieldName() != null) {
								linearAxisValues.add(li.get(chart.getLinearAxis().getFieldName()).toString());
							}
							if(chart.getLinearAxis().getDisplayname() != null) {
								linearAxisName = chart.getLinearAxis().getDisplayname();
							}
						}
					}
				}
				serie.setStrokeWidth(5);
				if(chartItemCount < 7){
					colorIndex = chartItemCount;
				} else if(chartItemCount == 7){
					colorIndex = 0;
				} else if(chartItemCount > 7){
					colorIndex++;
				}
				if(plot.getType().equals(Plot2D.PLOT_TYPE_LINES_STACKED) || plot.getType().equals(Plot2D.PLOT_TYPE_SCATTER)) {
					serie.setMarkerType(Serie.MARKER_TYPE_CIRCLE);
				}
				serie.setFillColor(colors[colorIndex]);
				serie.setStrokeColor(colors[colorIndex]);
				serie.setName(serieName);
				plot.addSerie(serie);
				serie = new Serie();
			}
			QAxis x = new QAxis(Axis.BOTTOM | Axis.HORIZONTAL);
			QAxis y = new QAxis(Axis.LEFT | Axis.VERTICAL);
			ArrayList<String> xAxisValues = new ArrayList<String>();
			ArrayList<String> yAxisValues = new ArrayList<String>();
			String xAxisName = null;
			String yAxisName = null;
			if(plot.getType().equals(BarPlot.PLOT_TYPE_BARS_CLUSTERED)) { // BAR CHART
				xAxisName = linearAxisName;
				xAxisValues = linearAxisValues;
				yAxisName = categoryAxisName;
				yAxisValues = categoryAxisValues;
			} else { // COLUMN CHART or LINE CHART or PLOT CHART or PIE CHART
				xAxisName = categoryAxisName;
				xAxisValues = categoryAxisValues;
				yAxisName = linearAxisName;
				yAxisValues = linearAxisValues;
			}

			if(xAxisValues.size() > 0){
				for (int k = 0; k < xAxisValues.size(); k++) {
			        x.addLabel(xAxisValues.get(k), k+1);
				}
			} else {
				x = QAxis.simpleXAxis();
			}
			if(yAxisValues.size() > 0){
				for (int j = 0; j < yAxisValues.size(); j++) {
			        y.addLabel(yAxisValues.get(j), j+1);
				}
			} else {
				y = QAxis.simpleYAxis();
			}

			// Setting min-value, max-value and ticker size for linear axis
			if(chart.getLinearAxis() !=null && chart.getLinearAxis().getMinValue() !=null){
				if(plot.getType().equals(BarPlot.PLOT_TYPE_BARS_CLUSTERED)){
					x.setMin(chart.getLinearAxis().getMinValue());
				} else {
					y.setMin(chart.getLinearAxis().getMinValue());
				}
			} else {
				y.setIncludeZero(true);
			}
			if(chart.getLinearAxis() !=null && chart.getLinearAxis().getMaxValue() !=null){
				if(plot.getType().equals(BarPlot.PLOT_TYPE_BARS_CLUSTERED)){
					x.setMax(chart.getLinearAxis().getMaxValue());
				} else {
					y.setMax(chart.getLinearAxis().getMaxValue());
				}
			}
			if(chart.getLinearAxis() !=null && chart.getLinearAxis().getTickSize() !=null){
				if(plot.getType().equals(BarPlot.PLOT_TYPE_BARS_CLUSTERED)){
					x.setMajorTickStep(chart.getLinearAxis().getTickSize());
				} else {
					y.setMajorTickStep(chart.getLinearAxis().getTickSize());
				}
			}

			x.setIncludeZero(true);
			if(xAxisName != null){
				x.setAxisName(xAxisName); // NOT WORKING
			}
			if(yAxisName != null) {
				y.setAxisName(yAxisName); // NOT WORKING
			}
			plot.setXAxis(x);
			plot.setYAxis(y);
			if(plot instanceof PiePlot){
				chart.setTheme(Themes.Shrooms);
			}
		}
		chart.refreshChart();
	}
	// CHECKSTYLE.ON: CyclomaticComplexity
}
