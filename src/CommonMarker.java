import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;


public abstract class CommonMarker extends SimplePointMarker {

	private int craterSize;
	// Records whether this marker has been clicked (most recently)
	protected boolean clicked = false;
	
	public CommonMarker(Location location) {
		super(location);
	}
	
	public CommonMarker(Location location, java.util.HashMap<java.lang.String,java.lang.Object> properties) {
		super(location, properties);
	}
	
	// Getter method for clicked field
	public boolean getClicked() {
		return clicked;
	}
	
	// Setter method for clicked field
	public void setClicked(boolean state) {
		clicked = state;
	}
	
	public void setSize(int size)
	{
		craterSize = size;
	}
	public int getSize()
	{
		return craterSize;
	}
	
	// Common piece of drawing method for markers; 
	// YOU WILL IMPLEMENT. 
	// Note that you should implement this by making calls 
	// drawMarker and showTitle, which are abstract methods 
	// implemented in subclasses
	public void draw(PGraphics pg, float x, float y)
		{
		if (!hidden)
			{
			drawMarker(pg, x, y);
			}
//		if (selected) {showTitle(pg, x, y);}
	}
	public abstract void drawMarker(PGraphics pg, float x, float y);
	public abstract void showTitle(PGraphics pg, float x, float y);
}