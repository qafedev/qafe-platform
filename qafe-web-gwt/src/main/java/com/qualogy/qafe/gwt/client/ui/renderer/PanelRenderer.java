/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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
package com.qualogy.qafe.gwt.client.ui.renderer;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.qualogy.qafe.gwt.client.component.QHidden;
import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.ui.images.DataGridImageBundle;
import com.qualogy.qafe.gwt.client.ui.renderer.AbstractComponentRenderer;
import com.qualogy.qafe.gwt.client.ui.renderer.RendererHelper;
import com.qualogy.qafe.gwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.gwt.client.vo.layout.AbsoluteLayoutGVO;
import com.qualogy.qafe.gwt.client.vo.layout.AutoLayoutGVO;
import com.qualogy.qafe.gwt.client.vo.layout.BorderLayoutGVO;
import com.qualogy.qafe.gwt.client.vo.layout.GridLayoutGVO;
import com.qualogy.qafe.gwt.client.vo.layout.HorizontalLayoutGVO;
import com.qualogy.qafe.gwt.client.vo.layout.LayoutGVO;
import com.qualogy.qafe.gwt.client.vo.layout.VerticalLayoutGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ComponentGVO;
import com.qualogy.qafe.gwt.client.vo.ui.ElementGVO;
import com.qualogy.qafe.gwt.client.vo.ui.PanelGVO;
import com.qualogy.qafe.gwt.client.vo.ui.RootPanelGVO;

public class PanelRenderer extends AbstractComponentRenderer {

	private DataGridImageBundle dataGridImageBundle;

