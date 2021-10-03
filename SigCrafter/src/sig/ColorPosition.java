package sig;

import java.awt.Color;
import java.awt.Point;

public class ColorPosition {
	Point p;
	Color c;
	ColorPosition(int x,int y,int r,int g, int b) {
		this.p=new Point(x,y);
		this.c=new Color(r,g,b);
	}
}
