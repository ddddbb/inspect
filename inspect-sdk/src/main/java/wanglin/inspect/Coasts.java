package wanglin.inspect;

public interface Coasts {


    public interface VarHandler {
        String RequestHandler = "requestHandler";
    }

    public interface TOPIC {
        String INSPECT_RESULT = "inspect_result";
    }

    public interface CACHE {
        String CONTEXT_STATUS = "/context_status/";
        String SYNC_SWITCH = "/inspect/switch/";
    }

    public interface Ctx {
        String ID       = "id";
        String BIZ_TYPE = "type";
        String REQUEST  = "req";
    }
}
