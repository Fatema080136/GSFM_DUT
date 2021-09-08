package org.mysimulationmodel.simulation.common;

import javax.vecmath.Vector2d;

/**
 * static element's class
 * Created by Fatema on 10/21/2016.
 */
public class CStatic
{
    private final int m_X1;
    private final int m_Y1;
    private final int m_width;
    private final int m_height;
    private final String m_label;

    public CStatic( final int p_x1, final int p_y1, final int p_width, final int p_height, final String p_label )
    {
        m_X1 = p_x1;
        m_Y1 = p_y1;
        m_width = p_width;
        m_height = p_height;
        m_label = p_label;
    }

    /**
     * returns static object's position's X
     * @return X
     **/
    public final int getX1()
    {
        return m_X1;
    }

    /**
     * returns static object's position's Y
     * @return Y
     **/
    public final int getY1()
    {
        return m_Y1;
    }

    /**
     * returns static object's wall1 among 4 sorrounded walls
     * @return wall1
     **/
    public final CWall getwall1()
    {
        return new CWall( new Vector2d( m_X1, m_Y1 ), new Vector2d( m_X1 + m_width, m_Y1 ), m_label ) ;
    }

    /**
     * returns static object's wall2 among 4 sorrounded walls
     * @return wall2
     **/
    public final CWall getwall2()
    {
        return new CWall( new Vector2d( m_X1, m_Y1 ), new Vector2d( ( m_X1 ), ( m_Y1 + m_height ) ), m_label ) ;
    }

    /**
     * returns static object's wall3 among 4 sorrounded walls
     * @return wall3
     **/
    public final CWall getwall4()
    {
        return new CWall(  new Vector2d( m_X1 + m_width, m_Y1 ), new Vector2d( ( m_X1 + m_width ), ( m_Y1 + m_height ) ), m_label ) ;
    }

    /**
     * returns static object's wall4 among 4 sorrounded walls
     * @return wall4
     **/
    public final CWall getwall3()
    {
        return new CWall( new Vector2d( ( m_X1 ), ( m_Y1 + m_height ) ), new Vector2d( ( m_X1 + m_width ), ( m_Y1 + m_height ) ), m_label ) ;
    }

    /**
     * returns static object's width
     * @return width
     **/
    public final int getWidth() {
        return m_width;
    }

    /**
     * returns static object's height
     * @return height
     **/
    public final int getHeight() {
        return m_height;
    }


    public boolean contains( double p_goalx, double p_goaly )
    {
        return ( this.getX1() <= p_goalx && ( this.m_X1 + this.m_width ) >= p_goalx
                && this.m_Y1 <= p_goaly && ( this.getY1() + this.m_height ) >= p_goaly );
    }
}
