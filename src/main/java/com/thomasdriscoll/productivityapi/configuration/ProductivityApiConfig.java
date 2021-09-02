package com.thomasdriscoll.productivityapi.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.thomasdriscoll.productivityapi.repository")
public class ProductivityApiConfig {
}
