/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetMapConfigurationRequest;
import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.Api;
import org.geomajas.gwt.client.action.menu.AboutAction;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.MenuContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PaintableGroup;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.WorldPaintable;
import org.geomajas.gwt.client.gfx.paintable.Composite;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.MapAddon;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.PanButtonCollection;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.ScaleBar;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.Watermark;
import org.geomajas.gwt.client.gfx.paintable.mapaddon.ZoomAddon;
import org.geomajas.gwt.client.gfx.painter.CirclePainter;
import org.geomajas.gwt.client.gfx.painter.FeaturePainter;
import org.geomajas.gwt.client.gfx.painter.FeatureTransactionPainter;
import org.geomajas.gwt.client.gfx.painter.GeometryPainter;
import org.geomajas.gwt.client.gfx.painter.ImagePainter;
import org.geomajas.gwt.client.gfx.painter.MapModelPainter;
import org.geomajas.gwt.client.gfx.painter.RasterLayerPainter;
import org.geomajas.gwt.client.gfx.painter.RasterTilePainter;
import org.geomajas.gwt.client.gfx.painter.RectanglePainter;
import org.geomajas.gwt.client.gfx.painter.TextPainter;
import org.geomajas.gwt.client.gfx.painter.VectorLayerPainter;
import org.geomajas.gwt.client.gfx.painter.VectorTilePainter;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.event.LayerChangedHandler;
import org.geomajas.gwt.client.map.event.LayerLabeledEvent;
import org.geomajas.gwt.client.map.event.LayerShownEvent;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.Layer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.menu.Menu;

/**
 * The map widget is responsible for rendering the map model. It has a Model-View relationship with the map model. A
 * single controller for capturing user events can be set on the map.
 * 
 * @author Pieter De Graef
 */
@Api
public class MapWidget extends Canvas implements MapViewChangedHandler, MapModelHandler {

	private MapModel mapModel;

	private GraphicsWidget graphics;

	private PainterVisitor painterVisitor;

	protected boolean navigationAddonEnabled = true;

	protected boolean scaleBarEnabled = true;

	private ScaleBar scalebar;

	private Map<String, MapAddon> addons = new HashMap<String, MapAddon>();

	private double unitLength;

	private double pixelLength;

	/** Is zooming in and out using the mouse wheel enabled or not? This is true by default. */
	private boolean zoomOnScrollEnabled;

	/** Registration for the zoom on scroll handler. */
	private HandlerRegistration mouseWheelRegistration;

	private boolean resizedHandlerDisabled;

	private Menu defaultMenu;

	protected String applicationId;

	private PanButtonCollection panButtons;

	private ZoomAddon zoomAddon;

	private PaintableGroup rasterGroup = new Composite("raster");

	private PaintableGroup vectorGroup = new Composite("vector");

	private PaintableGroup worldGroup = new Composite("world");

	private PaintableGroup screenGroup = new Composite("screen");

	private List<WorldPaintable> worldSpacePaintables = new ArrayList<WorldPaintable>();

	/**
	 * Map groups: rendering should be done in one of these. Try to always use either the SCREEN or the WORLD group,
	 * unless you need something very specific and you know what you are doing.
	 */
	public enum RenderGroup {
		/**
		 * The pan group. Drawing should be done in pan coordinates. All raster layers are drawn in pan coordinates. In
		 * essence this means that the coordinates are expected to have been scaled for the current scale before
		 * drawing, and that only the translation still needs to occur. For advanced use only.
		 */
		RASTER,

		/**
		 * The pan group. Drawing should be done in pan coordinates. All vector layers, their selection and their labels
		 * are drawn in pan coordinates. In essence this means that the coordinates are expected to have been scaled for
		 * the current scale before drawing, and that only the translation still needs to occur. For advanced use only.
		 */
		VECTOR,

		/**
		 * The world group. Drawing should be done in world coordinates. World coordinates means that the map coordinate
		 * system is used. The advantage of rendering objects in the world group, is that when the user moves the map
		 * around, the objects will move with it.
		 */
		WORLD,

