package pwr.osm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseDAO{
	protected EntityManager em;
	 
    public EntityManager getEntityManager() {
        return em;
    }
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }
}
