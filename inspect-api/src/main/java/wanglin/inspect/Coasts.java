package wanglin.inspect;

public interface Coasts {
    interface TOPIC {
        String SEQUENCE_PERSIST        = "sequence_persist";
        String SEQUENCE_PERSIST_RESULT = "sequence_persist_result";
        String RULE_RESULT_PERSIST     = "rule_result_persist";
        String DATA_RESULT_PERSIST     = "data_result_persist";
        String RESULT_NOTIFY           = "result_notify";
    }


    interface CTX {
        String ID       = "id";
        String BIZ_TYPE = "type";
        String REQUEST  = "req";
    }

    interface CACHE {
        String SEQUENCE = "sequence:";
    }
}
