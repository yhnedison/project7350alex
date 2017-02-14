package project;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.WindowConstants;
public class mainFrame 
{
	final static int N=640;
	final static int WIDTH=700;
	public static void main(String[] args)
	{	
		Point points[]=new Point[N];
		Point points1[]=new Point[N];
		Point points2[]=new Point[N];
		Point points3[]=new Point[N];
		for(int i=0;i<N;i++)
		{
			int x=(int)(Math.random()*WIDTH);
			int y=(int)(Math.random()*WIDTH);
			Point p=new Point(i,x,y);
			points[i]=p;
		}
//		for(int i=0;i<N;i++)
//		{
//			int x,y;
//			while(true)
//			{
//				x=(int)(Math.random()*WIDTH);
//				y=(int)(Math.random()*WIDTH);
//				if((Math.pow(WIDTH/2-x, 2)+Math.pow(WIDTH/2-y, 2)) < Math.pow(WIDTH/2, 2))
//				{
//					Point p= new Point(i,x,y);
//					points[i]=p;
//					break;
//				}
//			}
//		}
		for(int i=0;i<N;i++)
		{
			Point p=new Point(i,points[i].get_x(),points[i].get_y());
			points1[i]=p;
		}
		for(int i=0;i<N;i++)
		{
			Point p=new Point(i,points[i].get_x(),points[i].get_y());
			points2[i]=p;
		}
		for(int i=0;i<N;i++)
		{
			Point p=new Point(i,points[i].get_x(),points[i].get_y());
			points3[i]=p;
		}
		JFrame frame =new JFrame("draw");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, WIDTH);
		frame.setLayout(null);
		
		JFrame frame1 =new JFrame("draw");
		frame1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame1.setSize(WIDTH, WIDTH);
		frame1.setLayout(null);
		
		JFrame frame2 =new JFrame("draw");
		frame2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame2.setSize(WIDTH, WIDTH);
		frame2.setLayout(null);
		
		JFrame frame3 =new JFrame("draw");
		frame3.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame3.setSize(WIDTH, WIDTH);
		frame3.setLayout(null);
		
		DrawPanel drawpanel=new DrawPanel(points,0);
		DrawPanel drawpanel1=new DrawPanel(points1,1);
		DrawPanel drawpanel2=new DrawPanel(points2,2);
		DrawPanel drawpanel3=new DrawPanel(points3,3);
		
		
		drawpanel.setSize(WIDTH,WIDTH);
		drawpanel.setBackground(java.awt.Color.white);
		
		drawpanel1.setSize(WIDTH,WIDTH);		
		drawpanel1.setBackground(java.awt.Color.white);
		
		drawpanel2.setSize(WIDTH,WIDTH);
		drawpanel2.setBackground(java.awt.Color.white);
		
		drawpanel3.setSize(WIDTH,WIDTH);
		drawpanel3.setBackground(java.awt.Color.white);
		
		frame.setContentPane(drawpanel);
		frame.setVisible(true);
		
		frame1.setContentPane(drawpanel1);
		frame1.setVisible(true);
		
		frame2.setContentPane(drawpanel2);
		frame2.setVisible(true);
		
		frame3.setContentPane(drawpanel3);
		frame3.setVisible(true);
		
		try{
			PrintWriter writer=new PrintWriter("degree5.txt","UTF-8");
			for(int i=0;i<N;i++)
			{
				writer.println(points[i].getDegree());
			}
			writer.close();
		}catch(IOException e){
			
		}
		try{
			PrintWriter writer1=new PrintWriter("copy5.txt","UTF-8");
			for(int i=0;i<N;i++)
			{
				writer1.println(drawpanel1.copy[i].getDegree());
			}
			writer1.close();
		}catch(IOException e){
			
		}
	}

}