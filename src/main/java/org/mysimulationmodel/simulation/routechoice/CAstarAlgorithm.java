package org.mysimulationmodel.simulation.routechoice;

import org.mysimulationmodel.simulation.common.CVector;
import org.mysimulationmodel.simulation.common.CWall;
import org.mysimulationmodel.simulation.environment.CEnvironment;

import javax.vecmath.Vector2d;
import java.util.*;


/**
 * A* Routing algorithm
 * Created by fatema on 03.11.2017.
 * @todo have individual visibility map for individual user // ask sovon
 */
public final class CAstarAlgorithm
{
    private final Map<Vector2d, CNode> m_visibilitygraph;

    public CAstarAlgorithm( CEnvironment p_environment )
    {
        m_visibilitygraph = new CVisibilityGraph( p_environment ).getGraph();
        System.out.println( m_visibilitygraph );
    }

    /**
     * routing algorithm
     *
     * @param p_walledge list of wall edges
     * @param p_currentposition start/current position
     * @param p_targetposition end/target position
     * @return route
     */
    public final List<Vector2d> route( ArrayList<CWall> p_walledge, final Vector2d p_currentposition, final Vector2d p_targetposition )
    {

        final TreeSet<CRouteNode> l_openlist = new TreeSet<>( new CCompareRouteNode() );
        final ArrayList<Vector2d> l_closedlist = new ArrayList<>();
        final List<Vector2d> l_finalpath = new ArrayList<>();

        //add start node to the graph
        if ( !m_visibilitygraph.containsKey( p_currentposition ) ) m_visibilitygraph.put( p_currentposition, new CNode( p_currentposition, new ArrayList<>(), null ) );

        m_visibilitygraph.forEach( ( i, j ) ->
        {
            //calculate neighbors of the start node
            if ( ( i != p_currentposition ) && this.helpCheck( i, p_currentposition, p_walledge  ) )
            {
                m_visibilitygraph.get( p_currentposition ).getNeighbourList().add( i );
            }

            //add goal node to the graph
            if ( ( i != p_targetposition ) && this.helpCheck( i, p_targetposition, p_walledge ) )
                m_visibilitygraph.get( i ).getNeighbourList().add( p_targetposition );

        } );

        l_openlist.add( new CRouteNode( p_currentposition, null ) );

        while ( !l_openlist.isEmpty() )
        {

            final CRouteNode l_currentnode = l_openlist.pollFirst();

            //if the current node is the end node
            if ( l_currentnode.coordinate().equals( p_targetposition ) )
            {
                l_finalpath.add( p_targetposition );
                CRouteNode l_parent = l_currentnode.parent();

                while ( !l_parent.coordinate().equals( p_currentposition ) )
                {
                    l_finalpath.add( m_visibilitygraph.get( l_parent.coordinate() ).getCoordinate() );

                    l_parent = l_parent.parent();
                }
                Collections.reverse( l_finalpath );
                return l_finalpath;
            }

            //Find the successors to current node (add them to the open list)
            this.addsuccessors( m_visibilitygraph, l_currentnode, p_targetposition, l_closedlist, l_openlist );

            //Add the l_currentnode to the closed list (as to not open it again)
            l_closedlist.add( l_currentnode.coordinate() );

        }

        return Collections.<Vector2d>emptyList();
    }

