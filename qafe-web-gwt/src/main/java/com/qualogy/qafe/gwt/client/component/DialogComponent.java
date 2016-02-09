/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.widgetideas.client.GlassPanel;
import com.google.gwt.widgetideas.table.client.overrides.FlexTable;
import com.qualogy.qafe.gwt.client.factory.MainFactoryActions;
import com.qualogy.qafe.gwt.client.vo.functions.dialog.GenericDialogGVO;

@SuppressWarnings("deprecation")
public class DialogComponent { 
	
	private static GlassPanel glassPanel = new GlassPanel(true);

	public DialogComponent() {}
	
	public static void showDialog(final String dialogTitle, String dialogMessage, String dialogType, String detailedMessage, int left, int top) {
		final ShowPanelComponent dialogBox = new ShowPanelComponent(false, true, left, top);
		final boolean expandAndShrinkDisclosureInCenter = left > 0 ? false : true;
		boolean showSendReportButton = false;
		
		FlexTable dialogComponentMainContainer = new FlexTable();
		
		Label close = new Label("X");
		close.setStyleName("qafe_close_on_showPanel");
		close.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		
		Label dialogTitleLabel = new Label(dialogTitle);
		if(dialogMessage != null && dialogMessage.length() > 1000) {
			detailedMessage = dialogMessage; 
			dialogMessage = dialogTitle;
			
		}
		HTML dialogMessageHTML = new HTML(dialogMessage);
		
		String imageURL = null;
		if(dialogType.equals(GenericDialogGVO.TYPE_ALERT)){
			imageURL = "images/alertDialogIcon.png";
		} else if(dialogType.equals(GenericDialogGVO.TYPE_ERROR)){
			showSendReportButton = true;
			imageURL = "images/errorDialogIcon.png";
		} else {
			imageURL = "images/infoDialogIcon.png";
		}
		Image dialogTypeImage = new Image(imageURL);
		
		FlexTable buttonsHolder = new FlexTable();
		Button okButton = new Button("OK");
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				glassPanel.removeFromParent();
				dialogBox.hide();
			}
		});
		okButton.setWidth("90px");
		buttonsHolder.setWidget(0, 1, okButton);
		
		if(detailedMessage != null) {
			if(showSendReportButton) {
				Button sendReportButton = new Button("Send Report");
				final String detailedMessageFinal = detailedMessage; 
				final String dialogMessageFinal = dialogMessage ; 
				sendReportButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						MainFactoryActions.notify("QAFE ERROR Report[" +dialogTitle+"]", dialogMessageFinal +"     \n"+ detailedMessageFinal);
						glassPanel.removeFromParent();
						dialogBox.hide();
					}
				});
				sendReportButton.setWidth("90px");
				buttonsHolder.setWidget(0, 2, sendReportButton);
			}
			
			
			final ScrollPanel scrollPanel = new ScrollPanel(new HTML(detailedMessage));
			if(dialogMessage != null && dialogMessage.length() > 100){
				scrollPanel.setSize("1000px", "200px");
			} else {
				scrollPanel.setSize("500px", "200px");
			}
			DisclosurePanel disclosurePanel = new DisclosurePanel("Details");
			disclosurePanel.add(scrollPanel);
			disclosurePanel.addOpenHandler(new OpenHandler<DisclosurePanel>(){
				public void onOpen(OpenEvent<DisclosurePanel> arg0) {
					if(expandAndShrinkDisclosureInCenter) {
						dialogBox.center();
					}
				} 
			});
			disclosurePanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
				public void onClose(CloseEvent<DisclosurePanel> arg0) {
					if(expandAndShrinkDisclosureInCenter) {
						dialogBox.center();
					}
				}
			});
			dialogComponentMainContainer.setWidget(3, 1, disclosurePanel);
			
		}
		
		dialogComponentMainContainer.setWidget(0, 3, close);
		dialogComponentMainContainer.setWidget(1, 0, dialogTitleLabel);
		dialogComponentMainContainer.setWidget(2, 0, dialogTypeImage);
		dialogComponentMainContainer.setWidget(2, 1, dialogMessageHTML);
		dialogComponentMainContainer.setWidget(4, 1, buttonsHolder);
		
		dialogComponentMainContainer.getFlexCellFormatter().setWidth(2, 0, "40px");
		dialogComponentMainContainer.getFlexCellFormatter().setColSpan(1, 0, 2);
		dialogComponentMainContainer.getFlexCellFormatter().setColSpan(3, 1, 2);
		dialogComponentMainContainer.getFlexCellFormatter().setColSpan(4, 1, 1);

		dialogBox.add(dialogComponentMainContainer);
		RootPanel.get().add(glassPanel, 0, 0);
		if(left == 0 && top == 0) {
			dialogBox.center();
		} else {
			dialogBox.setVisible(false);
			dialogBox.show();
			int dialogHalfWidth = (int) (dialogBox.getOffsetWidth()/3);
			int dialogHalfHeight = (int) (dialogBox.getOffsetHeight()/3);
			left = left-dialogHalfWidth < 0 ? 0 : left-dialogHalfWidth;
			top = top-dialogHalfHeight;
			dialogBox.setPopupPosition(left, top);
			dialogBox.setVisible(true);
		}
	}
}
