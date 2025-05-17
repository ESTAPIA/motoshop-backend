package ec.puce.motoshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

/**
 * Custom DataSource configuration for production environment.
 * This class ensures proper JDBC URL formatting and connection properties
 * when deployed to cloud platforms like Render with PostgreSQL databases.
 */
@Configuration
@Profile("prod")
public class DatabaseConfig {
    
    @Value("${spring.datasource.url:jdbc:postgresql://db.azfjqhgglyoqyipcdsoh.supabase.co:5432/postgres?sslmode=require}")
    private String jdbcUrl;
    
    @Value("${spring.datasource.username:postgres}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Bean
    @Primary
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        // Connection pool settings
        dataSource.setMaximumPoolSize(10);
        dataSource.setMinimumIdle(5);
        dataSource.setConnectionTimeout(20000);
        return dataSource;
    }
}
