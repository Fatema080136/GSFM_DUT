package org.mysimulationmodel.simulation.common;

public class CSampleOutput
{
    public double m_timestep;
    public String m_id;
    public double m_selfX;
    public double m_selfY;
    public double m_speed;

    public CSampleOutput( double p_timestep, String p_id, double p_selfX, double p_selfY, double p_speed )
    {
        m_timestep = p_timestep;
        m_id = p_id;
        m_selfX = p_selfX;
        m_selfY = p_selfY;
        m_speed = p_speed;
    }
}
