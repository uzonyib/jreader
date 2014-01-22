package jreader.dao;

import java.util.Collection;

public interface OfyDao<EntityType> {
	
	EntityType save(EntityType entity);
	
	void saveAll(Collection<EntityType> entities);
	
	void delete(EntityType entity);
	
	void deleteAll(Collection<EntityType> entities);
	
}
