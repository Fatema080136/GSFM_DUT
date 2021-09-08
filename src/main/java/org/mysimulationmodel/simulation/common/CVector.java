package org.mysimulationmodel.simulation.common;

import javax.vecmath.Vector2d;
import java.text.DecimalFormat;

/**
 * all vector calculations
 * Created by Fatema on 10/22/2016.
 */
public final class CVector
{

    /**
     * calculate direction between two points
     * @return direction
     **/
    public static Vector2d direction( final Vector2d p_v1, final Vector2d p_v2 )
    {
        return normalize( subtract( p_v1, p_v2 ) );
    }

    /**
     * calculate manhattan distance between 2 points
     * @return distance
     **/
    public static double manhattanDistance( final Vector2d p_v1, final Vector2d p_v2 )
    {
        return (Math.abs(p_v2.getX()- p_v1.getX()) + Math.abs(p_v2.getY()- p_v1.getY()));
    }

    /**
     * to scale vector
     * @return scaled vector
     **/
    public static Vector2d scale( final double p_speed, final Vector2d p_v1 )
    {
        return new Vector2d( p_v1.getX() * p_speed, p_v1.getY() * p_speed );
    }

    /**
     * vector add operation of 2 given vectors
     * @return result of addition
     **/
    public static Vector2d add( final Vector2d p_v1, final Vector2d p_v2 )
    {
        return new Vector2d( p_v1.getX() + p_v2.getX(), p_v1.getY() + p_v2.getY() );
    }

    /**
     * vector add operation of 3 given vectors
     * @return result of addition
     **/
    public static Vector2d add(final Vector2d p_v1, final Vector2d p_v2, final Vector2d p_v3)
    {
        return new Vector2d( p_v1.getX() + p_v2.getX() + p_v3.getX(), p_v1.getY() + p_v2.getY() + p_v3.getY() );
    }

    /**
     * vector subtract operation of 2 given vectors
     * @return result of subtraction
     **/
    public static Vector2d subtract( final Vector2d p_v1, final Vector2d p_v2 )
    {
        return new Vector2d( p_v1.getX() - p_v2.getX(), p_v1.getY() - p_v2.getY() );
    }

    /**
     * calculate distance between 2 points
     * @return distance
     **/
    public static double distance( final Vector2d p_v1, final Vector2d p_v2 )
    {
        return subtract( p_v1, p_v2 ).length();
    }

    /**
     * calculate the center of any line
     * @return center point
     **/
    static Vector2d staticObjectCentre( final Vector2d p_v1, final Vector2d p_v2 )
    {
        return new Vector2d( ( p_v1.getX() + p_v2.getX() )/2.0 , ( p_v1.getY() + p_v2.getY() )/2.0 );
    }

    /**
     * calculate cos of any angle theta
     * @return cos value of any angle
     **/
    public static double costheta(final Vector2d p_v1, final Vector2d p_v2, final Vector2d p_v3)
    {
        return ( direction( p_v1, p_v2 ).dot( subtract( p_v3, p_v2 ) ) )/
                ( direction( p_v1, p_v2 ).length() * subtract( p_v3, p_v2 ).length() );

    }



    /**
     * calculate angle between 2 vectors
     * @return angle
     **/
    public static double angle(final Vector2d p_v1, final Vector2d p_v2)
    {
        return Math.toDegrees( Math.acos( p_v1.dot( p_v2 ) / ( p_v1.length() * p_v2.length() ) ) ) ;

    }

    static double cos(final Vector2d p_v1, final Vector2d p_v2)
    {
        return p_v1.dot( p_v2 ) / ( p_v1.length() * p_v2.length() ) ;

    }

    /**
     * perpendicular direction from a point to a line
     * @return direction vector
     **/
    public static Vector2d perpendicular_derection(final Vector2d p_position, final CWall l_wall)
    {
        final Vector2d l_wallPoint = l_wall.getPoint1();
        final Vector2d l_walldirection = CVector.normalize( CVector.subtract( l_wall.getPoint2(), l_wall.getPoint1() ) );
        final double l_check = CVector.subtract( p_position, l_wallPoint ).dot( l_walldirection );

        return CVector.add( l_wallPoint, CVector.scale( l_check, l_walldirection ) );
    }