	public PanelRenderer() {
		dataGridImageBundle = ClientApplicationContext.getInstance().getDatagridImageBundle();
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public UIObject render(ComponentGVO component, String uuid, String parent, String context) {
		UIObject panel = null;
		if (component != null) {
			if (component instanceof PanelGVO) {
				final ComponentGVO finalComponentGVO = component;
				final String finalUuid = uuid;
				final String finalParent = parent;
				final PanelGVO root = (PanelGVO) component;
				LayoutGVO layout = root.getLayout();
				if (layout instanceof AutoLayoutGVO) {
					if (root.getMenu() != null && !(component instanceof RootPanelGVO)) {
						panel = new FlexTable() {
							@Override
							public void onBrowserEvent(Event event) {
								if (event.getTypeInt() == Event.ONCONTEXTMENU) {
									DOM.eventPreventDefault(event);
									applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
								}
								super.onBrowserEvent(event);
							}

							@Override
							protected void setElement(Element elem) {
								super.setElement(elem);
								sinkEvents(Event.ONCONTEXTMENU);
							}
						};
					} else {
						panel = new FlexTable();
					}
					panel.setTitle(component.getTooltip());
					AutoLayoutGVO autoLayoutGVO = (AutoLayoutGVO) layout;
					int columns = autoLayoutGVO.getCols() != null ? autoLayoutGVO.getCols().intValue() : 1;
					UIObject[] children = renderChildComponents(layout.getComponents(), uuid, parent, context);

					if (children != null) {
						int nrOfRows = (children.length / columns) + 1;

						for (int i = 0; i < nrOfRows; i++) {
							for (int j = 0; j < columns; j++) {
								int element = (i * columns) + j;
								if (element < children.length) {
									if (children[element] != null) {
										if (children[element] instanceof Widget) {
											((FlexTable) panel).setWidget(i, j, (Widget) children[element]);
										}
									}
								}
							}
						}
					}
				} else if (layout instanceof AbsoluteLayoutGVO) {
					AbsoluteLayoutGVO absoluteLayout = (AbsoluteLayoutGVO) layout;
					if (root.getMenu() != null && !(component instanceof RootPanelGVO)) {
						panel = new AbsolutePanel() {
							@Override
							public void onBrowserEvent(Event event) {
								if (event.getTypeInt() == Event.ONCONTEXTMENU) {
									DOM.eventPreventDefault(event);
									applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
								}
								super.onBrowserEvent(event);
							}

							@Override
							protected void setElement(Element elem) {
								super.setElement(elem);
								sinkEvents(Event.ONCONTEXTMENU);
							}
						};
					} else {
						panel = new AbsolutePanel();
					}
					AbsolutePanel absolutePanel = (AbsolutePanel) panel;
					ElementGVO[] elementGVOs = absoluteLayout.getElements();
					if (elementGVOs != null) {
						for (int i = 0; i < elementGVOs.length; i++) {
							UIObject uiObject = super.renderChildComponent(elementGVOs[i].getComponent(), uuid, parent, context);
							if (uiObject instanceof Widget) {
								absolutePanel.add((Widget) uiObject, elementGVOs[i].getX(), elementGVOs[i].getY());
							}
						}
					}
				} else if (layout instanceof GridLayoutGVO) {
					GridLayoutGVO gridLayout = (GridLayoutGVO) layout;
					if (root.getMenu() != null && !(component instanceof RootPanelGVO)) {
						panel = new FlexTable() {
							@Override
							public void onBrowserEvent(Event event) {
								if (event.getTypeInt() == Event.ONCONTEXTMENU) {
									DOM.eventPreventDefault(event);
									applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
								}
								super.onBrowserEvent(event);
							}

							@Override
							protected void setElement(Element elem) {
								super.setElement(elem);
								sinkEvents(Event.ONCONTEXTMENU);
							}
						};
					} else {
						panel = new FlexTable();
					}
					ElementGVO[] elementGVOs = gridLayout.getElements();
					if (elementGVOs != null) {
						int maxX = 0;
						int maxY = 0;
						for (int i = 0; i < elementGVOs.length; i++) {
							if (maxX < elementGVOs[i].getX()) {
								maxX = elementGVOs[i].getX();
							}
							if (maxY < elementGVOs[i].getY()) {
								maxY = elementGVOs[i].getY();
							}
						}
						RendererHelper.addMenu(component, panel, uuid, parent);
						FlexTable flexTable = (FlexTable) panel;
						flexTable.setTitle(root.getTooltip());
						for (int i = 0; i < elementGVOs.length; i++) {
							UIObject uiObject = super.renderChildComponent(elementGVOs[i].getComponent(), uuid, parent, context);
							if (uiObject instanceof Widget) {
								flexTable.setWidget(elementGVOs[i].getY(), elementGVOs[i].getX(), (Widget) uiObject);
								if (elementGVOs[i].getStyleClass() != null && elementGVOs[i].getStyleClass().length() > 0) {
									flexTable.getFlexCellFormatter().setStyleName(elementGVOs[i].getY(), elementGVOs[i].getX(), elementGVOs[i].getStyleClass());
								}
								Element gvoElement = flexTable.getFlexCellFormatter().getElement(elementGVOs[i].getY(), elementGVOs[i].getX());
								RendererHelper.setStyleForElement(gvoElement, elementGVOs[i].getStyleProperties());
								if (elementGVOs[i].getGridwidth() > 0) {
									flexTable.getFlexCellFormatter().setColSpan(elementGVOs[i].getY(), elementGVOs[i].getX(), elementGVOs[i].getGridwidth());
								}
								if (elementGVOs[i].getGridheight() > 0) {
									flexTable.getFlexCellFormatter().setRowSpan(elementGVOs[i].getY(), elementGVOs[i].getX(), elementGVOs[i].getGridheight());
								}
							}
						}
					}
				} else if (layout instanceof HorizontalLayoutGVO) {
					if (root.getMenu() != null && !(component instanceof RootPanelGVO)) {
						panel = new HorizontalPanel() {
							@Override
							public void onBrowserEvent(Event event) {
								if (event.getTypeInt() == Event.ONCONTEXTMENU) {
									DOM.eventPreventDefault(event);
									applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
								}
								super.onBrowserEvent(event);
							}

							@Override
							protected void setElement(Element elem) {
								super.setElement(elem);
								sinkEvents(Event.ONCONTEXTMENU);
							}
						};
					} else {
						panel = new HorizontalPanel();
					}
					performCommonTasks(root, panel, uuid, parent);
				} else if (layout instanceof VerticalLayoutGVO) {
					if (root.getMenu() != null && !(component instanceof RootPanelGVO)) {
						panel = new VerticalPanel() {
							@Override
							public void onBrowserEvent(Event event) {
								if (event.getTypeInt() == Event.ONCONTEXTMENU) {
									DOM.eventPreventDefault(event);
									applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
								}
								super.onBrowserEvent(event);
							}

							@Override
							protected void setElement(Element elem) {
								super.setElement(elem);
								sinkEvents(Event.ONCONTEXTMENU);
							}
						};
					} else {
						panel = new VerticalPanel();
					}
					performCommonTasks(root, panel, uuid, parent);
				} else if (layout instanceof AbsoluteLayoutGVO) {
					if (root.getMenu() != null && !(component instanceof RootPanelGVO)) {
						panel = new DockPanel() {
							@Override
							public void onBrowserEvent(Event event) {
								if (event.getTypeInt() == Event.ONCONTEXTMENU) {
									DOM.eventPreventDefault(event);
									applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
								}
								super.onBrowserEvent(event);
							}

							@Override
							protected void setElement(Element elem) {
								super.setElement(elem);
								sinkEvents(Event.ONCONTEXTMENU);
							}
						};
					} else {
						panel = new DockPanel();
					}
					performCommonTasks(root, (Panel) panel, uuid, parent);
				} else if (layout instanceof BorderLayoutGVO) {
					if (root.getMenu() != null && !(component instanceof RootPanelGVO)) {
						panel = new DockPanel() {
							@Override
							public void onBrowserEvent(Event event) {
								if (event.getTypeInt() == Event.ONCONTEXTMENU) {
									DOM.eventPreventDefault(event);
									applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
								}
								super.onBrowserEvent(event);
							}

							@Override
							protected void setElement(Element elem) {
								super.setElement(elem);
								sinkEvents(Event.ONCONTEXTMENU);
							}
						};
					} else {
						panel = new DockPanel();
					}
					DockPanel dockPanel = (DockPanel) panel;
					dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
					dockPanel.setSpacing(3);
					BorderLayoutGVO borderLayoutGVO = (BorderLayoutGVO) layout;
					if (borderLayoutGVO.getNorth() != null) {
						dockPanel.add((Widget) renderChildComponent(borderLayoutGVO.getNorth(), uuid, parent, context), DockPanel.NORTH);
					}
					if (borderLayoutGVO.getSouth() != null) {
						dockPanel.add((Widget) renderChildComponent(borderLayoutGVO.getSouth(), uuid, parent, context), DockPanel.SOUTH);
					}
					if (borderLayoutGVO.getEast() != null) {
						dockPanel.add((Widget) renderChildComponent(borderLayoutGVO.getEast(), uuid, parent, context), DockPanel.EAST);
					}
					if (borderLayoutGVO.getWest() != null) {
						dockPanel.add((Widget) renderChildComponent(borderLayoutGVO.getWest(), uuid, parent, context), DockPanel.WEST);
					}
					if (borderLayoutGVO.getCenter() != null) {
						dockPanel.add((Widget) renderChildComponent(borderLayoutGVO.getCenter(), uuid, parent, context), DockPanel.CENTER);
					}

				}
				if (root.getConcurrentModificationEnabled() && (root.getFieldName() != null) && !root.getFieldName().isEmpty()) {
					addChecksum(panel);
				}
				if (root.getFieldName() != null && root.getFieldName().length() > 0 && root.getShowdatacontrol() != null && root.getShowdatacontrol().booleanValue()) {
					DockPanel dockPanel = null;
					if (root.getMenu() != null && !(component instanceof RootPanelGVO)) {
						dockPanel = new DockPanel() {
							@Override
							public void onBrowserEvent(Event event) {
								if (event.getTypeInt() == Event.ONCONTEXTMENU) {
									DOM.eventPreventDefault(event);
									applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
								}
								super.onBrowserEvent(event);
							}

							@Override
							protected void setElement(Element elem) {
								super.setElement(elem);
								sinkEvents(Event.ONCONTEXTMENU);
							}
						};
					} else {
						dockPanel = new DockPanel();
					}
					dockPanel.add(createDataPanelToolBar(root, uuid, parent), DockPanel.NORTH);
					dockPanel.add((Widget) panel, DockPanel.NORTH);
					panel = dockPanel;
				}
				if(root.getDisclosure()){
					DisclosurePanel disclosurePanel = new DisclosurePanel(root.getTitle());
					disclosurePanel.setAnimationEnabled(true);
					disclosurePanel.add((Widget) panel);
					panel = disclosurePanel;
				} else if (root.getTitle() != null && root.getTitle().length() > 0) {
					CaptionPanel titledPanel = null;
					if (root.getMenu() != null && !(component instanceof RootPanelGVO)) {
						titledPanel = new CaptionPanel(root.getTitle()) {
							@Override
							public void onBrowserEvent(Event event) {
								if (event.getTypeInt() == Event.ONCONTEXTMENU) {
									DOM.eventPreventDefault(event);
									applyContextMenu(event, finalComponentGVO, finalUuid, finalParent);
								}
								super.onBrowserEvent(event);
							}

							@Override
							protected void setElement(Element elem) {
								super.setElement(elem);
								sinkEvents(Event.ONCONTEXTMENU);
							}
						};
					} else {
						titledPanel = new CaptionPanel(root.getTitle());
					}
					titledPanel.add((Widget) panel);
					panel = titledPanel;
				}

				RendererHelper.fillIn(component, panel, uuid, parent, context);
			}
		}
		return panel;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	protected void performCommonTasks(PanelGVO component, UIObject panel, String uuid, String parent) {
		if (component != null && panel != null) {
			panel.setTitle(component.getTooltip());
			UIObject[] children = renderChildComponents(component.getLayout().getComponents(), uuid, parent, component.getContext());
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					if (children[i] != null) {
						if (children[i] instanceof Widget) {
							if (panel instanceof DockPanel) {
								((DockPanel) panel).add((Widget) children[i], DockPanel.NORTH);
							} else if (panel instanceof HasWidgets) {
								((HasWidgets) panel).add((Widget) children[i]);
							}
							RendererHelper.setStyleForElement(children[i].getElement(), component.getLayout().getComponents()[i].getStyleProperties());
						}
					}
				}
			}
		}
	}

