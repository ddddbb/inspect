package wanglin.inspect.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import wanglin.inspect.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DbConfigProvider implements ConfigProvider {

    JdbcTemplate jdbcTemplate;
    public DbConfigProvider(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<BizType> getAllBizType() throws IOException {
        String sql = "select * from  biztype";
        List<BizType> list =  jdbcTemplate.query(sql, new RowMapper<BizType>() {
            @Override
            public BizType mapRow(ResultSet rs, int i) throws SQLException {
                BizType bizType = new BizType();
                bizType.setCode(rs.getString("code"));
                bizType.setName(rs.getString("name"));
                bizType.setNotifyUrl(rs.getString("notify_url"));
                bizType.setCharset(rs.getString("charset"));
                return bizType;
            }
        });
        return list;
    }

    @Override
    public List<Data> getAllVar() throws IOException {
        String sql = "select * from  data";
        List<Data> list =  jdbcTemplate.query(sql, new RowMapper<Data>() {
            @Override
            public Data mapRow(ResultSet rs, int i) throws SQLException {
                Data t = new Data();
                t.setBizCode(rs.getString("bizcode"));
                t.setId(rs.getLong("id"));
                t.setName(rs.getString("name"));
                t.setServiceName(rs.getString("service_name"));
                return t;
            }
        });
        return list;
    }

    @Override
    public List<Rule> getAllRule() throws IOException {
        String sql = "select * from  rule";
        List<Rule> list =  jdbcTemplate.query(sql,new RowMapper<Rule>() {
            @Override
            public Rule mapRow(ResultSet rs, int i) throws SQLException {
                Rule t = new Rule();
                t.setBizCode(rs.getString("bizcode"));
                t.setId(rs.getLong("id"));
                t.setName(rs.getString("name"));
                t.setContext(rs.getString("context"));
                t.setEngine(EngineEnum.valueOf(rs.getString("engine")));
                t.setResultStrategy(rs.getString("result_strategy"));
                t.setResultField(rs.getString("result_field"));
                return t;
            }
        });
        return list;
    }
}
