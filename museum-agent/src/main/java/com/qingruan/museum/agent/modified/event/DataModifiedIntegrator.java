package com.qingruan.museum.agent.modified.event;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.jboss.logging.Logger;

/**
 * 注册Hibernate Event响应事件.
 * 
 * @author tommy
 */
public class DataModifiedIntegrator implements Integrator {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, DataModifiedIntegrator.class.getName());
	
	public static final String AUTO_REGISTER = "hibernate.listeners.hss-entity.autoRegister";
	
	@Override
	public void integrate(Configuration configuration,
			SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry) {
		final boolean autoRegister = ConfigurationHelper.getBoolean( AUTO_REGISTER, configuration.getProperties(), true );
		if ( !autoRegister ) {
			LOG.debug( "Skipping museum-entity listener auto registration" );
			return;
		}

		EventListenerRegistry listenerRegistry = serviceRegistry.getService(EventListenerRegistry.class);
	//	listenerRegistry.addDuplicationStrategy(EnversListenerDuplicationStrategy.INSTANCE);

		listenerRegistry.appendListeners(EventType.POST_UPDATE, new PostUpdateEventListenerImpl());
		listenerRegistry.appendListeners(EventType.POST_DELETE, new PostDeleteEventListenerImpl());
		listenerRegistry.appendListeners(EventType.POST_INSERT, new PostInsertEventListenerImpl());
		listenerRegistry.appendListeners(EventType.POST_COLLECTION_RECREATE, new PostCollectionRecreateEventListenerImpl());
		listenerRegistry.appendListeners(EventType.POST_COLLECTION_UPDATE, new PostCollectionUpdateEventListenerImpl());
		
		listenerRegistry.appendListeners(EventType.PRE_UPDATE, new PreUpdateEventCommonListener());
	}

	@Override
	public void integrate(MetadataImplementor metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
		// nothing to do afaik
	}

	@Override
	public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
		// nothing to do afaik
	}

}