		/**
		 * The screen group. Drawing should be done in screen coordinates. Screen coordinates are expressed in pixels,
		 * starting from the top left corner of the map. When rendering objects in the screen group they will always
		 * appear at a fixed position, even when the user moves the map about.
		 */
		SCREEN
	}

	/**
	 * Rendering statuses.
	 */
	public enum RenderStatus {
		/**
		 * Render paintable, including all children.
		 */
		ALL,

		/**
		 * Redraw paintable, parent only.
		 */
		UPDATE,

		/**
		 * Delete the paintable.
		 */
		DELETE
	}

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public MapWidget(String id, String applicationId) {
		super(id);
		this.applicationId = applicationId;
		mapModel = new MapModel(getID() + "Graphics");
		mapModel.addMapModelHandler(this);
		mapModel.getMapView().addMapViewChangedHandler(this);
		graphics = new GraphicsWidget(this, getID() + "Graphics");
		painterVisitor = new PainterVisitor(graphics);
		mapModel.addFeatureSelectionHandler(new MapWidgetFeatureSelectionHandler(this));

		// Painter registration:
		painterVisitor.registerPainter(new CirclePainter());
		painterVisitor.registerPainter(new RectanglePainter());
		painterVisitor.registerPainter(new TextPainter());
		painterVisitor.registerPainter(new GeometryPainter());
		painterVisitor.registerPainter(new ImagePainter());
		painterVisitor.registerPainter(new MapModelPainter(this));
		painterVisitor.registerPainter(new RasterLayerPainter(this));
		painterVisitor.registerPainter(new RasterTilePainter());
		painterVisitor.registerPainter(new VectorLayerPainter(this));
		painterVisitor.registerPainter(new VectorTilePainter());
		painterVisitor.registerPainter(new FeatureTransactionPainter(this));

		defaultMenu = new Menu();
		defaultMenu.addItem(new AboutAction());
		setContextMenu(defaultMenu);

		// Resizing should work correctly!
		setWidth100();
		setHeight100();
		setDynamicContents(true);
		addResizedHandler(new MapResizedHandler(this));
		setZoomOnScrollEnabled(true);
	}

	// -------------------------------------------------------------------------
	// Class specific methods:
	// -------------------------------------------------------------------------

	/**
	 * Return the Object that represents one the default RenderGroups in the DOM tree when drawing.
	 * 
	 * @param group
	 *            The general group definition (RenderGroup.SCREEN, RenderGroup.WORLD, ...)
	 */
	public PaintableGroup getGroup(RenderGroup group) {
		switch (group) {
			case RASTER:
				return rasterGroup;
			case VECTOR:
				return vectorGroup;
			case WORLD:
				return worldGroup;
			case SCREEN:
			default:
				return screenGroup;
		}
	}

	/**
	 * Get the full list of registered WorldPaintable objects in this map. It is possible to add new instances to it.
	 */
	public List<WorldPaintable> getWorldSpacePaintables() {
		return worldSpacePaintables;
	}

	public double getUnitLength() {
		return unitLength;
	}

	public double getPixelLength() {
		return pixelLength;
	}

	public void registerPainter(Painter painter) {
		painterVisitor.registerPainter(painter);
	}

	public void unregisterPainter(Painter painter) {
		painterVisitor.unregisterPainter(painter);
	}

	public void registerMapAddon(MapAddon addon) {
		if (addon != null && !addons.containsKey(addon.getId())) {
			addons.put(addon.getId(), addon);
			addon.setMapSize(getWidth(), getHeight());
			render(addon, RenderGroup.SCREEN, RenderStatus.ALL);
			addon.onDraw();
		}
	}

	public void unregisterMapAddon(MapAddon addon) {
		if (addon != null && addons.containsKey(addon.getId())) {
			addons.remove(addon.getId());
			graphics.getVectorContext().deleteGroup(addon);
			addon.onRemove();
		}
	}

	/**
	 * Determines whether or not the zooming using the mouse wheel should be enabled or not.
	 * 
	 * @param zoomOnScrollEnabled
	 *            True or false. Enable or disable zooming using the mouse wheel.
	 */
	public void setZoomOnScrollEnabled(boolean zoomOnScrollEnabled) {
		if (mouseWheelRegistration != null) {
			mouseWheelRegistration.removeHandler();
			mouseWheelRegistration = null;
		}
		this.zoomOnScrollEnabled = zoomOnScrollEnabled;
		if (zoomOnScrollEnabled) {
			mouseWheelRegistration = graphics.addMouseWheelHandler(new ZoomOnScrollController());
		}
	}

