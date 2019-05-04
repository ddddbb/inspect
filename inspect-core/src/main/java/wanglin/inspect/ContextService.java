package wanglin.inspect;

public interface ContextService {
    void save(InspectContext context);

    InspectContext get(Long sequence);
}
