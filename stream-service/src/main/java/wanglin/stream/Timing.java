package wanglin.stream;

import java.util.concurrent.TimeUnit;

public @interface Timing {
    EventMode type();

    int value();

    TimeUnit unit();
}
