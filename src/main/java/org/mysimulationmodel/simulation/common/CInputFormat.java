package org.mysimulationmodel.simulation.common;

public class CInputFormat
{
    public int m_scenario_id;
    public String m_roaduser_id;
    public double m_startx_axis;
    public double m_starty_axis;
    public double m_endx_axis;
    public double m_endy_axis;
    public double m_speed;
    public double m_max_speed;
    public int m_numberofcycle;
    public int m_start_cycle;
    public int m_type;
    public int m_strategy;
    public int m_pedtype;
    public CInputFormat(int p_scenario_id, String p_roaduser_id, double p_startx_axis, double p_starty_axis, double p_endx_axis,
                        double p_endy_axis, double p_speed, double p_max_speed, int p_numberofcycle, int p_start_cycle, int p_type, int p_strategy )
    {
        m_scenario_id = p_scenario_id;
        m_roaduser_id = p_roaduser_id;
        m_startx_axis = p_startx_axis;
        m_starty_axis = p_starty_axis;
        m_endx_axis = p_endx_axis;
        m_endy_axis = p_endy_axis;
        m_speed = p_speed;
        m_max_speed = p_max_speed;
        m_numberofcycle = p_numberofcycle;
        m_start_cycle = p_start_cycle;
        m_type = p_type;
        m_pedtype = p_strategy;
    }
}
