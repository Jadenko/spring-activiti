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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@PropertySource(value = "classpath:db-config/database-config.properties", ignoreResourceNotFound = false)
@ComponentScan("lu.jadbouchouka.learnings")
@EnableWebMvc
@EnableJpaRepositories("lu.jadbouchouka.learnings.dao")
public class SpringConf {

	Database database = Database.ORACLE;
	
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
		
		if(Database.MYSQL.equals(database)){
			dataSource.setDriverClassName("com.mysql.jdbc.Driver");
			dataSource.setUrl("jdbc:mysql://localhost:3306/activiti");
			dataSource.setUsername("root");
			dataSource.setPassword("root");	
		}

		if(Database.ORACLE.equals(database)){
			dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
			dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:XE");
			dataSource.setUsername("jad");
			dataSource.setPassword("jad");
		}

		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		try {
			LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
			factoryBean.setPackagesToScan("lu.jadbouchouka.learnings.model");
			factoryBean.setJpaProperties(additionalProperties());
			factoryBean.setPersistenceUnitName("activiti");
			factoryBean.setDataSource(dataSource);

			JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter() {

				{
					setDatabase(database);
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
				if(Database.MYSQL.equals(database)){
					setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
				}
				
				if(Database.ORACLE.equals(database)){
					setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");	
				}
				setProperty("hibernate.show_sql", "true");
				setProperty("hibernate.hbm2ddl.auto", "update");
			}
		};
	}

	@Bean
	public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());

		return transactionManager;
	}
	
	/**
	 * Spring MVC Conf
	 */
	@Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
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