	/**
	 * Is the zooming using the mouse wheel currently enabled or not?
	 */
	public boolean isZoomOnScrollEnabled() {
		return zoomOnScrollEnabled;
	}

	/**
	 * Apply a new <code>GraphicsController</code> on the map. This controller will handle all mouse-events that are
	 * global for the map. Only one controller can be set at any given time.
	 * 
	 * @param controller
	 *            The new <code>MapController</code> object.
	 */
	public void setController(GraphicsController controller) {
		graphics.setController(controller);
	}

	/**
	 * Set a new mouse wheel controller on the map. If the zoom on scroll is currently enabled, it will be disabled
	 * first.
	 * 
	 * @param controller
	 *            The new mouse wheel controller to be applied on the map.
	 */
	public void setMouseWheelController(MouseWheelHandler controller) {
		setZoomOnScrollEnabled(false);
		mouseWheelRegistration = graphics.addMouseWheelHandler(controller);
	}

	/**
	 * The main rendering method. Renders some paintable object in the given group, using the given status.
	 * 
	 * @param paintable
	 *            The actual object to be rendered. Should always contain location and styling information.
	 * @param renderGroup
	 *            In what group to render the paintable object?
	 * @param status
	 */
	public void render(Paintable paintable, RenderGroup renderGroup, RenderStatus status) {
		PaintableGroup group = null;
		if (renderGroup != null) {
			group = getGroup(renderGroup);
		}
		if (!graphics.isAttached()) {
			return;
		}
		if (paintable == null) {
			paintable = this.mapModel;
		}
		if (RenderStatus.DELETE.equals(status)) {
			List<Painter> painters = painterVisitor.getPaintersForObject(paintable);
			if (painters != null) {
				for (Painter painter : painters) {
					painter.deleteShape(paintable, group, graphics);
				}
			}
		} else {
			if (RenderStatus.ALL.equals(status)) {
				if (paintable == this.mapModel) {
					// Paint the world space paintable objects:
					for (WorldPaintable worldPaintable : worldSpacePaintables) {
						worldPaintable.transform(mapModel.getMapView().getWorldViewTransformer());
						worldPaintable.accept(painterVisitor, getGroup(RenderGroup.WORLD), mapModel.getMapView()
								.getBounds(), true);
					}
				}
				paintable.accept(painterVisitor, group, mapModel.getMapView().getBounds(), true);
			} else if (RenderStatus.UPDATE.equals(status)) {
				if (paintable == this.mapModel) {
					// Paint the world space paintable objects:
					for (WorldPaintable worldPaintable : worldSpacePaintables) {
						worldPaintable.transform(mapModel.getMapView().getWorldViewTransformer());
						worldPaintable.accept(painterVisitor, getGroup(RenderGroup.WORLD), mapModel.getMapView()
								.getBounds(), false);
					}
				}
				paintable.accept(painterVisitor, group, mapModel.getMapView().getBounds(), false);
			}
		}
	}

	/**
	 * Enables or disables the scalebar. This setting has immediate effect on the map.
	 * 
	 * @param enabled
	 *            set status
	 */
	public void setScalebarEnabled(boolean enabled) {
		scaleBarEnabled = enabled;
		if (scaleBarEnabled) {
			if (null == scalebar) {
				scalebar = new ScaleBar("scalebar", this);
				scalebar.setVerticalAlignment(VerticalAlignment.BOTTOM);
				scalebar.setHorizontalMargin(2);
				scalebar.setVerticalMargin(2);
			}
			scalebar.initialize(getMapModel().getMapInfo().getDisplayUnitType(), unitLength, new Coordinate(20,
					graphics.getHeight() - 25));
			scalebar.adjustScale(mapModel.getMapView().getCurrentScale());
			registerMapAddon(scalebar);
		} else {
			if (null != scalebar) {
				unregisterMapAddon(scalebar);
				scalebar = null;
			}
		}
	}

