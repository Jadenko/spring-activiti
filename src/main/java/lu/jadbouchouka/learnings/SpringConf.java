package lu.jadbouchouka.learnings;

import java.util.Properties;

import javax.sql.DataSource;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@PropertySource(value = "classpath:db-config/database-config.properties", ignoreResourceNotFound = false)
@ComponentScan("lu.jadbouchouka.learnings")
@EnableWebMvc
public class SpringConf {

	/**
	 * To resolve ${}
	 *
	 * @return
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/activiti");
		dataSource.setUsername("root");
		dataSource.setPassword("root");

		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource) {
		try {
			LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
			factoryBean.setPackagesToScan("lu.jadbouchouka");

			factoryBean.setJpaProperties(additionalProperties());
			factoryBean.setPersistenceUnitName("activiti");
			factoryBean.setDataSource(dataSource);

			JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter() {

				{
					setDatabase(Database.MYSQL);
				}
			};
			factoryBean.setJpaVendorAdapter(vendorAdapter);
			return factoryBean;
		} catch (Exception e) {

			throw e;
		}
	}

	@Bean
	public Properties additionalProperties() {
		return new Properties() {

			private static final long serialVersionUID = 1L;

			{ // Hibernate Specific:
				setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
				setProperty("hibernate.show_sql", "true");
			}
		};
	}

	// @Bean
	// public SessionFactory sessionFactory(DataSource dataSource) throws
	// Exception{
	// AnnotationSessionFactoryBean factoryBean = new
	// AnnotationSessionFactoryBean();
	// factoryBean.setDataSource(dataSource);
	// Properties properties = new Properties();
	// properties.setProperty("hibernate.dialect",
	// "org.hibernate.dialect.MySQLDialect");
	// properties.setProperty("hibernate.show_sql", "true");
	// factoryBean.setHibernateProperties(properties);
	// factoryBean.afterPropertiesSet();
	// return factoryBean.getObject();
	// }

	@Bean
	public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactoryBean.getObject());

		return transactionManager;
	}
	
	
	/**
	 * Activiti
	 */
	@Bean
	public SpringProcessEngineConfiguration processEngineConfiguration(DataSource dataSource,PlatformTransactionManager transactionManager){
		SpringProcessEngineConfiguration processEngineConfiguration =  new SpringProcessEngineConfiguration();
		processEngineConfiguration.setDataSource(dataSource);
		processEngineConfiguration.setTransactionManager(transactionManager);
		processEngineConfiguration.setDatabaseSchemaUpdate("true");
		processEngineConfiguration.setJobExecutorActivate(false);
		processEngineConfiguration.setDeploymentResources(new Resource[]{new ClassPathResource("EnhacementForInputHall.bpmn")});
		return processEngineConfiguration;
	}
	
	@Bean
	public ProcessEngineFactoryBean processEngine(SpringProcessEngineConfiguration processEngineConfiguration){
		ProcessEngineFactoryBean factoryBean= new ProcessEngineFactoryBean();
		factoryBean.setProcessEngineConfiguration(processEngineConfiguration);
		return factoryBean;
	}
	
	@Bean
	public RuntimeService runtimeService(ProcessEngineFactoryBean processEngine) throws Exception{
		return processEngine.getObject().getRuntimeService();
	}
	
	@Bean
	public TaskService taskService(ProcessEngineFactoryBean processEngine) throws Exception{
		return processEngine.getObject().getTaskService();
	}
	
	@Bean
	public RepositoryService repositoryService(ProcessEngineFactoryBean processEngine) throws Exception{
		return processEngine.getObject().getRepositoryService();
	}
}