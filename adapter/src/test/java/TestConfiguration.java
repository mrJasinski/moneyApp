import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
class TestConfiguration
{
    @Bean
    @Primary
    DataSource testDataSource()
    {
        var result = new DriverManagerDataSource("jdbc:mariadb://localhost:3306/moneyTest", "root", "");
        result.setDriverClassName("org.mariadb.jdbc.Driver");

        return result;
    }
}
