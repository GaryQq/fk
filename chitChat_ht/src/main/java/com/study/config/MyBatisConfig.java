package com.study.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.study.util.datasource.DatabaseType;
import com.study.util.datasource.DynamicDataSource;

@Configuration
@MapperScan(basePackages = "com.study.mapper")
public class MyBatisConfig {
	@Autowired
	private Environment env;

	/**
	 * 主数据源(默认使用)
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	public DataSource lotterytieDataSource() throws Exception {
		Properties props = new Properties();
		props.put("driverClassName", env.getProperty("spring.datasource1.driverClassName"));
		props.put("url", env.getProperty("spring.datasource1.url"));
		props.put("username", env.getProperty("spring.datasource1.username"));
		props.put("password", env.getProperty("spring.datasource1.password"));
		return DruidDataSourceFactory.createDataSource(props);
	}

	/**
	 * 辅数据源
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	public DataSource lotteryDataSource() throws Exception {
		Properties props = new Properties();
		props.put("driverClassName", env.getProperty("spring.datasource2.driverClassName"));
		props.put("url", env.getProperty("spring.datasource2.url"));
		props.put("username", env.getProperty("spring.datasource2.username"));
		props.put("password", env.getProperty("spring.datasource2.password"));
		return DruidDataSourceFactory.createDataSource(props);
	}

	/**
	 * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错（一般用于多数据源的情况下）
	 * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
	 */
	@Bean
	@Primary
	public DynamicDataSource dataSource(@Qualifier("lotterytieDataSource") DataSource lotterytieDataSource,
			@Qualifier("lotteryDataSource") DataSource lotteryDataSource) {
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DatabaseType.lotterytie, lotterytieDataSource);
		targetDataSources.put(DatabaseType.lottery, lotteryDataSource);

		DynamicDataSource dataSource = new DynamicDataSource();
		dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
		dataSource.setDefaultTargetDataSource(lotterytieDataSource);// 默认的datasource设置为myTestDbDataSource

		return dataSource;
	}

	/**
	 * 根据数据源创建SqlSessionFactory
	 */
	@Bean
	public SqlSessionFactory sqlSessionFactory(@Qualifier("lotterytieDataSource") DataSource lotterytieDataSource,
			@Qualifier("lotteryDataSource") DataSource lotteryDataSource) throws Exception {
		SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
		fb.setDataSource(this.dataSource(lotterytieDataSource, lotteryDataSource));
		fb.setTypeAliasesPackage(env.getProperty("com.study.mapper"));// 如使用注解方式,可以不用配置数据源
		fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*.xml"));
		return fb.getObject();
	}

	/**
	 * 配置事务管理器
	 */
	@Bean
	public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
		return new DataSourceTransactionManager(dataSource);
	}

}
