import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import processing.core.PConstants;
import processing.core.PGraphics;

public class CraterMarker extends CommonMarker{

	public static int radius = 5;
		
	public CraterMarker(Location location) {
		super(location);
	}
	
	
	public CraterMarker(Feature crater)
		{
		super(((PointFeature)crater).getLocation(), crater.getProperties());
		}
	
	public void drawMarker(PGraphics pg, float x, float y)
		{
		float radius = getRadius();
		pg.pushStyle();
		
		int color = 255 - (int) getDepth() * 50;
		
		if (isOnEarthLand()) pg.fill(255, color, color);
		else pg.fill(color, color, 255);
		
		pg.ellipse(x, y, radius, radius);
		
		// Restore previous drawing style
		pg.popStyle();
	}
	
	public void showTitle(PGraphics pg, float x, float y)
	{
		String name = getCrater() + " : " + 2 * getRadius() + " [km]";
		
		pg.pushStyle();
		
		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x+3, y-radius-33);
		
		pg.popStyle();
	}
	
	private String getCrater()
	{
		return getStringProperty("name");
	}
	private float getRadius()
		{
		return (float) Float.parseFloat(getProperty("radius").toString());
		}
	private float getDepth()
		{
		return (float) Float.parseFloat(getProperty("depth").toString());
		}
	private boolean isOnEarthLand()
		{
		return Boolean.parseBoolean(getProperty("isOnEarthLand").toString());
		}
	}