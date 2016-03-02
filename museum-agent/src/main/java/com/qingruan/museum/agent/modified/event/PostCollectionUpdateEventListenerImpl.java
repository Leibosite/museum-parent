package com.qingruan.museum.agent.modified.event;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.CollectionEntry;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.event.spi.AbstractCollectionEvent;
import org.hibernate.event.spi.PostCollectionUpdateEvent;
import org.hibernate.event.spi.PostCollectionUpdateEventListener;

import com.qingruan.museum.agent.modified.DataModifiedDealer;

/**
 * 
 * @author tommy
 *
 */
@Slf4j
public class PostCollectionUpdateEventListenerImpl extends AbstractEventListener implements PostCollectionUpdateEventListener {
	private static final long serialVersionUID = 1L;
	
	protected final CollectionEntry getCollectionEntry(AbstractCollectionEvent event) {
        return event.getSession().getPersistenceContext().getCollectionEntry(event.getCollection());
    }

	private Serializable getId(Object entity, AbstractCollectionEvent event) {
		Serializable id = event.getAffectedOwnerIdOrNull();
		if (id == null) {
			// most likely this recovery is unnecessary since Hibernate Core probably try that
			EntityEntry entityEntry = event.getSession().getPersistenceContext().getEntry(entity);
			id = entityEntry == null ? null : entityEntry.getId();
		}
		
		return id;
	}
	
	@Override
	public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
		final Object entity = event.getAffectedOwnerOrNull();
		if (entity == null) {
			// Hibernate cannot determine every single time the owner especially
			// incase detached objects are involved
			// or property-ref is used
			// Should log really but we don't know if we're interested in this
			// collection for indexing
			return;
		}
		
		Serializable id = getId(entity, event);
		if (id == null) {
			log.warn("Unable to reindex entity on collection change, id cannot be extracted: " + event.getAffectedOwnerEntityName());
			return;
		}
		
		String ownerEntityName = event.getAffectedOwnerEntityName();
		
		if (log.isDebugEnabled()) {
			CollectionEntry collectionEntry = getCollectionEntry(event);
			String referencingPropertyName = collectionEntry.getRole().substring(ownerEntityName.length() + 1);	

			log.debug("Entity collection [{}] property [{}]changed.", ownerEntityName, referencingPropertyName);					;
		}

		DataModifiedDealer<?> modifiedDealer =  getDealer(entity.getClass());
		if (modifiedDealer == null) {
			return;
		}
		
		Object ownerEntity = event.getAffectedOwnerOrNull();
		PersistentCollection newCollection = event.getCollection();
		Serializable oldCollection = newCollection.getStoredSnapshot();
		CollectionEntry collectionEntry = getCollectionEntry(event);
		String referencingPropertyName = collectionEntry.getRole().substring(ownerEntityName.length() + 1);	
        
		modifiedDealer.onUpdateCollection(ownerEntity, ownerEntityName, referencingPropertyName, 
        		getNewCollectionContent(newCollection), getOldCollectionContent(oldCollection));
	}
	
    protected Collection<?> getNewCollectionContent(PersistentCollection newCollection) {
        return (Collection) newCollection;
    }
	
	protected Collection<?> getOldCollectionContent(Serializable oldCollection) {
        if (oldCollection == null) {
            return null;
        } else if (oldCollection instanceof Map) {
            return ((Map) oldCollection).keySet();
        } else {
            return (Collection) oldCollection;
        }
    }
	
}