    /**
     * truncate a vector's magnitude comparing with a given double value
     * @return truncated vector
     **/
    public static Vector2d truncate( final Vector2d p_vector, final double p_scalefactor )
    {
        double l_check;

        l_check = p_scalefactor / p_vector.length();
        l_check = l_check < 1.0 ? 1.0 : 1/l_check;

        return CVector.scale( l_check, p_vector );
    }

    /**
     * check if wall is under pedestrain point of view or not?
     * @return if falls in FOV then true else false
     **/
    public static boolean check( final Vector2d p_point, final Vector2d p_wallpoint1, final Vector2d p_wallpoint2 )
    {
        final double l_wall2Towall1 = CVector.subtract( p_wallpoint2, p_wallpoint1 ).length();
        final double l_pointTowall1 = CVector.subtract( p_point, p_wallpoint1 ).length();
        final double l_pointTowall2 = CVector.subtract( p_wallpoint2, p_point ).length();

        return ( l_wall2Towall1 == l_pointTowall1 + l_pointTowall2 );
    }

    /**
     * calculate normalized vector
     * @return normalized vector
     **/
    public static Vector2d normalize( final Vector2d p_vector )
    {

        Vector2d l_temp = new Vector2d( 0, 0 );

        if ( p_vector.length() != 0 )
        {
            l_temp.x = p_vector.x / p_vector.length();
            l_temp.y = p_vector.y / p_vector.length();
        }

        return l_temp;
    }

    /**
     * calculatee distance from a point to a line
     * @return distance vector
     **/
    public static Vector2d distanceToWall( final Vector2d p_position, final Vector2d p_wall1, final Vector2d p_wall2 )
    {
        if( CVector.subtract( p_position, p_wall1 ).dot( CVector.direction( p_wall2, p_wall1 ) ) <= 0 )
        {
            return CVector.subtract( p_wall1, p_position );
        }
        else if( CVector.subtract( p_position, p_wall1 ).dot( CVector.direction( p_wall2, p_wall1 ) ) > 0 && CVector.subtract( p_position,
                            p_wall1 ).dot( CVector.direction( p_wall2, p_wall1 ) ) <= CVector.subtract( p_wall2, p_wall1 ).length() )
        {
            return CVector.subtract( CVector.subtract( p_wall1, p_position ), CVector.scale( CVector.subtract( p_wall1, p_position )
                            .dot( CVector.direction( p_wall2, p_wall1 ) ), CVector.direction( p_wall2, p_wall1 ) ) );
        }
        else return CVector.subtract( p_wall2, p_position );

    }

    /*
     * check if two line segments intersect
     * code adapted from https://github.com/bit101/CodingMath/blob/master/episode33/main_interactive.js
     * @see https://www.youtube.com/watch?v=A86COO8KC58
     */
    public static Boolean segmentIntersect( Vector2d p_point1, Vector2d p_point2, Vector2d p_point3, Vector2d p_point4 )
    {
        double l_a1 = p_point2.y - p_point1.y;
        double l_b1 = p_point1.x - p_point2.x;
        double l_c1 = l_a1 * p_point1.x + l_b1 * p_point1.y;
        double l_a2 = p_point4.y - p_point3.y;
        double l_b2 = p_point3.x - p_point4.x;
        double l_c2 = l_a2 * p_point3.x + l_b2 * p_point3.y;
        double l_denominator = l_a1 * l_b2 - l_a2 * l_b1;

        if( l_denominator == 0 )
        {
            return false;
        }

        double l_intersectX = ( l_b2 * l_c1 - l_b1 * l_c2 ) / l_denominator;
        double l_intersectY = ( l_a1 * l_c2 - l_a2 * l_c1 ) / l_denominator;
        double l_rx0 = ( l_intersectX - p_point1.x ) / ( p_point2.x - p_point1.x );
        double l_ry0 = ( l_intersectY - p_point1.y ) / ( p_point2.y - p_point1.y );
        double l_rx1 = ( l_intersectX - p_point3.x ) / ( p_point4.x - p_point3.x );
        double l_ry1 = ( l_intersectY - p_point3.y ) / ( p_point4.y - p_point3.y );

        return ( ( l_rx0 >= 0 && l_rx0 <= 1 ) || ( l_ry0 >= 0 && l_ry0 <= 1 ) ) &&
                ( ( l_rx1 >= 0 && l_rx1 <= 1 ) || ( l_ry1 >= 0 && l_ry1 <= 1 ) );
    }

