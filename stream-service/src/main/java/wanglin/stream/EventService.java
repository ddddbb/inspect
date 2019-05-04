package wanglin.stream;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventService {
    ExecutorService pool = Executors.newFixedThreadPool(2);

    public void onEvent(Event e) {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                StreamScript ss = getScript(e);
                if (e.mode == EventMode.ON)
                    ss.on(e.dataType, e.value);
                else
                    ss.expire(e.dataType, e.value);
            }
        });
    }

    //获取最近1分钟的任务，执行
    @Scheduled(cron = "0 * * * * ? *")
    public void expire() {
        List<Event> events = new ArrayList<Event>();
        events.forEach(e -> {
            pool.submit(new Runnable() {
                @Override
                public void run() {

                }
            });
        });
    }

    private StreamScript getScript(Event e) {
        return null;
    }

}
