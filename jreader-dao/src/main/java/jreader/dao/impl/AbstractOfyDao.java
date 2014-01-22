package jreader.dao.impl;

import java.util.Collection;

import jreader.dao.OfyDao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;

abstract class AbstractOfyDao<EntityType> implements OfyDao<EntityType> {
	
	private ObjectifyFactory objectifyFactory;
	
	public AbstractOfyDao(ObjectifyFactory objectifyFactory) {
		this.objectifyFactory = objectifyFactory;
	}
	
	protected Objectify getOfy() {
		return objectifyFactory.begin();
	}
	
	@Override
	public EntityType save(final EntityType entity) {
		final Objectify ofy = getOfy();
		return ofy.transact(new Work<EntityType>() {
			@Override
			public EntityType run() {
				Key<EntityType> key = ofy.save().entity(entity).now();
				return ofy.load().key(key).now();
			}
		});
	}
	
	@Override
	public void saveAll(final Collection<EntityType> entities) {
		final Objectify ofy = getOfy();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				ofy.save().entities(entities).now();
			}
		});
	}
	
	@Override
	public void delete(final EntityType entity) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				ofy.delete().entity(entity).now();
			}
		});
	}
	
	@Override
	public void deleteAll(final Collection<EntityType> entities) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				ofy.delete().entities(entities).now();
			}
		});
	}

}
