package wanglin.inspect.mock

import wanglin.inspect.ContextService
import wanglin.inspect.InspectContext

class MockContextService implements ContextService {
    static Map<Long,InspectContext> cache = new HashMap();
    void save(InspectContext context) {
        cache.put(context.sequence,context)
    }

    InspectContext get(Long sequence) {
        return cache.get(sequence)
    }
}
