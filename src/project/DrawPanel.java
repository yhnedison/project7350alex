package project;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
//import java.util.List;

import javax.swing.JPanel;
public class DrawPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int N=640;
	final int DEGREE=20;
	final int WIDTH=700;
	Point points[];
	Point copy[];
	int color_n=0;
	int index;
	LinkedList<LinkedList<Point>> bucket;
	
	DrawPanel(Point[] P,int identify)
	{
		super();
		index=identify;
		points= new Point[N];
		build(P);
//		ran();
		ini_Adj();
//		ranCircle();
//		ini_C_Adj();
		iniBucket();
		display();
		LinkedList<Point> order=smallestLastOrder();
		System.out.printf("%d \n", order.size());
		color_n=coloring(order);
		clique();
		info();
	}
	
	public void build(Point[] P)
	{
		for(int i=0;i<P.length;i++)
		{
			points[i]=P[i];
		}
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2=(Graphics2D)g;
		drawPoint(g2);
		drawCircle(g2);
		if(index==1)
		{
			connectPoints(g2);
		}
		if(index==0)
		{
			smeat(g2);
		}
		else
		{
			backbone(g2);
		}
	}
	
	//draw points
	public void drawPoint(Graphics2D g)
	{
		for(int i=0;i<N;i++)
		{
			int x=points[i].get_x();
			int y=points[i].get_y();
			g.drawLine(x, y, x, y);
		}
	}
	
	//draw disk
	public void drawCircle(Graphics2D g)
	{
		g.drawOval(0, 0, WIDTH, WIDTH);
	}
	
	//draw maximum and minimum degree vertex
	public void smeat(Graphics2D g)
	{
		int max=maxIndex();
		int min=minIndex();
		int maxlen=points[max].get_Adj_len();
		int xmax=points[max].get_x();
		int ymax=points[max].get_y();
		for(int i=0;i<maxlen;i++)
		{
			Point p=points[max].get_Adj_element(i);
			int x=p.get_x();
			int y=p.get_y();
			g.drawLine(xmax,ymax,x,y);
		}
		int minlen=points[min].get_Adj_len();
		int xmin=points[min].get_x();
		int ymin=points[min].get_y();
		for(int i=0;i<minlen;i++)
		{
			Point p=points[min].get_Adj_element(i);
			int x=p.get_x();
			int y=p.get_y();
			g.drawLine(xmin,ymin,x,y);
		}
	}
	
	//connect points
	public void connectPoints(Graphics2D g)
	{
		for(int i=0;i<N;i++)
		{
			int len=points[i].get_Adj_len();
			int x1=points[i].get_x();
			int y1=points[i].get_y();
			for(int j=0;j<len;j++)
			{
				Point p=points[i].get_Adj_element(j);
				int x2=p.get_x();
				int y2=p.get_y();
				g.drawLine(x1, y1, x2, y2);
			}
		}
	}
	
	//draw backbone
	public void drawBackbone(Graphics2D g, LinkedList<Point> graph)
	{
		int n=graph.size();
		for(int i=0;i<n;i++)
		{
			Point p=graph.get(i);
			int x1=p.get_x();
			int y1=p.get_y();
			LinkedList<Point> adj=p.get_Adj();
			for(int j=0;j<adj.size();j++)
			{
				Point adj_p=adj.get(j);
				if(adj_p.getColor()==2)
				{
					int x2=adj_p.get_x();
					int y2=adj_p.get_y();
					g.drawLine(x1, y1, x2, y2);
				}
			}
		}
	}
	
	//create random points
	public void ran()
	{
		for(int i=0;i<N;i++)
		{
			int x=(int)(Math.random()*WIDTH);
			int y=(int)(Math.random()*WIDTH);
			Point p=new Point(i,x,y);
			points[i]=p;
		}
	}
	
	//create random points in circle
	void ranCircle()
	{
		for(int i=0;i<N;i++)
		{
			int x,y;
			while(true)
			{
				x=(int)(Math.random()*WIDTH);
				y=(int)(Math.random()*WIDTH);
				if((Math.pow(WIDTH/2-x, 2)+Math.pow(WIDTH/2-y, 2)) < Math.pow(WIDTH/2, 2))
				{
					Point p= new Point(i,x,y);
					points[i]=p;
					break;
				}
			}
		}
	}
	
	//copy point
	public Point[] copy_points()
	{
		Point copy_p[]=new Point[N];
		for(int i=0;i<N;i++)
		{
			Point p=new Point(points[i].id,points[i].get_x(),points[i].get_y());
			copy_p[i]=p;
		}
		double r2=(DEGREE+1)*(Math.pow(WIDTH,2))/N/(Math.PI);
		for(int i=0;i<N;i++)
		{
			for(int j=i+1;j<N;j++)
			{
					int x1=copy_p[i].get_x();
					int y1=copy_p[i].get_y();
					int x2=copy_p[j].get_x();
					int y2=copy_p[j].get_y();
					if((Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2))<=r2)
					{
						copy_p[i].add_Adj(copy_p[j]);
						copy_p[i].setDegree();
						copy_p[j].add_Adj(copy_p[i]);
						copy_p[j].setDegree();
					}
			}
		}
		return copy_p;
	}
	
	//copy point for circle
	public Point[] copy_c_points()
	{
		Point copy[]=new Point[N];
		for(int i=0;i<N;i++)
		{
			Point p=new Point(points[i].id,points[i].get_x(),points[i].get_y());
			copy[i]=p;
		}
		double r2=(DEGREE+1)*Math.pow(WIDTH/2, 2)/N;
		for(int i=0;i<N;i++)
		{
			for(int j=i+1;j<N;j++)
			{
					int x1=copy[i].get_x();
					int y1=copy[i].get_y();
					int x2=copy[j].get_x();
					int y2=copy[j].get_y();
					if((Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2))<=r2)
					{
						copy[i].add_Adj(copy[j]);
						copy[i].setDegree();
						copy[j].add_Adj(copy[i]);
						copy[j].setDegree();
					}
			}
		}
		return copy;
		
	}
	
	//initial adjacent list
	public void ini_Adj()
	{
		double r2=(DEGREE+1)*(Math.pow(WIDTH,2))/N/(Math.PI);
		System.out.print(Math.sqrt(r2)/WIDTH);
		System.out.print("\n");
		for(int i=0;i<N;i++)
		{
			for(int j=i+1;j<N;j++)
			{
					int x1=points[i].get_x();
					int y1=points[i].get_y();
					int x2=points[j].get_x();
					int y2=points[j].get_y();
					if((Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2))<=r2)
					{
						points[i].add_Adj(points[j]);
						points[i].setDegree();
						points[j].add_Adj(points[i]);
						points[j].setDegree();
					}
			}
		}
	}
	
	//initial adjacent list
	public void ini_C_Adj()
	{
		double r2=(DEGREE+1)*Math.pow(WIDTH/2, 2)/N;
		System.out.print(Math.sqrt(r2)/WIDTH);
		System.out.print("\n");
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				if(i!=j)
				{
					int x1=points[i].get_x();
					int y1=points[i].get_y();
					int x2=points[j].get_x();
					int y2=points[j].get_y();
					if((Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2))<=r2)
					{
						points[i].add_Adj(points[j]);
						points[i].setDegree();
					}
				}
			}
		}
	}	
	
	//find the maximun degree
	public int maxDegree()
	{
		int max=points[0].getDegree();
		for(int i=1;i<N;i++)
		{
			int currentDegree=points[i].getDegree();
			if(currentDegree>max)
			{
				max=currentDegree;
			}
		}
		System.out.printf("max:%d \n",max);
		return max;
	}
	
	public int maxIndex()
	{
		int max=points[0].getDegree();
		int max_index=0;
		for(int i=1;i<N;i++)
		{
			int currentDegree=points[i].getDegree();
			if(currentDegree>max)
			{
				max=currentDegree;
				max_index=i;
			}
		}
		return max_index;
	}
	public int minIndex()
	{
		int min=points[0].getDegree();
		int min_index=0;
		for(int i=1;i<N;i++)
		{
			int curDegree=points[i].getDegree();
			if(curDegree<min)
			{
				min=curDegree;
				min_index=i;
			}
		}
		return min_index;
	}
	
	public int minDegree()
	{
		int min=points[0].getDegree();
//		int min_index=0;
		for(int i=1;i<N;i++)
		{
			int curDegree=points[i].getDegree();
			if(curDegree<min)
			{
				min=curDegree;
//				min_index=i;
			}
		}
		System.out.printf("min:%d \n",min);
		return min;
	}
	
	public void iniBucket()
	{
		//List<Point> bucket[];
		int max=maxDegree();
		minDegree();
		bucket=new LinkedList<LinkedList<Point>>();
		//initial list
		for(int i=0;i<=max;i++)
		{
			LinkedList<Point> buck=new LinkedList<Point>();
			bucket.add(buck);
		}
		//copy points
		copy=copy_points();
//		copy=copy_c_points();
		//distribute point
		for(int i=0;i<N;i++)
		{
			int degree=copy[i].getDegree();
			bucket.get(degree).add(copy[i]);
		}
		int size=bucket.size();
		System.out.printf("bucket:%d \n", size);
		for(int i=0;i<size;i++)
		{
			System.out.printf("bucket %d: %d \n",i,bucket.get(i).size());
		}
	}
	
	public LinkedList<Point> smallestLastOrder()
	{
		LinkedList<Point> order=new LinkedList<Point>();
		
		int bucket_size=bucket.size();
		boolean t=true;
		while(t)
		{
			t=false;
			//each iteration from the smallest bucket
			for(int i=0;i<bucket_size;i++)
			{
				LinkedList<Point> buck=bucket.get(i);
				if(buck.size()!=0)
				{
					//add to order
					Point temp=buck.getLast();
					order.add(temp);
					
					//deal adjacent point 
					LinkedList<Point> adj=temp.get_Adj();
					//remove form bucket
					buck.removeLast();
					
					int len=adj.size();
					for(int j=0;j<len;j++)
					{
						Point adj_point=adj.get(j);
						//remove current point from its adjacent point's adjacent list
						int d = adj_point.getDegree();
						boolean r= adj_point.get_Adj().remove(temp);
//						System.out.printf("bucket %d:%d,",i,d);
//						System.out.print(r);
//						System.out.print(",");
						//switch to lower bucket
						boolean r1=bucket.get(d).remove(adj_point);
//						System.out.print(r1);
//						System.out.print("\n");
						bucket.get(d-1).add(adj_point);
						//subtract degree
						adj_point.subDegree();
					}
					
					t=true;
					break;
				}
			}
		}
		
		return order;
	}
	
	//coloring
	public int coloring(LinkedList<Point> order)
	{
		//color the first point
		int color_count=1;
		points[order.getLast().id].setColor(1);
		order.removeLast();
		
		while(order.size()!=0)
		{
			Point last=points[order.getLast().id];
			
			//initial array of color
			boolean current_color[]=new boolean[color_count];
			for(int i=0;i<color_count;i++)
			{
				current_color[i]=true;
			}
			
			//check the adjacent points and set color array
			LinkedList<Point> adj=last.get_Adj();
			for(int i=0;i<adj.size();i++)
			{
				int c=adj.get(i).getColor();
				if(c!=0)
				{
					current_color[c-1]=false;
				}
			}
			
			//check if current color available  
			boolean t=true;
			for(int i=0;i<color_count;i++)
			{
				if(current_color[i])
				{
					t=false;
					last.setColor(i+1);
					break;
				}
			}
			//if not, add a new color
			if(t)
			{
				color_count++;
				last.setColor(color_count);
			}
			
			order.removeLast();
			
		}
		
		System.out.printf("color:%d \n", color_count);
		return color_count;
	}
	
	public void backbone(Graphics2D g)
	{
		//find first four largest color set
		int color_set[]=new int[color_n];
		for(int i=0;i<color_n;i++)
		{
			color_set[i]=0;
		}
		for(int i=0;i<N;i++)
		{
			color_set[points[i].getColor()-1]++;
		}
		for(int i=0;i<color_n;i++)
		{
			System.out.printf("color%d: %d \n",i+1,color_set[i]);
		}
		int order[]={0,0,0,0};
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<color_n;j++)
			{
				if(color_set[j]>color_set[order[i]])
				{
					order[i]=j;
				}
			}
			System.out.printf("color %d: %d,", order[i],color_set[order[i]]);
			color_set[order[i]]=0;
		}
		for(int i=0;i<4;i++)
		{
			order[i]++;
		}
		System.out.print("\n");
				
		//find maximum backbone
		int len[]=new int[6];
		LinkedList<LinkedList<Point>> graphs=new LinkedList<LinkedList<Point>>();
		LinkedList<Point> graph1=graph(order[0],order[1]);
		graphs.add(graph1);
		LinkedList<Point> graph2=graph(order[0],order[2]);
		graphs.add(graph2);
		LinkedList<Point> graph3=graph(order[0],order[3]);
		graphs.add(graph3);
		LinkedList<Point> graph4=graph(order[1],order[2]);
		graphs.add(graph4);
		LinkedList<Point> graph5=graph(order[1],order[3]);
		graphs.add(graph5);
		LinkedList<Point> graph6=graph(order[2],order[3]);
		graphs.add(graph6);
		
		System.out.print("backbone:");
		for(int i=0;i<6;i++)
		{
			len[i]=bfs(graphs.get(i));
			System.out.print(len[i]);
			System.out.print(" ");
		}
		System.out.print("\n");
		//find first two
		int len_order[]={0,0};
		for(int i=0;i<2;i++)
		{
			for(int j=0;j<6;j++)
			{
				if(len[j]>len[len_order[i]])
				{
					len_order[i]=j;
				}
			}
			len[len_order[i]]=0;
		}
		
		//draw
		if(index==2)
			drawBackbone(g,graphs.get(len_order[0]));
		else
			drawBackbone(g,graphs.get(len_order[1]));
		
		
	}
	
	//create graph
	public LinkedList<Point> graph(int color1,int color2)
	{
		LinkedList<Point> bipartite=new LinkedList<Point>();
		//find all vertices
		for(int i=0;i<N;i++)
		{
			int c=points[i].getColor();
			if( c==color1 || c==color2 )
			{
				Point p=new Point(points[i].id,points[i].get_x(),points[i].get_y());
				bipartite.add(p);
			}
		}
		//find edges
		for(int i=0; i<bipartite.size();i++)
		{
			Point p=bipartite.get(i);
			LinkedList<Point> adj=points[p.id].get_Adj();
			for(int j=0;j<adj.size();j++)
			{
				int c=adj.get(j).getColor();
				if(c==color1 || c==color2)
				{
					int id =adj.get(j).id;
					for(int k=0;k<bipartite.size();k++)
					{
						if(bipartite.get(k).id==id)
						{
							p.add_Adj(bipartite.get(k));
							break;
						}
					}
				}
			}
		}
		
		int len=0;
		for(int i=0;i<bipartite.size();i++)
		{
			len=len+bipartite.get(i).get_Adj_len();
		}
		System.out.printf("graph: v %d, e %d, degree %d \n", bipartite.size(),len,len/bipartite.size());
		return bipartite;
	}
	
	//bread first search
	public int bfs(LinkedList<Point> graph)
	{
		int len=0;
		LinkedList<Point> queue=new LinkedList<Point>();
		queue.add(graph.get(0));
		while(queue.size()!=0)
		{
			Point p=queue.getFirst();
			queue.removeFirst();
			for(int i=0; i<p.get_Adj_len();i++)
			{
				Point v=p.get_Adj_element(i);
				if(v.getColor()==0)
				{
					v.setColor(1);
					queue.add(v);
				}
			}
			p.setColor(2);
			len=len+1;
		}
		
		return len;
	}
	
	public void clique()
	{
		LinkedList<Integer> clique=new LinkedList<Integer>();
		for(int i=0;i<N;i++)
		{
			boolean t=true;
			LinkedList<Point> adj=points[i].get_Adj();
			int colors[]=new int[color_n];
			for(int j=0;j<color_n;j++)
			{
				colors[j]=0;
			}
			for(int j=0;j<adj.size();j++)
			{
				colors[adj.get(j).color-1]++;
			}
			for(int j=0;j<color_n;j++)
			{
				if(colors[j]>1)
				{
					t=false;
					break;
				}
			}
			if(t)
			{
				clique.add(points[i].degree+1);
			}
		}
		int max=0;
		for(int i=0;i<clique.size();i++)
		{
			if(clique.get(i)>max)
			{
				max=clique.get(i);
			}
		}
		System.out.printf("clique: %d \n", max);
	}
	
	//print true 
	public void display()
	{
		int len=0;
		for(int i=0;i<N;i++)
		{
			len=len+points[i].get_Adj_len();
		}
		System.out.printf("total edges:%d \n",len);
		System.out.printf("average degree:%d \n",len/N);
	}
	public void info()
	{
		int max=0;
		for(int i=0;i<N;i++)
		{
			if(copy[i].degree>max)
			{
				max=copy[i].getDegree();
			}
		}
		System.out.printf("deleted degree:%d \n",max);
	}
}