	public boolean isScaleBarEnabled() {
		return scaleBarEnabled;
	}

	/**
	 * Enables or disables the panning buttons. This setting has immediate effect on the map.
	 * 
	 * @param enabled
	 *            enabled status
	 */
	public void setNavigationAddonEnabled(boolean enabled) {
		navigationAddonEnabled = enabled;

		if (enabled) {
			panButtons = new PanButtonCollection("panBTNCollection", this);
			panButtons.setHorizontalMargin(5);
			panButtons.setVerticalMargin(5);
			registerMapAddon(panButtons);

			zoomAddon = new ZoomAddon("zoomAddon", this);
			zoomAddon.setHorizontalMargin(20);
			zoomAddon.setVerticalMargin(65);
			registerMapAddon(zoomAddon);
		} else {
			if (panButtons != null) {
				unregisterMapAddon(panButtons);
				unregisterMapAddon(zoomAddon);
			}
			panButtons = null;
			zoomAddon = null;
		}
	}

	public boolean isNavigationAddonEnabled() {
		return navigationAddonEnabled;
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public MapModel getMapModel() {
		return mapModel;
	}

	public MenuContext getMenuContext() {
		return graphics.getMenuContext();
	}

	public GraphicsContext getVectorContext() {
		return graphics.getVectorContext();
	}

	public PainterVisitor getPainterVisitor() {
		return painterVisitor;
	}

	@Override
	public void setContextMenu(Menu contextMenu) {
		if (null == contextMenu) {
			super.setContextMenu(defaultMenu);
		} else {
			super.setContextMenu(contextMenu);
		}
	}

	public void init() {
		GwtCommand commandRequest = new GwtCommand("command.configuration.GetMap");
		commandRequest.setCommandRequest(new GetMapConfigurationRequest(id, applicationId));
		GwtCommandDispatcher.getInstance().execute(commandRequest, new CommandCallback() {

			public void execute(CommandResponse response) {
				if (response instanceof GetMapConfigurationResponse) {
					GetMapConfigurationResponse r = (GetMapConfigurationResponse) response;
					initializationCallback(r);
				}
			}
		});

	}

	public void onMapViewChanged(MapViewChangedEvent event) {
		if (isDrawn()) {
			if (scaleBarEnabled && scalebar != null) {
				scalebar.adjustScale(mapModel.getMapView().getCurrentScale());
				render(scalebar, RenderGroup.SCREEN, RenderStatus.UPDATE);
			}
			render(mapModel, null, RenderStatus.ALL);
		}
	}

	public void onMapModelChange(MapModelEvent event) {
		render(mapModel, null, RenderStatus.ALL);
	}

	public ShapeStyle getLineSelectStyle() {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			return painter.getLineSelectStyle();
		}
		return null;
	}

