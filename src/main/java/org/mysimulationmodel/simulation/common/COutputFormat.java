package org.mysimulationmodel.simulation.common;

/**
 * Created by fatema on 10.04.2018.
 */
public class COutputFormat
{
    public double m_timestep;
    public String m_id;
    public double m_selfX;
    public double m_selfY;
    public double m_speed;
    public int m_scenarioid;
    public COutputFormat( int p_scenarioid, double p_timestep, String p_id, double p_selfX, double p_selfY, double speed )
    {
        m_timestep = p_timestep;
        m_id = p_id;
        m_selfX = p_selfX;
        m_selfY = p_selfY;
        m_speed = speed;
        m_scenarioid = p_scenarioid;
    }

    public COutputFormat( double p_timestep, String p_id, double p_selfX, double p_selfY )
    {
        m_timestep = p_timestep;
        m_id = p_id;
        m_selfX = p_selfX;
        m_selfY = p_selfY;
    }

}
