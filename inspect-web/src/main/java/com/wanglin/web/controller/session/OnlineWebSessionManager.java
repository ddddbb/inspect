package com.wanglin.web.controller.session;

import com.wanglin.common.constant.ShiroConstants;
import com.wanglin.service.shiro.session.OnlineSession;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 主要是在此如果会话的属性修改了 就标识下其修改了 然后方便 OnlineSessionDao同步
 * 
 * @author wanglin
 */
public class OnlineWebSessionManager extends DefaultWebSessionManager
{
    private static final Logger log = LoggerFactory.getLogger(OnlineWebSessionManager.class);
    
    @Override
    public void setAttribute(SessionKey sessionKey, Object attributeKey, Object value) throws InvalidSessionException
    {
        super.setAttribute(sessionKey, attributeKey, value);
        if (value != null && needMarkAttributeChanged(attributeKey))
        {
            OnlineSession s = (OnlineSession) doGetSession(sessionKey);
            s.markAttributeChanged();
        }
    }

    private boolean needMarkAttributeChanged(Object attributeKey)
    {
        if (attributeKey == null)
        {
            return false;
        }
        String attributeKeyStr = attributeKey.toString();
        // 优化 flash属性没必要持久化
        if (attributeKeyStr.startsWith("org.springframework"))
        {
            return false;
        }
        if (attributeKeyStr.startsWith("javax.servlet"))
        {
            return false;
        }
        if (attributeKeyStr.equals(ShiroConstants.CURRENT_USERNAME))
        {
            return false;
        }
        return true;
    }

    @Override
    public Object removeAttribute(SessionKey sessionKey, Object attributeKey) throws InvalidSessionException
    {
        Object removed = super.removeAttribute(sessionKey, attributeKey);
        if (removed != null)
        {
            OnlineSession s = (OnlineSession) doGetSession(sessionKey);
            s.markAttributeChanged();
        }

        return removed;
    }

    /**
     * 验证session是否有效 用于删除过期session
     */
    @Override
    public void validateSessions()
    {


    }

    @Override
    protected Collection<Session> getActiveSessions()
    {
        throw new UnsupportedOperationException("getActiveSessions method not supported");
    }
}
