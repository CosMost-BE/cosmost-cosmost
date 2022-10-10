package com.cosmost.project.cosmost.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Scan Package Path
 * JPA All Scan -> Application Load 속도가 느림 -> 테이블이 늘어나면 늘어날수록
 */
@Configuration
@EnableJpaAuditing
public class JPAConfiguration {
}