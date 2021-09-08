package org.mysimulationmodel.simulation.agent;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.mysimulationmodel.simulation.environment.CEnvironment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by fatema on 16.03.2018.
 */
public class CHostGenarator extends IBaseAgentGenerator<CHostAgent>
{
    /**
     * environment
     */
    private final CEnvironment m_environment;


    /**
     * constructor of the generator
     * @param p_stream ASL code as any stream e.g. FileInputStream
     * @throws Exception Thrown if something goes wrong while generating agents.
     */
    public CHostGenarator(@Nonnull final InputStream p_stream, final CEnvironment p_environment ) throws Exception
    {
        super(
                // input ASL stream
                p_stream,
                // a set with all possible actions for the agent
                Stream.concat(
                        // we use all build-in actions of LightJason
                        CCommon.actionsFromPackage(),
                        // use the actions which are defined inside the agent class
                        CCommon.actionsFromAgentClass( CHostAgent.class )

                        // build the set with a collector
                ).collect( Collectors.toSet() )
        );

        m_environment = p_environment;
    }

    /**
     * generator method of the agent
     * @param p_data any data which can be put from outside to the generator method
     * @return returns an agent
     */
    @Override
    public final CHostAgent generatesingle( @Nullable final Object... p_data )
    {
        // create agent with a reference to the environment
        return new CHostAgent( m_configuration, m_environment );

    }
}
