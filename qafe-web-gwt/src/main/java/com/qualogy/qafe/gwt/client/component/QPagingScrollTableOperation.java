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
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.factory.MainFactoryActions;
import com.qualogy.qafe.gwt.client.images.ScrollTableOperationImages;
import com.qualogy.qafe.gwt.client.service.RPCServiceAsync;
import com.qualogy.qafe.gwt.client.ui.images.DataGridImageBundle;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.functions.execute.FunctionsExecutor;
import com.qualogy.qafe.gwt.client.vo.ui.ImageGVO;

/**
 * A panel that wraps a {@link QPagingScrollTable} and includes operations to
 * perform operations on the Qpagingscrolltable
 */
public class QPagingScrollTableOperation extends Composite {

	public final static String CONTROLS_DELETE="delete";
	public final static String CONTROLS_ADD="add";
	public final static String CONTROLS_SAVE="save";
	public final static String CONTROLS_REFRESH="refresh";
	public final static String CONTROLS_CANCEL="cancel";

	public final static String STYLE_DELETE="Delete";
	public final static String STYLE_ADD="Add";
	public final static String STYLE_SAVE="Save";
	public final static String STYLE_REFRESH="Refresh";
	public final static String STYLE_CANCEL="Cancel";

	/**
	 * The default style name.
	 */
	public static final String DEFAULT_STYLENAME = "gwt-PagingOptions";
	public static final String STYLENAME_PREFIX = "pagingOptions";

	/**
	 * The label used to display errors.
	 */
	private HTML errorLabel;

	private Image addImage;
	private Image deleteImage;
	private Image saveImage;
	private Image refreshImage;
	private Image cancelImage;

	/**
	 * The table being affected.
	 */
	private QPagingScrollTable table;

	/** Mapping between supported export formats and labels to be displayed on screen. */
	private HashMap<String, String> labelsMap;

	/** Mapping between supported export formats labels and images. */
	private HashMap<String, Image> imageMap;

	/**
	 * Constructor.
	 *
	 * @param table
	 *            the table being ad
	 */
	public QPagingScrollTableOperation(QPagingScrollTable table) {
		this(table, GWT.<ScrollTableOperationImages> create(ScrollTableOperationImages.class));
	}

