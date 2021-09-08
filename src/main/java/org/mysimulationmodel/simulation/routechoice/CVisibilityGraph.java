package org.mysimulationmodel.simulation.routechoice;

import org.mysimulationmodel.simulation.environment.CEnvironment;
import javax.vecmath.Vector2d;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by fatema on 02.11.2017.
 * @todo make this class more organized and efficient
 */
public class CVisibilityGraph
{
    private final Map<Vector2d, CNode> m_visibilitynodes = new ConcurrentHashMap<>();

    public CVisibilityGraph( CEnvironment p_environment )
    {
        // initialize the visibility graph's nodes
        p_environment.getWallinfo().stream()
         .forEach(
                i -> {
                    if( i.getPoint1().getX() > 16 && i.getPoint1().getY() > 16 && i.getPoint1().getX() < 1184 && i.getPoint1().getY() < 684 && ! m_visibilitynodes.containsKey( i.getPoint1() ) )
                        m_visibilitynodes.put( i.getPoint1(), new CNode( i.getPoint1(), new ArrayList<>(), i.getPolygonName() ) );//20

                    if( i.getPoint2().getX() > 16 && i.getPoint2().getY() > 16 && i.getPoint2().getX() < 1184 && i.getPoint2().getY() < 684 && ! m_visibilitynodes.containsKey( i.getPoint2() ) )
                        m_visibilitynodes.put( i.getPoint2(), new CNode( i.getPoint2(), new ArrayList<>(), i.getPolygonName() ) );

                }
        );

        // initialize the visibility graph's nodes edges
        m_visibilitynodes.forEach( ( i, j ) -> m_visibilitynodes.forEach( (k, l ) ->
            {
                if ( ( i != k ) && p_environment.check( i, j, k, l ) ) { m_visibilitynodes.get( i ).getNeighbourList().add( k );}
            } ) );

    }

    /**
     * getter for m_visibilitynodes
     * @return m_visibilitynodes
     */
    public Map<Vector2d, CNode> getGraph()
    {
        return m_visibilitynodes;
    }

}
