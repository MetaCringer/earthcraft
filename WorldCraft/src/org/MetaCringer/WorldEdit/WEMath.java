package org.MetaCringer.WorldEdit;

public class WEMath {
	public static void main(String[] args) {
		double x = 13,z=2;
		
		System.out.println(interpolation(1, 1, 3, 2, 2));
		System.out.println(interpolation(0, 64, 15, 72, x));
		System.out.println(interpolation(0, interpolation(0, 64, 15, 72, x) , 15, interpolation(0, 32, 15, 66, x) , z));
		System.out.println(bilineInterpolation(new point(0, 64, 0), new point(0, 32, 15), new point(15, 72, 0), new point(15, 66, 15), x, z));
		System.out.println(cubicInterpolate(0,25,12,30,0.6));
	}
	
	public static double bilineInterpolation(point q11,point q12,point q21,point q22,double x, double z) { //Условие: q11 q12 и q21 q22 лежат на одной оси y
		return interpolation(q11.getZ(), interpolation(q11.getX(), q11.getY(), q21.getX(), q21.getY(), x) ,// q11 q21 i q12 q22 лежат на одной оси x
				q12.getZ(), interpolation(q12.getX(), q12.getY(), q22.getX(), q22.getY(), x) , z);
	}
	
	public static double interpolation(double x1,double y1, double x2, double y2, double x) {
		
		
		return  ((x2-x)/(x2-x1))*y1 + ((x-x1)/(x2-x1))*y2  ;
		
	}
	public static double cubicInterpolate(double y0,double y1,double y2,double y3,double x) {// работа с коэфициентами 0..1
		return y1 + (-0.5 * y0 + 0.5 * y2) * x
		        + (y0 - 2.5 * y1 + 2.0 * y2 - 0.5 * y3) * x * x
		        + (-0.5 * y0 + 1.5 * y1 - 1.5 * y2 + 0.5 * y3) * x * x * x;
	}
}
