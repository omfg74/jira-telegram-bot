package com.omfgdevelop.jiratelegrambot

import com.omfgdevelop.jiratelegrambot.config.AppConfig
import com.omfgdevelop.jiratelegrambot.config.JiraBotConfig
import com.omfgdevelop.jiratelegrambot.config.MvcConfig
import org.apache.catalina.security.SecurityConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.testcontainers.containers.PostgreSQLContainer

import javax.sql.DataSource
import java.time.Duration

/**
 * TestContainers config
 */
@Configuration
@ComponentScan(basePackages = ["com.omfgdevelop.jiratelegrambot"])
class DbConfigGroovy {


    @Value('${spring.datasource.url}')
    String datasourceUrl

    @Value('${spring.datasource.username}')
    String username

    @Value('${spring.datasource.password}')
    String password

    private String getDatabase() {
        final URI uri = new URI(datasourceUrl.replace("jdbc:", ""))
        return uri.getPath().substring(1)
    }

    @Bean
    PostgreSQLContainer postgreSqlContainer() {
        PostgreSQLContainer postgreSQLContainer = null
        def migrationsDir = new File("src/test/resources/sql")

        postgreSQLContainer = (new PostgreSQLContainer("postgres:12")
                .withDatabaseName(getDatabase())
                .withUsername(username)
                .withPassword(password)
                .withFileSystemBind(migrationsDir.getAbsolutePath(), "/docker-entrypoint-initdb.d")
                .withStartupTimeout(Duration.ofSeconds(600))) as PostgreSQLContainer

        postgreSQLContainer.start()
        postgreSQLContainer
    }


    @Bean
    DataSource dataSource() {
        def ds = new DriverManagerDataSource()
        ds.setDriverClassName("org.postgresql.Driver")

        ds.setUrl(String.format("jdbc:postgresql://%s:%s/%s",
                postgreSqlContainer().getContainerIpAddress(),
                postgreSqlContainer().getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                getDatabase()))
        ds.setUsername(username)
        ds.setPassword(username)

        ds
    }


    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        def managerFactoryBean = new LocalContainerEntityManagerFactoryBean()
        managerFactoryBean.setDataSource(dataSource)

        managerFactoryBean.setPackagesToScan("com.omfgdevelop.jiratelegrambot")

        def va = new HibernateJpaVendorAdapter()

        managerFactoryBean.setJpaVendorAdapter(va)
        managerFactoryBean.afterPropertiesSet()

        managerFactoryBean
    }

}
