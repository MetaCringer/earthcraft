package org.MetaCringer.util;

public class Color {
	private byte r,g,b;
	public byte getR() {
		return r;
	}
	public byte getG() {
		return g;
	}
	public byte getB() {
		return b;
	}
	public Color(byte r,byte g,byte b) {
		this.r =r;
		this.g =g;
		this.b =b;
	}
	public Color(int color) {
		b=(byte)color;
		color >>= 8;
		g=(byte)color;
		color >>= 8;
		r=(byte)color;
	}
	public boolean compare(Color c) {
		return ((c.r == r) && (c.g == g) && (c.b == b)) ? true : false;
	}
	public int getInt() {
		return (b) | (g << 8) | (r << 16);
	}
}
