/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.widget;

import com.google.gwt.core.client.GWT;
import org.geomajas.annotation.Api;
import org.geomajas.global.ExceptionDto;
import org.geomajas.gwt.client.i18n.GlobalMessages;
import org.geomajas.gwt.client.i18n.I18nProvider;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.client.util.WidgetLayout;

/**
 * <p>
 * Modal error messaging window that displays an exception. This window is used in case the server throws up an
 * exception during command execution. This window shows the general exception message, and adds an option to view more
 * details about the error. The details will then display the Java class name of the exception and the entire stack
 * trace.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api
public class ExceptionWindow extends Window {

	private static final GlobalMessages MESSAGES = GWT.create(GlobalMessages.class);

	/** The list of exceptions this window should display. */
	private ExceptionDto error;

	/** The button that displays the details about the exception upon clicking. */
	private Button expandButton;

	/** The actual layout that contains the details. Invisible by default. */
	private VLayout detailsLayout;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create a new error messaging window displaying a single exception.
	 * 
	 * @param error The exception to display.
	 * @since 1.10.0
	 */
	@Api
	public ExceptionWindow(ExceptionDto error) {
		super();
		this.error = error;

		buildGui();
		setDetailsVisible(false);
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	/** Build the entire GUI for this widget. */
	private void buildGui() {
		setTitle(I18nProvider.getGlobal().commandError());
		setHeaderIcon(WidgetLayout.iconError);
		setIsModal(true);
		setShowModalMask(true);
		setModalMaskOpacity(WidgetLayout.modalMaskOpacity);
		setWidth(WidgetLayout.exceptionWindowWidth);
		setHeight(WidgetLayout.exceptionWindowHeightNormal);
		setCanDragResize(true);
		centerInPage();
		setAutoSize(true);

		addItem(createErrorLayout(error));
	}

	/**
	 * Create the GUI for a single exception.
	 *
	 * @param error error to report
	 * @return layout
	 */
	private VLayout createErrorLayout(ExceptionDto error) {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setPadding(WidgetLayout.marginLarge);

		HLayout topLayout = new HLayout(WidgetLayout.marginLarge);
		topLayout.setWidth100();
		Img icon = new Img(WidgetLayout.iconError,
				WidgetLayout.exceptionWindowIconSize, WidgetLayout.exceptionWindowIconSize);
		topLayout.addMember(icon);
		HTMLFlow message = new HTMLFlow();
		message.setWidth100();
		message.setHeight100();
		message.setLayoutAlign(VerticalAlignment.TOP);
		message.setContents(WidgetLayout.divStyle(WidgetLayout.exceptionWindowMessageStyle, error.getMessage()));
		topLayout.addMember(message);
		layout.addMember(topLayout);

		if (error.getStackTrace() != null && error.getStackTrace().length > 0) {
			expandButton = new Button(MESSAGES.exceptionDetailsView());
			expandButton.setWidth(WidgetLayout.exceptionWindowButtonWidth);
			expandButton.setLayoutAlign(Alignment.RIGHT);
			expandButton.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					setDetailsVisible(!detailsLayout.isVisible());
				}
			});
			layout.addMember(expandButton);

			String content = getDetails(error);
			HTMLPane detailPane = new HTMLPane();
			detailPane.setContents(content);
			detailPane.setWidth100();
			detailPane.setHeight100();
			detailsLayout = new VLayout();
			detailsLayout.setWidth100();
			detailsLayout.setHeight100();
			detailsLayout.addMember(detailPane);
			detailsLayout.setBorder(WidgetLayout.exceptionWindowDetailBorderStyle);
			layout.addMember(detailsLayout);
		}
		return layout;
	}

	/**
	 * Build details message for an exception.
	 *
	 * @param error error to build message for
	 * @return HTML string with details message
	 */
	private String getDetails(ExceptionDto error) {
		if (null == error) {
			return "";
		}
		StringBuilder content = new StringBuilder();
		content.append(WidgetLayout.divStyle(WidgetLayout.exceptionWindowDetailHeaderStyle, error.getClassName()));
		for (StackTraceElement el : error.getStackTrace()) {
			String style =  WidgetLayout.exceptionWindowDetailTraceNormalStyle;
			String line = el.toString();
			if ((line.startsWith("org.geomajas.") && !line.startsWith("org.geomajas.example.")) ||
					line.startsWith("org.springframework.") ||
					line.startsWith("org.hibernate.") ||
					line.startsWith("org.hibernatespatial.") ||
					line.startsWith("org.geotools.") ||
					line.startsWith("com.vividsolutions.") ||
					line.startsWith("org.mortbay.jetty.") ||
					line.startsWith("sun.") ||
					line.startsWith("java.") ||
					line.startsWith("javax.") ||
					line.startsWith("com.google.")) {
				style = WidgetLayout.exceptionWindowDetailTraceLessStyle;
			}
			content.append(WidgetLayout.divStyle(style, line));
		}
		content.append(getDetails(error.getCause()));
		return content.toString();
	}

	/**
	 * Toggle the visibility of the exception details.
	 *
	 * @param detailsVisible should details be visible
	 */
	private void setDetailsVisible(boolean detailsVisible) {
		detailsLayout.setVisible(detailsVisible);
		if (detailsVisible) {
			setAutoSize(false);
			expandButton.setTitle(MESSAGES.exceptionDetailsHide());
			setHeight(WidgetLayout.exceptionWindowHeightDetails);
		} else {
			expandButton.setTitle(MESSAGES.exceptionDetailsView());
			setHeight(WidgetLayout.exceptionWindowHeightNormal);
		}
	}
}