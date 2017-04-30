/**
 * Clqss for better handling of board coordinates
 */
public class Vec2 {
	private int x;
	private int y;
	
	public Vec2() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vec2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2(Vec2 copy) {
		this.x = copy.x;
		this.y = copy.y;
	}

	Vec2 add(Vec2 other) {
		return new Vec2(this.x + other.x, this.y + other.y);
	}
	
	Vec2 add(int x, int y) {
		return new Vec2(this.x + x, this.y + y);
	}
	
	boolean within(int x1, int y1, int x2, int y2) {
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}
	
	boolean within(Vec2 p1, Vec2 p2) {
		return x >= p1.x && x <= p2.x && y >= p1.y && y <= p2.y;
	}
	
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	
	@Override
	public String toString() {
		return Character.toString((char)('a' + this.x)) + Integer.toString(8 - this.y);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Vec2))
			return false;
		Vec2 other = (Vec2) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public boolean equals(int x, int y) {
		if (x != this.x)
			return false;
		if (y != this.y)
			return false;
		return true;
	}
}
