package wanglin.inspect.config

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.junit4.SpringRunner
import wanglin.inspect.config.DbConfigProvider

@RunWith(SpringRunner.class)
@SpringBootTest
class DbConfigProviderTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    DbConfigProvider configLoader = new DbConfigProvider();

    @Before
    public void setup(){
        configLoader.jdbcTemplate = jdbcTemplate;
    }

    @Test
    public void test(){
        println  configLoader.allBizType;
    }
}
