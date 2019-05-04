package wanglin.inspect;


import lombok.Data;

import java.nio.charset.Charset;

@Data
public class BizType {
    String  code;
    String  name;
    String  notifyUrl;
    String  sequence;
    String  charset;


    public String getCharset() {
        return charset == null ? Charset.defaultCharset().name() : charset;
    }
}