    public static Vector2d intersectionPoint( Vector2d p_point1, Vector2d p_point2, Vector2d p_point3, Vector2d p_point4 )
    {
        double l_a1 = p_point2.y - p_point1.y;
        double l_b1 = p_point1.x - p_point2.x;
        double l_c1 = l_a1 * p_point1.x + l_b1 * p_point1.y;
        double l_a2 = p_point4.y - p_point3.y;
        double l_b2 = p_point3.x - p_point4.x;
        double l_c2 = l_a2 * p_point3.x + l_b2 * p_point3.y;
        double l_denominator = l_a1 * l_b2 - l_a2 * l_b1;

        double l_intersectX = ( l_b2 * l_c1 - l_b1 * l_c2 ) / l_denominator;
        double l_intersectY = ( l_a1 * l_c2 - l_a2 * l_c1 ) / l_denominator;

        return new Vector2d( l_intersectX, l_intersectY );
    }



    /*
     * @todo need to redesign by considering intesection with multiple edges
     * check if two line segments intersect
     * code adapted from https://github.com/bit101/CodingMath/blob/master/episode33/main_interactive.js
     * @see https://www.youtube.com/watch?v=A86COO8KC58
     */
    public static Boolean segmentIntersectwithAdditionalCondition( Vector2d p_point1, Vector2d p_point2, Vector2d p_point3, Vector2d p_point4 )
    {
        double l_a1 = p_point2.y - p_point1.y;
        double l_b1 = p_point1.x - p_point2.x;
        double l_c1 = l_a1 * p_point1.x + l_b1 * p_point1.y;
        double l_a2 = p_point4.y - p_point3.y;
        double l_b2 = p_point3.x - p_point4.x;
        double l_c2 = l_a2 * p_point3.x + l_b2 * p_point3.y;
        double l_denominator = l_a1 * l_b2 - l_a2 * l_b1;

        if( l_denominator == 0 )
        {
            return false;
        }

        double l_intersectX = ( l_b2 * l_c1 - l_b1 * l_c2 ) / l_denominator;
        double l_intersectY = ( l_a1 * l_c2 - l_a2 * l_c1 ) / l_denominator;
        double l_rx0 = ( l_intersectX - p_point1.x ) / ( p_point2.x - p_point1.x );
        double l_ry0 = ( l_intersectY - p_point1.y ) / ( p_point2.y - p_point1.y );
        double l_rx1 = ( l_intersectX - p_point3.x ) / ( p_point4.x - p_point3.x );
        double l_ry1 = ( l_intersectY - p_point3.y ) / ( p_point4.y - p_point3.y );

        if ( ( ( l_rx0 >= 0 && l_rx0 <= 1 ) || ( l_ry0 >= 0 && l_ry0 <= 1 ) ) && ( ( l_rx1 >= 0 && l_rx1 <= 1 ) || ( l_ry1 >= 0 && l_ry1 <= 1 ) ) ) {
            if (((Math.round(l_intersectX) != p_point3.x) && (Math.round(l_intersectX) != p_point4.x) && (Math.round(l_intersectY) == p_point3.y) &&
                    (Math.round(l_intersectY) == p_point4.y)) || ((Math.round(l_intersectY) != p_point3.y) && (Math.round(l_intersectY) != p_point4.y)
                    && (Math.round(l_intersectX) == p_point3.x) && (Math.round(l_intersectX) == p_point4.x))) {
                return true;
            }
        }
        return false;
    }

    public static boolean sameSigns( double x, double y )
    {
        return (x * y) >= 0.0;
    }

    public static Vector2d diffentunitvector( final Vector2d p_directionvector )
    {
        return new Vector2d( ( p_directionvector.x/ p_directionvector.x), ( p_directionvector.x/ p_directionvector.x) );
    }



    /**
     * rotate a given vector by a given direction
     * @return new direction vector
     **/
    public static Vector2d directionrotation( final Vector2d p_currentdirection, final double p_angle )
    {
        return new Vector2d( p_currentdirection.x * Math.cos( p_angle ) - p_currentdirection.y * Math.sin( p_angle ),
                p_currentdirection.x * Math.sin( p_angle ) + p_currentdirection.y * Math.cos( p_angle ) );
    }

    public static Vector2d midgoal(Vector2d goalposition, Vector2d position)
    {
        if( Math.abs(goalposition.x - position.x) <= 3 || Math.abs(goalposition.y - position.y) <= 3)
        return null;
        else return new Vector2d(position.x -(position.x - goalposition.x)/2,
                position.y -(position.y - goalposition.y)/2);
    }
}