    /**
     * Validated successors are added to open list
     * @param p_visibilitygraph visibility graph
     * @param p_curnode the current node to search for successors
     * @param p_target the goal node
     * @param p_closedlist the list of coordinate that already explored
     * @param p_openlist the set of CJumpPoint that will be explored
     */
    private void addsuccessors(final Map<Vector2d, CNode> p_visibilitygraph, final CRouteNode p_curnode, final Vector2d p_target,
                               final ArrayList<Vector2d> p_closedlist, final Set<CRouteNode> p_openlist )
    {
        p_visibilitygraph.get( p_curnode.coordinate() ).getNeighbourList().stream().filter( i ->  !p_closedlist.contains( i ) )
                .forEach( i -> {

                    final CRouteNode l_successor = new CRouteNode( i, p_curnode );
                    this.calculateScore( l_successor, p_target );
                    p_openlist.removeIf( s -> s.coordinate().equals( i ) && s.fscore() > l_successor.fscore() );

                    //checking that the jump point is already exists in open list or not, if yes then check their fscore to make decision
                    if ( p_openlist.stream().filter( s -> s.coordinate().equals( i ) ).noneMatch( s -> s.fscore() < l_successor.fscore() ) )
                        p_openlist.add( l_successor );

                } );

    }

    /**
     * Calculates the given node's score
     * @param p_successornode The node to calculate
     * @param p_target the target node
     */
    private void calculateScore( final CRouteNode p_successornode, final Vector2d p_target )
    {
        final double l_hscore = CVector.distance( p_target, p_successornode.coordinate() );

        p_successornode.sethscore( l_hscore );

        final double l_gscore = CVector.distance( p_successornode.parent().coordinate(), p_successornode.coordinate() );

        p_successornode.setgscore( p_successornode.parent().gscore() + l_gscore );

    }

    /**
     * check if the line segment between two points intersect with any wall edge and
     * the intersection point is not the start or end of point of the wall edge
     * @return true or false
     **/
    private Boolean helpCheck( Vector2d p_point1, Vector2d p_point2, ArrayList<CWall> m_walledge )
    {
        //int l_count = 0;
        for ( CWall l_walledge : m_walledge )
        {
            if ( CVector.segmentIntersectwithAdditionalCondition( p_point1, p_point2, l_walledge.getPoint1(),
                    l_walledge.getPoint2() ) )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * class to compare two graph nodes
     */
    private static final class CCompareRouteNode implements Comparator<CRouteNode>
    {
        @Override
        public final int compare( final CRouteNode p_node1, final CRouteNode p_node2 )
        {
            return ( p_node1.fscore() > p_node2.fscore() ) ? 1 : -1;
        }
    }



    /**
     * graph nodes with a static class
     * "static" means that the class can exists without the CAstarAlgorithm class object
     * "final" no inheritance can be create
     */
    private static final class CRouteNode
    {
        /**
         * node's g-score value
         */
        private double m_gscore;

        /**
         * node's h-score value
         */
        private double m_hscore;

        /**
         * parent node of current JumpPoint
         */
        private CRouteNode m_parent;
        /**
         * position
         */
        private final Vector2d m_coordinate;

        /**
         * ctor - with default values
         */
        private CRouteNode()
        {
            this( null, null );
        }

        /**
         * ctor
         *
         * @param p_coordinate postion value
         * @param p_parent parent of the current jump point
         */
        CRouteNode( final Vector2d p_coordinate, final CRouteNode p_parent )
        {
            m_coordinate = p_coordinate;
            m_parent = p_parent;
        }

        /**
         * getter for coordinate
         *
         * @return coordinate
         */
        final Vector2d coordinate()
        {
            return m_coordinate;
        }

        /**
         * getter for g-score
         *
         * @return g-score
         */
        final double gscore()
        {
            return m_gscore;
        }

        /**
         * getter for f_score
         *
         * @return f-score
         */
        final double fscore()
        {
            return m_hscore + m_gscore;
        }

        /**
         * getter for parent
         *
         * @return parent
         */
        final CRouteNode parent()
        {
            return m_parent;
        }

        /**
         * setter for g_score
         *
         * @return CJumpPoint
         */
        final void setgscore(final double p_gscore )
        {
            m_gscore = p_gscore;
        }

        /**
         * setter for h_score
         *
         * @return CJumpPoint
         */
        final void sethscore(final double p_hscore )
        {
            m_hscore = p_hscore;
        }

    }
}
