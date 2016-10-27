import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
//import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;


//dependenc Stdlib
//execute NetworkVis.java <txt file> <cycles>
public class NetworkVis{
	public static void main(String args[]) throws java.io.FileNotFoundException{
		if (args == null){
			System.out.println("Input text file in command line");
			System.exit(0);
		}
		Scanner scan = null;
		try{
		File file = new File(args[0]);
		scan = new Scanner(file);
		}
		catch (java.io.FileNotFoundException e){
			System.out.println("File not found");
		}
		int iteration = 500;
		if (args.length == 2)
			iteration = Integer.parseInt(args[1]);
		System.out.println("doing:" + iteration);
		double scaleX = 100;
		double scaleY = 100;
		StdDraw.setPenRadius(0.05);
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.setCanvasSize(600, 600);
		StdDraw.setXscale(-scaleX, scaleX);
		StdDraw.setYscale(-scaleY, scaleY);
		int a, b, size = Integer.parseInt(scan.next());
		System.out.println("size is:" + size);
		boolean[][] connect = new boolean[size][size];
		ArrayList<Node> nodeArray = new ArrayList<Node>();		
		for (int i = 0; i < size; i++){
			nodeArray.add(new Node());
			for (int j = 0; j < size; j++)
				connect[i][j] = false; //arrays already initialize to false
		}
		
		
		while(scan.hasNext()){
			a = Integer.parseInt(scan.next());
			b = Integer.parseInt(scan.next());
			connect[a][b] = true;
			connect[b][a] = true;
		}

		//animation loop
		int x = 0;
		while (x++ < iteration){
			StdDraw.clear();
			for (int i = 0; i < size; i++){
				if ((nodeArray.get(i).x > scaleX) || (nodeArray.get(i).x < -scaleX)){
					scaleX = Math.abs((nodeArray.get(i).x * 1.1));
					StdDraw.setXscale(-scaleX, scaleX);
					System.out.println("resizex =");
				}
				if ((nodeArray.get(i).y > scaleY) || (nodeArray.get(i).y < -scaleY)){
					scaleY = (nodeArray.get(i).y * 1.1);
					StdDraw.setYscale(-scaleY, scaleY);
					System.out.println("resizey =");
				}
				drawNode(nodeArray.get(i), scaleX, scaleY);
				for (int j = i+1; j < size; j++){
					if (connect[i][j]){
						drawEdge(nodeArray.get(i), nodeArray.get(j));
					}
				}
			}
			StdDraw.show(0);

			for (int i = 0; i < size; i++){	//for every node
				for (int j = i+1; j < size; j++){
					if (connect[i][j]){
						calculateEdge(nodeArray.get(i), nodeArray.get(j));	
					}
					calculateNodes(nodeArray.get(i), nodeArray.get(j));
				}
				calculateSingleNode(nodeArray.get(i));
			}
		}
	}
	public static void drawNode(Node i, double x, double y){
		Circle c = new Circle( (x + y) / 100, i.x, i.y);
		c.draw();
	}
	public static void drawEdge(Node i, Node j){
		Line l = new Line(i.x, i.y, j.x, j.y);
		l.draw();
	}


	public static void calculateSingleNode(Node i){
		i.x += i.dx;
		i.y += i.dy;
		i.dx = i.dy = 0;
	}
	public static void calculateEdge(Node i, Node j){
		double fatt = .0004 * (((j.x - i.x)*(j.x - i.x)) + ((j.y - i.y)*(j.y - i.y)));
		double theta = Math.atan2((j.y - i.y),(j.x - i.x));
		i.dx += fatt * Math.cos(theta);
		i.dy += fatt * Math.sin(theta);
		j.dx -= fatt * Math.cos(theta);
		j.dy -= fatt * Math.sin(theta);
	}

	public static void calculateNodes(Node i, Node j){
		double frep = .5 / Math.sqrt(((j.x - i.x)*(j.x - i.x)) + ((j.y - i.y)*(j.y - i.y)));
		double theta = Math.atan2((j.y - i.y) , (j.x - i.x));
		i.dx -= frep * Math.cos(theta);
		i.dy -= frep * Math.sin(theta);
		j.dx += frep * Math.cos(theta);
		j.dy += frep * Math.sin(theta);
	}
}

class Circle{
	public double r, x, y;
	Circle(){
		r = 1; x = y = 0;
	}
	Circle(double rr, double xx, double yy){
		r = rr; x = xx; y = yy;
	}
	public void draw(){
		StdDraw.filledCircle(x, y, r);
	}
}

class Line{
	Random r = new Random();
	public double sx, sy, ex, ey;
	Line(){
		sx = sy = ex = ey = 0;
	}
	Line(double ssx, double ssy, double eex, double eey){
		sx = ssx; sy = ssy; ex = eex; ey = eey;
	}
	public void draw(){
		StdDraw.line(sx, sy, ex, ey);
	}
}

class Node{
	Random r = new Random();
	public double x, y, dx, dy;
	Node(){
		x = r.nextDouble() * 100 - 50;
		y = r.nextDouble() * 100 - 50;
		dx = dy = 0;
	}
	Node(double xx, double yy){
		x = xx; y = yy;
		dx = dy = 0;
	}
}


