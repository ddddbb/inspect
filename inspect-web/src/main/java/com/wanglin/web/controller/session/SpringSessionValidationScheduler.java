package com.wanglin.web.controller.session;

import com.wanglin.common.utils.Threads;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 自定义任务调度器完成
 *
 * @author wanglin
 */
public class SpringSessionValidationScheduler implements SessionValidationScheduler {
    private static final Logger log = LoggerFactory.getLogger(SpringSessionValidationScheduler.class);

    public static final long DEFAULT_SESSION_VALIDATION_INTERVAL = DefaultSessionManager.DEFAULT_SESSION_VALIDATION_INTERVAL;

    /**
     * 定时器，用于处理超时的挂起请求，也用于连接断开时的重连。
     */
    @Autowired
    @Qualifier("scheduledExecutorService")
    private ScheduledExecutorService executorService;

    private volatile boolean enabled = false;

    /**
     * The session manager used to validate sessions.
     */
    private ValidatingSessionManager sessionManager;

    private long sessionValidationInterval = DEFAULT_SESSION_VALIDATION_INTERVAL;

    public SpringSessionValidationScheduler() {
    }

    public SpringSessionValidationScheduler(ValidatingSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setSessionManager(ValidatingSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setSessionValidationInterval(long sessionValidationInterval) {
        this.sessionValidationInterval = sessionValidationInterval;
    }

    /**
     * Starts session validation by creating a spring PeriodicTrigger.
     */
    @Override
    public void enableSessionValidation() {
        enabled = true;
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (enabled) {
                    sessionManager.validateSessions();
                }
            }
        }, 1000, sessionValidationInterval, TimeUnit.MILLISECONDS);

        this.enabled = true;
    }

    @Override
    public void disableSessionValidation() {
        if (this.enabled) {
            Threads.shutdownAndAwaitTermination(executorService);
        }
        this.enabled = false;
    }
}
