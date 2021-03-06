package com.qingruan.museum.engine.repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;


@DirtiesContext
@ContextConfiguration(locations = { "classpath:applicationContext.xml","classpath*:/META-INF/spring/applicationContext-museum-*.xml"})
// 如果存在多个transactionManager，可以需显式指定
@TransactionConfiguration(transactionManager = "transactionManager")
public class JpaMappingTest extends SpringTransactionalTestCase {

	private static Logger logger = LoggerFactory.getLogger(JpaMappingTest.class);

	@PersistenceContext
	private EntityManager em;
	
	/*@Autowired
	private HourArchiveSchedule hour;*/

	@Test
	public void allClassMapping() throws Exception {
		Metamodel model = em.getEntityManagerFactory().getMetamodel();

		assertThat(model.getEntities()).as("No entity mapping found").isNotEmpty();

		for (EntityType entityType : model.getEntities()) {
			String entityName = entityType.getName();
			em.createQuery("select o from " + entityName + " o").getResultList();
			logger.info("ok: " + entityName);
		}
	}
	/*@Test
	public void justTestCount(){
		hour.computeHourArchiveSchedule();
	}*/
}
