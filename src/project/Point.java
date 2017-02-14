package project;

import java.util.LinkedList;
//import java.util.List;

public class Point {
	int id; //identify node
	int x,y; //coordinates
	int degree;
	int color;
	LinkedList<Point> adj=new LinkedList<Point>();
	
	public Point(int i,int X,int Y)
	{
		x=X;
		y=Y;
		degree=0;
		id=i;
		color=0;
	}
	
	//add element to adj
	public void add_Adj(Point p)
	{
		adj.add(p);
	}
	
	public int get_x()
	{
		return x;
	}
	public int get_y()
	{
		return y;
	}
	public int get_Adj_len()
	{
//		System.out.printf("len:%d,",adj.size());
		return adj.size();
	}
	public Point get_Adj_element(int index)
	{
		return adj.get(index);
	}
	public LinkedList<Point> get_Adj()
	{
		return adj;
	}
	public int getDegree()
	{
		return degree;
	}
	public int getColor()
	{
		return color;
	}
	
	public void subDegree()
	{
		degree=degree-1;
	}
	
	public void setDegree()
	{
		degree=degree+1;
	}
	public void setColor(int i)
	{
		color=i;
	}
}
