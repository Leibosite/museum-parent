package com.qingruan.museum.agent.modified.event;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.event.spi.PostCollectionRecreateEvent;
import org.hibernate.event.spi.PostCollectionRecreateEventListener;

@Slf4j
public class PostCollectionRecreateEventListenerImpl implements PostCollectionRecreateEventListener {
	private static final long serialVersionUID = 1L;

	@Override
	public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
		log.debug(event.toString());
	}
}

