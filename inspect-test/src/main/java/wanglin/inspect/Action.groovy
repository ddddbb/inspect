package wanglin.inspect

class Action {
    void receivemessage(String channel,Object data){
        ChannelService cs = getChannelService(channel);
        cs.verifySign(data);//验证信息可行性
        def act = cs.getAccount(data);
        def contemt = cs.getContent(data);
//        保存信息到工单
//
    }
}

interface ChannelService{

}