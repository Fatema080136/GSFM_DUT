package org.mysimulationmodel.simulation.routechoice;

import javax.vecmath.Vector2d;
import java.util.ArrayList;

/**
 * Created by fatema on 02.11.2017.
 * class for visibility graph`s node
 */
public class CNode
{
    private ArrayList<Vector2d> m_neighbour;
    private Vector2d m_coordinate;
    private String m_polyname;


    public CNode(final Vector2d p_coordinate, final ArrayList<Vector2d> p_neighbour, final String p_polyname)
    {
        m_coordinate = p_coordinate;
        m_neighbour = p_neighbour;
        m_polyname = p_polyname;
    }

    /**
     * returns the neighbour list
     * @return m_neighbour
     **/
    ArrayList<Vector2d> getNeighbourList()
    {
        return m_neighbour;
    }

    /**
     * returns coordinate
     * @return m_coordinate
     **/
    Vector2d getCoordinate()
    {
        return m_coordinate;
    }

    /**
     * returns the polygon name where the node is the part of that polygon
     * @return m_polyname
     **/
    public String getPolygonName()
    {
        return m_polyname;
    }

}
