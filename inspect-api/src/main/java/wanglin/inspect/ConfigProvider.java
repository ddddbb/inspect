package wanglin.inspect;

import java.io.IOException;
import java.util.List;

public interface ConfigProvider {
    List<BizType> getAllBizType() throws IOException;

    List<Data> getAllVar() throws IOException;

    List<Rule> getAllRule() throws IOException;
}
