package org.mysimulationmodel.simulation.common;

public class CInputFormatforGA
{
    public String m_roaduser_id;
    public double m_x_axis;
    public double m_y_axis;
    public int m_timestep;

    public CInputFormatforGA( String p_roaduser_id, double p_x_axis, double p_y_axis,int p_timestep )
    {
        m_roaduser_id = p_roaduser_id;
        m_x_axis = p_x_axis;
        m_y_axis = p_y_axis;
        m_timestep = p_timestep;
    }
}
