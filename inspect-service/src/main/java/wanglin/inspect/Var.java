package wanglin.inspect;

import lombok.Data;

@Data
public class Var {
    String                  id;
    String                  name;
    String                  handlerName;

    public Var() {

    }

    public Var(String id, String name, String handlerName) {
        this.id = id;
        this.name = name;
        this.handlerName = handlerName;
    }


}