	public void setLineSelectStyle(ShapeStyle lineSelectStyle) {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			painter.setLineSelectStyle(lineSelectStyle);
		}
	}

	public ShapeStyle getPointSelectStyle() {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			return painter.getPointSelectStyle();
		}
		return null;
	}

	public void setPointSelectStyle(ShapeStyle pointSelectStyle) {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			painter.setPointSelectStyle(pointSelectStyle);
		}
	}

	public ShapeStyle getPolygonSelectStyle() {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			return painter.getPolygonSelectStyle();
		}
		return null;
	}

	public void setPolygonSelectStyle(ShapeStyle polygonSelectStyle) {
		FeaturePainter painter = getFeaturePainter();
		if (painter != null) {
			painter.setPolygonSelectStyle(polygonSelectStyle);
		}
	}

	public void setCursor(Cursor cursor) {
		super.setCursor(cursor);
		graphics.getRasterContext().setCursor(null, cursor.getValue());
		graphics.getVectorContext().setCursor(null, cursor.getValue());
	}

	public boolean isResizedHandlerDisabled() {
		return resizedHandlerDisabled;
	}

	public void setResizedHandlerDisabled(boolean resizedHandlerDisabled) {
		this.resizedHandlerDisabled = resizedHandlerDisabled;
	}

	public Menu getDefaultMenu() {
		return defaultMenu;
	}

	public void setDefaultMenu(Menu defaultMenu) {
		this.defaultMenu = defaultMenu;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private FeaturePainter getFeaturePainter() {
		List<Painter> painters = painterVisitor.getPaintersForObject(new Feature());
		for (Painter painter : painters) {
			if (painter instanceof FeaturePainter) {
				return (FeaturePainter) painter;
			}
		}
		return null;
	}

	protected void onDraw() {
		super.onDraw();
		// must be called before anything else !
		render(mapModel, null, RenderStatus.ALL);
		final int width = getWidth();
		final int height = getHeight();
		mapModel.getMapView().setSize(width, height);
		init();
	}

	protected void initializationCallback(GetMapConfigurationResponse r) {
		if (r.getMapInfo() != null && !mapModel.isInitialized()) {
			ClientMapInfo info = r.getMapInfo();
			unitLength = info.getUnitLength();
			pixelLength = info.getPixelLength();
			graphics.setBackgroundColor(info.getBackgroundColor());
			mapModel.initialize(info);
			setNavigationAddonEnabled(info.isPanButtonsEnabled());
			setScalebarEnabled(info.isScaleBarEnabled());
			painterVisitor.registerPainter(new FeaturePainter(new ShapeStyle(info.getPointSelectStyle()),
					new ShapeStyle(info.getLineSelectStyle()), new ShapeStyle(info.getPolygonSelectStyle())));

			for (final Layer<?> layer : mapModel.getLayers()) {
				layer.addLayerChangedHandler(new LayerChangedHandler() {

					public void onLabelChange(LayerLabeledEvent event) {
						render(layer, null, RenderStatus.ALL);
					}

					public void onVisibleChange(LayerShownEvent event) {
						render(layer, null, RenderStatus.ALL);
					}
				});
			}

			// Register the watermark MapAddon:
			Watermark watermark = new Watermark(id + "-watermark", this);
			watermark.setAlignment(Alignment.RIGHT);
			watermark.setVerticalAlignment(VerticalAlignment.BOTTOM);
			registerMapAddon(watermark);
		}
	}

	// -------------------------------------------------------------------------
	// Private ResizedHandler class
	// -------------------------------------------------------------------------

	/**
	 * Handles map view and scalebar on resize
	 */
	private class MapResizedHandler implements ResizedHandler {

		private MapWidget map;

		public MapResizedHandler(MapWidget map) {
			this.map = map;
		}

		public void onResized(ResizedEvent event) {
			if (!map.isResizedHandlerDisabled()) {
				try {
					final int width = map.getWidth();
					final int height = map.getHeight();
					GWT.log("Map " + getID() + "resized: " + width + ", " + height, null);
					mapModel.getMapView().setSize(width, height);

					for (String addonId : addons.keySet()) {
						MapAddon addon = addons.get(addonId);
						addon.setMapSize(width, height);
						render(addon, RenderGroup.SCREEN, RenderStatus.UPDATE);
					}
				} catch (Exception e) {
					GWT.log("OnResized", e);
				}
			}
		}
	}

	/**
	 * Controller that allows for zooming when scrolling the mouse wheel.
	 * 
	 * @author Pieter De Graef
	 */
	private class ZoomOnScrollController implements MouseWheelHandler {

		public void onMouseWheel(MouseWheelEvent event) {
			if (event.isNorth()) {
				mapModel.getMapView().scale(2.0f, MapView.ZoomOption.LEVEL_CHANGE);
			} else {
				mapModel.getMapView().scale(0.5f, MapView.ZoomOption.LEVEL_CHANGE);
			}
		}
	}

	/**
	 * Renders feature on select/deselect
	 */
	private class MapWidgetFeatureSelectionHandler implements FeatureSelectionHandler {

		private MapWidget mapWidget;

		public MapWidgetFeatureSelectionHandler(MapWidget mapWidget) {
			this.mapWidget = mapWidget;
		}

		public void onFeatureSelected(FeatureSelectedEvent event) {
			mapWidget.render(event.getFeature(), RenderGroup.SCREEN, RenderStatus.UPDATE);
		}

		public void onFeatureDeselected(FeatureDeselectedEvent event) {
			mapWidget.render(event.getFeature(), RenderGroup.SCREEN, RenderStatus.DELETE);
		}
	}

}