	/**
	 * Constructor.
	 *
	 * @param table
	 *            the table being affected
	 * @param images
	 *            the images to use
	 */
	// CHECKSTYLE.OFF: CyclomaticComplexity
	public QPagingScrollTableOperation(final QPagingScrollTable table, ScrollTableOperationImages images) {
		this.table = table;
		if (this.table instanceof QPagingScrollTable){
			((QPagingScrollTable)this.table).setScrollTableOperations(this);
		}

		// Create the main widget
		HorizontalPanel hPanel = new HorizontalPanel();
		initWidget(hPanel);
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		setStyleName(DEFAULT_STYLENAME);

		// Create the paging image buttons
		createPageButtons(images);

		// Create the error label
		errorLabel = new HTML();
		errorLabel.setStylePrimaryName("errorMessage");

		// Add the widgets to the panel
		hPanel.add(createSpacer());
		if (hasAddControl(table)){
			hPanel.add(addImage);
			hPanel.add(createSpacer());
		}
		if (hasDeleteControl(table)){
			hPanel.add(deleteImage);
			hPanel.add(createSpacer());
		}

		if (isEditableDatagrid(table) || hasDeleteControl(table) || hasAddControl(table)){
			if (saveDatagrid(table)) {
				hPanel.add(saveImage);
				hPanel.add(createSpacer());
			}
			if (refreshDatagrid(table)){
				hPanel.add(refreshImage);
				hPanel.add(createSpacer());
			}
			if (cancelDatagrid(table)){
				hPanel.add(cancelImage);
				hPanel.add(createSpacer());
			}
		}

		hPanel.add(errorLabel);

		if (table.getSource().getImportEnabled()!=null && table.getSource().getImportEnabled().booleanValue()){
			final DisclosurePanel importPanel = new DisclosurePanel("Upload data");
			hPanel.add(importPanel);
			final FormPanel formPanel = new FormPanel();

			formPanel.setAction(GWT.getModuleBaseURL() + "/rpc.datagridupload");
			formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
			formPanel.setMethod(FormPanel.METHOD_POST);

			FileUpload fileUploadComponent = new FileUpload();
			fileUploadComponent.setName("uploadElement");

			Button uploadButtonComponent = new Button("Upload");
			uploadButtonComponent.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					formPanel.submit();
				}
			});

			CheckBox isFirstLineHeader = new CheckBox("Is first row header ?");
			isFirstLineHeader.setName("isFirstLineHeader");
			isFirstLineHeader.setTitle("Check wheter or not the first line of the uploaded file is a header/column definition");
			HorizontalPanel hp = new HorizontalPanel();

			Label label = new Label("Delimeter");
			final TextBox delimiter = new TextBox();
			delimiter.setValue(",");
			delimiter.setTitle("Insert the delimeter (can be any value, as long it's length 1)");
			delimiter.setName("delimiter");
			delimiter.setWidth("15px");
			hp.setSpacing(10);
			hp.add(label);
			hp.add(delimiter);

			Grid gridPanel = new Grid(2, 4);
			gridPanel.setWidget(0, 0, fileUploadComponent);
			gridPanel.setWidget(0, 1, uploadButtonComponent);
			gridPanel.setWidget(1, 0, isFirstLineHeader);
			gridPanel.setWidget(1, 1, hp);

			formPanel.add(gridPanel);

			formPanel.addSubmitHandler(new FormPanel.SubmitHandler() {

				public void onSubmit(SubmitEvent event) {
					// This event is fired just before the form is submitted. We can
					// take
					// this opportunity to perform validation.
					if (delimiter.getText().length() == 0 || delimiter.getText().length() > 1) {
						ClientApplicationContext.getInstance().log("Ooops...Delimeter invalid", "Make sure there is valid delimeter value.One character only (current value ='" + delimiter.getText() + "'", true);
						event.cancel();
					}
				}

			});

			formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

				public void onSubmitComplete(SubmitCompleteEvent event) {
					String uuId = event.getResults();
					if (uuId != null && uuId.indexOf("=") > 0) {
						uuId = uuId.substring(uuId.indexOf("=") + 1);
						processData(uuId);
						importPanel.setOpen(false);
					} else {
						ClientApplicationContext.getInstance().log("Upload failed", event.getResults(), true);
					}
				}
			});
			importPanel.add(formPanel);
		}

		if (table.getSource() != null && table.getSource().getExport() != null && table.getSource().getExport().booleanValue()) {

			createExportLabelsAndImages();

			final DisclosurePanel exportPanel = new DisclosurePanel("Export");
			String[] labels = getExportLabels(table.getSource().getExportFormats());
			Image[] exportImages = getExportImages(labels);

			FlexTable gridExportPanel = new FlexTable();
			hPanel.add(exportPanel);
			exportPanel.add(gridExportPanel);
			final Frame frame = new Frame();
			frame.setHeight("0");
			frame.setWidth("0");
			frame.setVisible(false);
			final String moduleRelativeURL = GWT.getModuleBaseURL() + "/rpc.export";
			gridExportPanel.setWidget(0,0,frame);

			final CheckBox generateColumnHeaderBox= new CheckBox("Generate Column Header");
			gridExportPanel.getFlexCellFormatter().setColSpan(1,1, 7);
			gridExportPanel.setWidget(2, 1,generateColumnHeaderBox);
			gridExportPanel.getFlexCellFormatter().setColSpan(2,1,6);

			for (int i = 0; i < labels.length; i++) {

				exportImages[i].setStylePrimaryName("datagridexportlabel");
				exportImages[i].setTitle(labels[i]);
				gridExportPanel.setWidget(0, i+1,exportImages[i]);

				exportImages[i].addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {
						if (event.getSource() instanceof Image) {
							Image image = (Image) (event.getSource());
							final String exportCode = image.getTitle();
							RPCServiceAsync service = MainFactoryActions.createService();
							AsyncCallback<?> callback = new AsyncCallback<Object>() {
								public void onSuccess(Object result) {
									String uuid = (String) result;
									// set frame
									frame.setUrl(moduleRelativeURL + "?uuid=" + uuid);
									ClientApplicationContext.getInstance().setBusy(false);
								}

								public void onFailure(Throwable caught) {

									ClientApplicationContext.getInstance().log("Export failed", "Export failed for " + exportCode + " ", true, true, caught);
									ClientApplicationContext.getInstance().setBusy(false);
									FunctionsExecutor.setProcessedBuiltIn(true);
								}
							};
							List<DataContainerGVO> dList = new ArrayList<DataContainerGVO>();
							// following loop is to maintain the order of rows while exporting.
							for(int i=0;i<(table.getAbsoluteLastRowIndex()+1);i++){
								dList.add(table.getRowValue(i));
							}
							service.prepareForExport(dList, exportCode, null, generateColumnHeaderBox.getValue().booleanValue(),  callback);
						}
					}
				});
			}
		}
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	/**
	 * Create two maps to map the supported export formats with their corresponding labels and images
	 */
	private void createExportLabelsAndImages() {
		labelsMap = new HashMap<String, String>();
		labelsMap.put("csv", "CSV");
		labelsMap.put("excel", "Excel");
		labelsMap.put("pdf", "PDF");
		labelsMap.put("xml", "XML");

		DataGridImageBundle dib =ClientApplicationContext.getInstance().getDatagridImageBundle();
		imageMap = new HashMap<String, Image>();
		imageMap.put("CSV", dib.iconExportCSV().createImage());
		imageMap.put("Excel", dib.iconExportExcel().createImage());
		imageMap.put("XML", dib.iconExportXML().createImage());
		imageMap.put("PDF", dib.iconExportPDF().createImage());
	}

	/**
	 * Convert the export formats into labels to display.
	 * @param exportFormats A comma separated list of possible export formats
	 * @return an array of labels
	 */
	private String[] getExportLabels(String exportFormats) {
		String[] exportTypes = exportFormats.split(",");
		String[] labels = new String[exportTypes.length];
		for (int i = 0; i < labels.length; i ++) {
			labels[i] = labelsMap.get(exportTypes[i]);
		}
		return labels;
	}

	/**
	 * Create an array of images, one for every supported export type (i.e. labels)
	 * @param labels
	 * @return array of images
	 */
	private Image[] getExportImages(String[] labels) {
		Image[] images = new Image[labels.length];
		for (int i = 0; i < labels.length; i ++) {
			images[i] = imageMap.get(labels[i]);
		}
		return images;
	}

	private boolean hasDeleteControl(final QPagingScrollTable table) {
		return table.getSource().getDelete()!=null && table.getSource().getDelete().booleanValue();
	}

	private boolean hasAddControl(final QPagingScrollTable table) {
		return table.getSource().getAdd()!=null && table.getSource().getAdd().booleanValue();
	}

	private boolean isEditableDatagrid(final QPagingScrollTable table) {
		return table.getSource().getEditable()!=null && table.getSource().getEditable().booleanValue();
	}

	private boolean saveDatagrid(final QPagingScrollTable table) {
		return table.getSource().getSave();
	}

	private boolean cancelDatagrid(final QPagingScrollTable table) {
		return table.getSource().getCancel();
	}

	private boolean refreshDatagrid(final QPagingScrollTable table) {
		return table.getSource().getRefresh();
	}

	protected void processData(String uuid) {
		ClientApplicationContext.getInstance().setBusy(true);

		RPCServiceAsync service = MainFactoryActions.createService();
		AsyncCallback<?> callback = new AsyncCallback<Object>() {
			public void onSuccess(Object result) {
				List<DataContainerGVO> map = (List<DataContainerGVO>) result;
				table.insertData(map);
				// WindowFactory.createWindow(ui, windowId);
				ClientApplicationContext.getInstance().setBusy(false);
				FunctionsExecutor.setProcessedBuiltIn(true);
			}

			public void onFailure(Throwable caught) {

				ClientApplicationContext.getInstance().log("Getting window from server failed", "Error getting data after upload", true, true, caught);
				ClientApplicationContext.getInstance().setBusy(false);
				FunctionsExecutor.setProcessedBuiltIn(true);
			}
		};
		service.getDataForDatagridFromUpload(uuid, callback);

	}

	/**
	 * @return the {@link QPagingScrollTable}.
	 */
	public QPagingScrollTable getPagingScrollTable() {
		return table;
	}

	/**
	 * Create a widget that can be used to add space.
	 *
	 * @return a spacer widget
	 */
	protected Widget createSpacer() {
		return new HTML("&nbsp;&nbsp;");
	}

	/**
	 * Create a paging image buttons.
	 *
	 * @param images
	 *            the images to use
	 */
	private void createPageButtons(ScrollTableOperationImages images) {

		addImage = images.scrolltableOperationAdd().createImage();
		addImage.addStyleName(STYLENAME_PREFIX + STYLE_ADD);
		deleteImage = images.scrolltableOperationDelete().createImage();
		deleteImage.addStyleName(STYLENAME_PREFIX + STYLE_DELETE);

		// Create the images
		if (saveDatagrid(table)) {
			saveImage = images.scrolltableOperationSave().createImage();
			saveImage.addStyleName(STYLENAME_PREFIX + STYLE_SAVE);
		}
		if (refreshDatagrid(table)){
			refreshImage = images.scrolltableOperationRefresh().createImage();
			refreshImage.addStyleName(STYLENAME_PREFIX + STYLE_REFRESH);
		}
		if (cancelDatagrid(table)){
			cancelImage = images.scrolltableOperationCancel().createImage();
			cancelImage.addStyleName(STYLENAME_PREFIX + STYLE_CANCEL);
		}


		// Create the listener
		ClickHandler handler = new ClickHandler() {
			public void onClick(ClickEvent event) {
				Object source = event.getSource();
				if (source == addImage) {
					table.onAdd();
				} else if (source == deleteImage) {
					table.onDelete();
				} else if (source == saveImage) {
					table.onSave();
				} else if (source == refreshImage) {
					table.onRefresh();
				} else if (source == cancelImage) {
					table.onCancel();
				}
			}
		};

		// Add the listener to each image
		addImage.addClickHandler(handler);
		deleteImage.addClickHandler(handler);
		if (saveImage != null){
			saveImage.addClickHandler(handler);
		}
		if (refreshImage != null){
			refreshImage.addClickHandler(handler);
		}
		if (cancelImage != null){
			cancelImage.addClickHandler(handler);
		}

	}

	public void initForCallback(String gridId, String uuid, String parent, String context) {
		ImageGVO imageDelete = createImageGVO(gridId + "." + CONTROLS_DELETE);
		ImageGVO imageAdd = createImageGVO(gridId + "." + CONTROLS_ADD);
		ImageGVO imageSave = createImageGVO(gridId + "." + CONTROLS_SAVE);
		ImageGVO imageRefresh = createImageGVO(gridId + "." + CONTROLS_REFRESH);
		ImageGVO imageCancel = createImageGVO(gridId + "." + CONTROLS_CANCEL);

		RendererHelper.fillIn(imageDelete, getDeleteImage(), uuid, parent, context);
		RendererHelper.fillIn(imageAdd, getAddImage(), uuid, parent, context);
		RendererHelper.fillIn(imageSave, getSaveImage(), uuid, parent, context);
		RendererHelper.fillIn(imageRefresh, getRefreshImage(), uuid, parent, context);
		RendererHelper.fillIn(imageCancel, getCancelImage(), uuid, parent, context);
	}

	private ImageGVO createImageGVO(String id) {
		ImageGVO imageGVO = new ImageGVO();
		imageGVO.setId(id);
		return imageGVO;
	}

	public Image getDeleteImage() {
		return deleteImage;
	}

	public void setDeleteImage(Image deleteImage) {
		this.deleteImage = deleteImage;
	}

	public Image getAddImage() {
		return addImage;
	}

	public void setAddImage(Image addImage) {
		this.addImage = addImage;
	}

	public Image getSaveImage() {
		return saveImage;
	}

	public void setSaveImage(Image saveImage) {
		this.saveImage = saveImage;
	}

	public Image getRefreshImage() {
		return refreshImage;
	}

	public void setRefreshImage(Image refreshImage) {
		this.refreshImage = refreshImage;
	}

	public Image getCancelImage() {
		return cancelImage;
	}

	public void setCancelImage(Image cancelImage) {
		this.cancelImage = cancelImage;
	}
}
