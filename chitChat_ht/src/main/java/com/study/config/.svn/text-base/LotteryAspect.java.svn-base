package com.study.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.study.util.datasource.DatabaseContextHolder;
import com.study.util.datasource.DatabaseType;

/**
 * 切换数据源时面向切面,使用完备用数据源,切换回主数据源
 * 
 * @author Administrator
 *
 */
@Aspect
@Component
public class LotteryAspect {

	private static Logger logger = LoggerFactory.getLogger(LotteryAspect.class);

	@Pointcut("execution(public * com.study.service.lottery.*.*(..))")
	public void lotteryDatasource() {
	}

	/**
	 * 执行当前service进行切面操作切换数据源
	 * 
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Before("lotteryDatasource()")
	public void deBefore(JoinPoint joinPoint) throws Throwable {
		DatabaseContextHolder.setDatabaseType(DatabaseType.lottery);
	}

	/**
	 * 不管异常还是正常退出,执行的时候删除备用数据源切回主数据源
	 * 
	 * @param jp
	 */
	@After("lotteryDatasource()")
	public void after(JoinPoint jp) {
		DatabaseContextHolder.clearDatabaseType();
		DatabaseContextHolder.setDatabaseType(DatabaseType.lotterytie);
	}

}
