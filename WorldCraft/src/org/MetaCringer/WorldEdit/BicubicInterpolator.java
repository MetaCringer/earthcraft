package org.MetaCringer.WorldEdit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.MetaCringer.util.Color;

public class BicubicInterpolator {

	public static void main(String[] args) {
		int height=100,weight=100;
		BufferedImage img= new BufferedImage(weight, height, BufferedImage.TYPE_3BYTE_BGR);
		/*for(int z=0;z<height;z++) {
			for(int x = 0; x<weight;x++) {
				
				img.setRGB(x, z, 0x00ffffff);
			}
		}
		for(int x = 0; x<weight;x++) {
			System.out.println(((double)x)/weight);
			img.setRGB(x, (int) Math.round(WEMath.cubicInterpolate(1, 50, 20, 80, ((double)x)/weight)),0);
		}
		try {
			ImageIO.write(img, "PNG", new File("img.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		double[][] heights = 
			   {{-400,-200,200,220},
				{-600,100,100,190},
				{200,200,100,-200},
				{100,50,140,40}};
		BicubicInterpolator interpolator= null;
		try {
			interpolator = new BicubicInterpolator(heights);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		double value=0;
		for(int z=0;z<height;z++) {
			for(int x = 0; x<weight;x++) {
				value = interpolator.interpolate(((double)x)/weight, ((double)z)/height);
				System.out.println(value);
				img.setRGB(x, z, (((int)value)<<16) | (0x80<<8) | 0x80);
			}
		}
		img.setRGB(10, 10, (0xff << 16));
		img.setRGB(90, 10, (0xff << 8));
		img.setRGB(90, 90, 0xff);
		try {
			ImageIO.write(img, "PNG", new File("img.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();

	}
	
	double[][] p;
	double[][] a = new double[4][4];
	public BicubicInterpolator(double[][] p) throws Exception {
		this.update(p);
	}
	public void update(double[][] p) throws Exception {
		if(p.length < 4) {
			throw new Exception("Не достаточно точек аргументов");
		}
		for (int i=0; i<4;i++) {
			if(p[i].length < 4) {
				throw new Exception("Не достаточно точек аргументов");
			}
		}
		this.p = p;
		
		 a[0][0]  = p[1][1];
	        a[0][1]  = -0.5 * p[1][0] + 0.5 * p[1][2];
	        a[0][2]  = p[1][0] - 2.5 * p[1][1] + 2 * p[1][2] - 0.5 * p[1][3];
	        a[0][3]  = -0.5 * p[1][0] + 1.5 * p[1][1] - 1.5 * p[1][2] + 0.5 * p[1][3];
	        a[1][0]  = -0.5 * p[0][1] + 0.5 * p[2][1];
	        a[1][1]  = 0.25 * p[0][0] - 0.25 * p[0][2] - 0.25 * p[2][0] + 0.25 * p[2][2];
	        a[1][2]  = -0.5 * p[0][0] + 1.25 * p[0][1] - p[0][2] + 0.25 * p[0][3]
	                    + 0.5 * p[2][0] - 1.25 * p[2][1] + p[2][2] - 0.25 * p[2][3];
	        a[1][3]  = 0.25 * p[0][0] - 0.75 * p[0][1] + 0.75 * p[0][2] - 0.25 * p[0][3]
	                    - 0.25 * p[2][0] + 0.75 * p[2][1] - 0.75 * p[2][2] + 0.25 * p[2][3];
	        a[2][0]  = p[0][1] - 2.5 * p[1][1] + 2 * p[2][1] - 0.5 * p[3][1];
	        a[2][1]  = -0.5 * p[0][0] + 0.5 * p[0][2] + 1.25 * p[1][0] - 1.25 * p[1][2]
	                    - p[2][0] + p[2][2] + 0.25 * p[3][0] - 0.25 * p[3][2];
	        a[2][2]  = p[0][0] - 2.5 * p[0][1] + 2 * p[0][2] - 0.5 * p[0][3] - 2.5 * p[1][0]
	                    + 6.25 * p[1][1] - 5 * p[1][2] + 1.25 * p[1][3] + 2 * p[2][0]
	                    - 5 * p[2][1] + 4 * p[2][2] - p[2][3] - 0.5 * p[3][0]
	                    + 1.25 * p[3][1] - p[3][2] + 0.25 * p[3][3];
	        a[2][3]  = -0.5 * p[0][0] + 1.5 * p[0][1] - 1.5 * p[0][2] + 0.5 * p[0][3]
	                    + 1.25 * p[1][0] - 3.75 * p[1][1] + 3.75 * p[1][2]
	                    - 1.25 * p[1][3] - p[2][0] + 3 * p[2][1] - 3 * p[2][2] + p[2][3]
	                    + 0.25 * p[3][0] - 0.75 * p[3][1] + 0.75 * p[3][2] - 0.25 * p[3][3];
	        a[3][0]  = -0.5 * p[0][1] + 1.5 * p[1][1] - 1.5 * p[2][1] + 0.5 * p[3][1];
	        a[3][1]  = 0.25 * p[0][0] - 0.25 * p[0][2] - 0.75 * p[1][0] + 0.75 * p[1][2]
	                    + 0.75 * p[2][0] - 0.75 * p[2][2] - 0.25 * p[3][0] + 0.25 * p[3][2];
	        a[3][2]  = -0.5 * p[0][0] + 1.25 * p[0][1] - p[0][2] + 0.25 * p[0][3]
	                    + 1.5 * p[1][0] - 3.75 * p[1][1] + 3 * p[1][2] - 0.75 * p[1][3]
	                    - 1.5 * p[2][0] + 3.75 * p[2][1] - 3 * p[2][2] + 0.75 * p[2][3]
	                    + 0.5 * p[3][0] - 1.25 * p[3][1] + p[3][2] - 0.25 * p[3][3];
	        a[3][3]  = 0.25 * p[0][0] - 0.75 * p[0][1] + 0.75 * p[0][2] - 0.25 * p[0][3]
	                    - 0.75 * p[1][0] + 2.25 * p[1][1] - 2.25 * p[1][2] + 0.75 * p[1][3]
	                    + 0.75 * p[2][0] - 2.25 * p[2][1] + 2.25 * p[2][2] - 0.75 * p[2][3]
	                    - 0.25 * p[3][0] + 0.75 * p[3][1] - 0.75 * p[3][2] + 0.25 * p[3][3];
	        /*for (double[] ai : a) {
				for (double aj : ai) {
					System.out.print(aj + " ");
				}
				System.out.println();
			}*/
	}
	public double interpolate(double x, double z) {// 0..1
		double res=0,result=0;
		for(int i=0;i<4;i++) {
			for(int j=0;j<4;j++) {
				res+= a[i][j]*Math.pow(z, j);
			}
			result += res*Math.pow(x, i);
			res=0;
		}
		return result;
	}
	public double altinterpolate(double[][] p, double x,double z) {
		double[] tmp = new double[4];
		for(int i = 0 ; i < 4 ; i++) {
			tmp[i] = WEMath.cubicInterpolate(p[i][0], p[i][1], p[i][2], p[i][3], z);
		}
		return WEMath.cubicInterpolate(tmp[0], tmp[1], tmp[2], tmp[3], x);
	}
}
