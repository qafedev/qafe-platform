/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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
package com.qualogy.qafe.gwt.client.factory;

import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ScrollLayoutPanel;
import org.gwt.mosaic.ui.client.WindowPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.qualogy.qafe.gwt.client.component.ShowPanelComponent;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.vo.ui.QAFEKeywordsGVO;
import com.qualogy.qafe.gwt.client.vo.ui.TextFieldGVO;

public class MainFactory {

	private MainFactory() {
	}

	public static void createTryMeWindow(String subwindow) {

		final WindowPanel w = new WindowPanel("Try me!");
	
		w.setResizable(true);
		
		w.setAnimationEnabled(true);
		w.setSize("800px", "500px");

		VerticalPanel verticalPanel = new VerticalPanel();
		final ScrollLayoutPanel vp = new ScrollLayoutPanel();
		vp.setAlwaysShowScrollBars(false);
		vp.setWidth("800px");	
		vp.setHeight("500px");
		w.setWidget(verticalPanel);
		// vp.setSpacing(5);
		// vp.setWidth("100%");
		final TabPanel tabPanel = new TabPanel();
		tabPanel.setAnimationEnabled(true);
		tabPanel.setWidth("580px");
		tabPanel.setHeight("500px");
	
		DockPanel dockPanel = new DockPanel();
		 dockPanel.setWidth("580px");
		 dockPanel.setHeight("500px");
		tabPanel.add(dockPanel, "Insert code!");
		final TextArea textArea = new TextArea();
		textArea.setVisibleLines(30);
		textArea.setHeight("auto");
		textArea.setWidth("580px");
		DOM.setElementAttribute(textArea.getElement(), "font-size", "10pt");

		dockPanel.add(textArea, DockPanel.CENTER);
		final MenuBar menu = new MenuBar();
		MenuBar renderMenu = new MenuBar(true);
		w.addResizeHandler(new ResizeHandler() {

			public void onResize(ResizeEvent event) {
				int height = event.getHeight();
				int width = event.getWidth();
				
				if (w.getWidget() != null ) {
						w.setHeight((height)+"px");
						w.setWidth((width)+"px");
						vp.setHeight((height-20)+"px");
						vp.setWidth((width-20)+"px");
						tabPanel.setHeight((height-20)+"px");
						tabPanel.setWidth((width-20)+"px");
						menu.setWidth((width)+"px");
						textArea.setWidth((width-20)+"px");
				}

			}
		});

		MenuItem gwtMenuItem = new MenuItem("GWT output", new Command() {

			public void execute() {
				String xml = textArea.getText();
				if (xml == null || xml.length() == 0) {
					MessageBox.error("Try me:Error", "There is no input");
				} else {
					MainFactoryActions.processUIXml(xml);
				}

			}
		});
		MenuItem flexMenuItem = new MenuItem("Flex output", new Command() {

			public void execute() {
				String xml = textArea.getText();
				if (xml == null || xml.length() == 0) {
					MessageBox.error("Try me:Error", "There is no input");
				} else {
					MainFactoryActions.processUIXmlFlex(xml);
				}

			}
		});

		renderMenu.addItem(gwtMenuItem);
		renderMenu.addItem(flexMenuItem);

		MenuBar codeMenu = new MenuBar(true);

		MenuItem clearXmlInput = new MenuItem("Clear", new Command() {

			public void execute() {
				textArea.setText("");

			}
		});

		MenuItem createHeaderButton = new MenuItem("Create Header", new Command() {

			public void execute() {
				final String headerText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" + "<application-mapping xmlns=\"http://qafe.com/schema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://qafe.com/schema http://www.qafe.com/schema/2.2/application-mapping.xsd\"> \n" + "  <!-- PLEASE ENTER YOUR CODE HERE --> \n" + "</application-mapping> \n";

				textArea.setText(headerText);

			}
		});

		MenuItem createSampleAppButton = new MenuItem("Create Sample Application", new Command() {

			public void execute() {
				final String sampleText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" + "<application-mapping xmlns=\"http://qafe.com/schema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://qafe.com/schema http://www.qafe.com/schema/2.2/application-mapping.xsd\"> \n" + "<presentation-tier>\n" + "  <view>\n" + "    <window id=\"window1\" displayname=\"Hello World\" width=\"200\" height=\"200\">\n" + "      <rootpanel id=\"myRootPanel\">	\n" + "        <verticallayout>\n\n" + "          <!-- PLEASE ENTER HERE YOUR CODE -->\n\n" + "          <label id=\"mylabel\" displayname=\"Hello World\" />\n\n" + "        </verticallayout>\n" + "      </rootpanel>\n" + "    </window>\n" + " </view>\n" + "</presentation-tier>\n" +

				"</application-mapping> \n";

				textArea.setText(sampleText);

			}
		});

		codeMenu.addItem(clearXmlInput);
		codeMenu.addItem(createHeaderButton);
		codeMenu.addItem(createSampleAppButton);

		menu.addItem("Render", renderMenu);
		menu.addItem("Code", codeMenu);

		if (menu != null) {
			verticalPanel.add(menu);
		}
		verticalPanel.add(vp);
	
		// w.setWidget(tabPanel);

		/*
		 * FMB Upload
		 */
		final FormPanel fmbForm = new FormPanel();
		fmbForm.setAction(GWT.getModuleBaseURL() + "/rpc.fmbupload");

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		fmbForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		fmbForm.setMethod(FormPanel.METHOD_POST);
		// Create a panel to hold all of the form widgets.
		VerticalPanel panelFmbUpload = new VerticalPanel();
		panelFmbUpload.setWidth("580px");
		panelFmbUpload.setHeight("500px");
		// panelFmbUpload.setHeight("100%");

		final FileUpload fmbFile = new FileUpload();
		final TextBox emailBox = new TextBox();
		final TextBox phoneBox = new TextBox();
		fmbFile.setName("fmbUploadElement");

		
		// Add an event handler to the form.
		fmbForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

			public void onSubmitComplete(SubmitCompleteEvent event) {
				
				if (event.getResults()!=null){
					if (event.getResults().startsWith("Conversion failed")){
						ClientApplicationContext.getInstance().log("Conversion failed", "The file doesn't seem to be a valid Oracle Forms file. If you still want it to be converted for demo purpose, you can also send it to info@qafe.com", true);
						
					} else if  (event.getResults().startsWith("UUID")) {
	
						String[] split = event.getResults().split("=");
						if (split.length == 2) {
							String uuid = split[1];
							MainFactoryActions.processUIByUUID(uuid, textArea);
							MainFactoryActions.notify("FMB uploaded with filename ["+fmbFile.getFilename()+"]","The message was sent by "+ emailBox.getText() +" with optional phonenr: "+phoneBox.getText());                                                                                                                                                                                                                                             
							tabPanel.selectTab(0);
						}
					} else {
						// firefox workaround
						String[] split = event.getResults().split("=");
						if (split.length == 2) {
							String uuid = split[1];
	
							uuid = uuid.replaceAll("</pre>", "");
							MainFactoryActions.processUIByUUID(uuid, textArea);
							MainFactoryActions.notify("FMB uploaded with filename ["+fmbFile.getFilename()+"]","The message was sent by "+ emailBox.getText() +" with optional phonenr: "+phoneBox.getText());                                                                                                                                                                                                                                             
							tabPanel.selectTab(0);
						} else {							
							ClientApplicationContext.getInstance().log(event.getResults());							
						}
					}

				} else {
					ClientApplicationContext.getInstance().log("The Forms Conversion process could not handle this file. Please check the file.","Check whether or not this file is an FMB (not an FMX)",true );
				}
			}
		});

		FlexTable tempFmbPanel = new FlexTable();
		tempFmbPanel.setWidget(0, 1, fmbFile);
		tempFmbPanel.setWidget(0, 0, new Label("Input for FMB"));

		tempFmbPanel.setWidget(1, 0, new HTML("<p>Note: the FMB you are uploading is only for <span style=\"color:red;\">demo</span> purpose.</p>" +
											  "<p>FMB's can have external dependencies like <span style=\"color:red;\">PLL, OLB's, images</span>,etc. Since they are <span style=\"color:red;\">not</span> included in the upload, the output might not appear correct.</p>" +
											  "<p>For a more detailed conversion of your FMB's please contact us at <span style=\"color:red;\">info@qafe.com </span></p> <p/>"+
											  "<p>Please fill in the information below, so that we can contact you for more information</p> "));
		tempFmbPanel.getFlexCellFormatter().setColSpan(1, 0, 2);
		
		tempFmbPanel.setWidget(2, 0, new Label("E-mail: (required)"));
		
		emailBox.setName("fmbEmail");
		emailBox.addBlurHandler(new BlurHandler(){

			public void onBlur(BlurEvent event) {
				String textValue=  ((TextBoxBase)event.getSource()).getText();
				if (textValue!=null){											
					if (textValue.replaceFirst(TextFieldGVO.REGEXP_TYPE_EMAIL_VALUE, "").length()>0){							
						ClientApplicationContext.getInstance().log("Email validation error", TextFieldGVO.TYPE_EMAIL_DEFAULT_MESSAGE,true);
					}						
				}
			
			}});
		tempFmbPanel.setWidget(2, 1, emailBox);
		
		
		tempFmbPanel.setWidget(3, 0, new Label("Phonenr:"));
		
		phoneBox.setName("fmbPhone");
		phoneBox.addBlurHandler(new BlurHandler(){

			public void onBlur(BlurEvent event) {
				
			}});
		tempFmbPanel.setWidget(3, 1, phoneBox);
		
		
		fmbForm.add(tempFmbPanel);

		panelFmbUpload.add(fmbForm);
		// Add a 'submit' button.
		panelFmbUpload.add(new Button("Generate", new ClickHandler() {

			public void onClick(ClickEvent event) {
				fmbForm.submit();

			}
		}));
		  // Add an event handler to the form.
		fmbForm.addSubmitHandler(new FormPanel.SubmitHandler() {
	      public void onSubmit(SubmitEvent event) {
	        // This event is fired just before the form is submitted. We can take
	        // this opportunity to perform validation.
	        if (emailBox.getText().length() == 0) {
	        	ClientApplicationContext.getInstance().log("Email validation error","Please fill in your email address",true);
	        	event.cancel();
	        }else if (fmbFile.getFilename()==null || fmbFile.getFilename().length()==0){
				ClientApplicationContext.getInstance().log("Uploaded file validation error", "There is no file selected. Please select one to continue",true);
				event.cancel();
			}
	      }
	    });
		
		tabPanel.add(panelFmbUpload, "Forms Conversion");

		vp.add(tabPanel);
		w.center();
		if (QAFEKeywordsGVO.SYSTEM_MENUITEM_TRYME_FORMS.equals(subwindow)){
			tabPanel.selectTab(1);
			
		}else {
			tabPanel.selectTab(0);
		}
	}

	public static void createWindowWithUrl(String title, String url) {
		createWindowWithUrl(title, url, 640, 480, true, true, 0, 0, false);

	}

	public static void createWindowWithUrl(String title, String url, int width, int height, boolean resizable, boolean centered, int top, int left, boolean modal) {
		Frame frame = new Frame(url);
		DOM.setStyleAttribute(frame.getElement(), "border", "none");

		if (ClientApplicationContext.getInstance().isMDI()) {
			final WindowPanel sized = new WindowPanel(title);
			sized.addCloseHandler(new CloseHandler<PopupPanel>() {
				public void onClose(CloseEvent<PopupPanel> event) {
					if (ClientApplicationContext.getInstance().internalWindowCount > 0) {
						ClientApplicationContext.getInstance().internalWindowCount--;
					}
				}
			});
			sized.setResizable(resizable);

			sized.setAnimationEnabled(true);
			sized.setSize("" + width + "px", "" + height + "px");
			sized.setModal(modal);
			sized.setWidget(frame);
			if (centered) {
				sized.center();
			} else if (top > 0 && left > 0) {
				sized.setPopupPosition(left, top);
				sized.show();
			} else {
				sized.show();
			}
		} else {
		//	ExtendedWindow.resizeTo(width, height);
			WindowFactory.setWidgetToMainPanel(frame, null);
		}

	}
	
	public static void showActivationDialog(String activationSignature, final AsyncCallback<String> callback) {
		final ShowPanelComponent showPanel = new ShowPanelComponent(false, true, 0, 0){
			@Override
			public void show() {
				super.show();
				DOM.setStyleAttribute(getElement(), "position", "fixed");
			}
		};
		
		String activationLink = "http://www.qafe.com/activation";
		VerticalPanel verticalPanel = new VerticalPanel();
		HTML html = new HTML("A valid activation file can not be found." 
				+ "<p>1) Please click <a target='_blank' href='" + activationLink + "?signature=" + activationSignature + "&product=qafe-platform'>here</a> to acquire the activation key" 
				+ ", use the activation signature: " + activationSignature 
				+ "</br>2) Then fill in the activation key you have received in the field below<p>");
		verticalPanel.add(html);
		
		FlexTable panel = new FlexTable();
		verticalPanel.add(panel);
		
		Label label = new Label("Activation key");
		panel.setWidget(0, 0, label);
		final TextBox textBoxActivationCode = new TextBox();		
		panel.setWidget(0, 1, textBoxActivationCode);

		Button buttonActivate = new Button("Activate");
		buttonActivate.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String activationCode = textBoxActivationCode.getText();
				if (activationCode.isEmpty()) {
					ClientApplicationContext.getInstance().log("", "Activation key is required", true);
					return;
				}
				callback.onSuccess(activationCode);
				showPanel.clear();
				showPanel.hide(true);
			}
		});
		panel.setWidget(0, 2, buttonActivate);
		
		showPanel.setWidget(verticalPanel);
		showPanel.center();
	}
	

	
}