	private HorizontalPanel createDataPanelToolBar(PanelGVO root, String uuid, String parent) {
		HorizontalPanel tb = new HorizontalPanel();
		tb.setSpacing(5);
		tb.setStyleName("x-toolbar");
		if (root.getDataPanelControl() != null) {
			int index = 0;
			for (ComponentGVO componentGVO : root.getDataPanelControl()) {
				tb.add(createDataPanelToolBarButton(componentGVO, uuid, parent, index));
				index++;
			}
		}
		return tb;
	}

	private Widget createDataPanelToolBarButton(ComponentGVO gvo, String uuid, String parent, int index) {
		Image ui = null;
		switch (index) {
		case 0:
			ui = dataGridImageBundle.saveIcon().createImage();
			break;
		case 1:
			ui = dataGridImageBundle.backwardsIcon().createImage();
			break;
		case 2:
			ui = dataGridImageBundle.forwardIcon().createImage();
			break;
		case 3:
			ui = dataGridImageBundle.refreshIcon().createImage();
			break;
		default:
			ui = dataGridImageBundle.saveIcon().createImage();
		}
		RendererHelper.fillIn(gvo, ui, uuid, parent, gvo.getContext());
		ui.setSize("16px", "16px");
		ui.setTitle(gvo.getTooltip());
		return ui;
	}

	private void addChecksum(UIObject panel) {
		QHidden qafeChecksum = new QHidden();
		DOM.setElementAttribute(qafeChecksum.getElement(), "fn", DataContainerGVO.QAFE_CHECKSUM);
		if (panel instanceof FlexTable) {
			FlexTable flexTable = (FlexTable)panel;
			int rowCount = flexTable.getRowCount();
			flexTable.setWidget(rowCount, 0, qafeChecksum);
		} else if (panel instanceof AbsolutePanel) {
			AbsolutePanel absolutePanel = (AbsolutePanel)panel;
			absolutePanel.add(qafeChecksum, 0, 0);
		} else if (panel instanceof DockPanel) {
			DockPanel dockPanel = (DockPanel)panel;
			//dockPanel.add(qafeChecksum);
		} else if (panel instanceof CellPanel) {
			CellPanel cellPanel = (CellPanel)panel;
			cellPanel.add(qafeChecksum);
		}
	}
}