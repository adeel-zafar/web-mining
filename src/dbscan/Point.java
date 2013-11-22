package dbscan;

import java.util.ArrayList;

public class Point
{
	
	public ArrayList<Double> point;
        private int x;

        private int y;

        Point()
        {
        	this.point=new ArrayList<Double>();
        	
        }
        Point(int a, int b)
         {
                x=a;
                y=b;
         }
        
        public int getX ()
         {

                return x;

         }


        public int getY ()
        {

                return y;

        }

        

}
