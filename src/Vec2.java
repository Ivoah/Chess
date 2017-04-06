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
}
