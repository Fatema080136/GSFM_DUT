package org.mysimulationmodel.simulation.routechoice;

import javax.vecmath.Vector2d;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MyHausdorfDistanceImpl
{
    /**
     * This method will be used to calculate the Hausdorff distance
     *
     * @param routesim this is one of the routes to compare
     * @param routereal this is one of the routes to compare
     * @return returns true if the value of the maximum distance is not exceeded, false otherwise
     */
    public double distanceHausdorff(HashMap<Double, Position> routesim,
                                    HashMap<Double, Position> routereal)
    {   System.out.println( "what is the problem "+distanceCalc(routesim, routereal) );
        return distanceCalc(routesim, routereal);
    }

    /*
     * This method  is a helper to calculate the distance
     */
    private double distanceCalc(HashMap<Double, Position> a, HashMap<Double, Position> b)
    {
        final double[] maxDistAB = {0};

        System.out.println(a.size() + " " + b.size());
        a.forEach((i, j) ->
        {
            final double[] minB = {1000000};
            b.forEach((k, l) ->
            {
                if (a.containsKey(k)) {
                    double dx = (j.getX() - l.getX());
                    double dy = (j.getY() - l.getY());
                    double tmpDist = new Vector2d(dx, dy).length();
                    if (tmpDist < minB[0]) {
                        minB[0] = tmpDist;
                    }
                }
            });
            if (minB[0] > maxDistAB[0])
                maxDistAB[0] = minB[0];
        });

        ArrayList check = new ArrayList<Double>();
        a.forEach((i, j) ->
        {
            final double[] minB = {1000000};

            b.forEach((k, l) ->
            {
                if (a.containsKey(k)) {
                    double dx = (j.getX() - l.getX());
                    double dy = (j.getY() - l.getY());
                    double tmpDist = new Vector2d(dx, dy).length();
                    if (tmpDist < minB[0]) {
                        minB[0] = tmpDist;
                    }
                }
            });
            check.add(minB[0]);
        });

        return check.stream().mapToDouble(j -> (double) j).max().getAsDouble();


    }


    // The classe Posicion is a simple POJO, for example
    public static class Position {
        private double x;
        private double y;

        Position(){
            this.x = 0;
            this.y = 0;
        }

        public Position(double x, double y){
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }


    }
}
