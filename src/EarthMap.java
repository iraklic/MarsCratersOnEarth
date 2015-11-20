import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;


public class EarthMap extends PApplet
	{
//	private String craterFile = "craterGeo.JSON";
	private String craterFile = "testcraterGeo.JSON";
	private String countryFile = "countries.geo.json";
	
	private List<Marker> craterMarkers;
	private List<Marker> countryMarkers;
	private int requestedCraterSize = 100;
	
	UnfoldingMap map;

	public void setup() {
		size(900, 700, OPENGL);
		map = new UnfoldingMap(this, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		map.setScaleRange(5, 50000);
		
//		map.zoomAndPanTo(14, new Location(32.881, -117.238));

		MapUtils.createDefaultEventDispatcher(this, map);
		
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		List<Feature> craters = GeoJSONReader.loadData(this, craterFile);
		craterMarkers = new ArrayList<Marker>();
		
		for(Feature crater : craters)
			{
			isOnEarthLand((PointFeature) crater);
			craterMarkers.add(new CraterMarker(crater));
			}
		map.addMarkers(craterMarkers);
		
		// run in setup to hide craters that are lower than initial value of requestedCraterSize
		cratersToShow();
		} // End Setup
	
	public void draw() {
		background(0);
		map.draw();
		cratersToShow();
		addKey();
	}
	
	public void mouseClicked()
		{
/*
		for(Marker marker : craterMarkers)
			{
			if (marker.isInside(map,  mouseX, mouseY)) 
				{
				marker.setSelected(true);
				}
			}
*/
		showThreshold();
		}
	
	private void showThreshold()
	{
	if (mouseX > 50 && mouseX < 110 && mouseY > 330 && mouseY < 350)
		{
		requestedCraterSize+=10;
		cratersToShow();
		}
	if (mouseX > 50 && mouseX < 110 && mouseY < 430 && mouseY > 410)
		{
		if(requestedCraterSize >= 10) requestedCraterSize-=10;
		cratersToShow();
		}
	}
	private void cratersToShow()
		{
		for(Marker marker : craterMarkers)
		{
			java.util.HashMap<String, Object> properties = marker.getProperties();
			float diameter = Float.parseFloat(properties.get("diameter").toString());
			properties.put("radius", ((diameter / 100) * map.getZoom())); // radius scaling is to match scales of the map
			if (Float.parseFloat(marker.getProperty("diameter").toString()) < requestedCraterSize) marker.setHidden(true);
			else marker.setHidden(false);
			}
		}
	
	private void addKey()
		{
		fill(0, 0, 0);
		textSize(20);
		text("This is how Mars Craters would look on Earth!", 30, 30);
//		Buttons		
		fill(100,100,100);
		triangle(50, 350, 80, 330, 110, 350);
		rect(50, 355, 60, 50);
		triangle(50, 410, 80, 430, 110, 410);
		fill(255, 0, 0);
		textSize(20);
		text(requestedCraterSize, 60, 380);
		fill(0, 0, 0);
		textSize(12);
		text("Select crater", 40, 440);
		text("diameter threshold", 40, 455);
		}
	
	private void isOnEarthLand(PointFeature crater)
		{
		for (Marker country : countryMarkers)
			{
			Location checkLoc = crater.getLocation();

			// some countries represented it as MultiMarker
			// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
			if(country.getClass() == MultiMarker.class)
				{	
				// looping over markers making up MultiMarker
				for(Marker marker : ((MultiMarker)country).getMarkers())
					{	
					// checking if inside
					if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc))
						{
						crater.addProperty("isOnEarthLand", "true");
						return;
						}
					}
				}
			// check if inside country represented by SimplePolygonMarker
			else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc))
				{
				crater.addProperty("isOnEarthLand", "true");
				return;
				}
			}
		// not inside any country
		crater.addProperty("isOnEarthLand", "false");
		}
	
	}