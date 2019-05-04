package wanglin.inspect.protocol

class HttpProtocolAdapter extends ProtocolAdapter{
    //  MQClient mqclient
    public HttpProtocolAdapter(String bizCode,String notifyUrl){

    }

    void onMessage(Object result){
        //构造协议client并发送风控结果
        getClient(getProtocol(notifyUrl),notifyUrl).send(result)
    }

    //构造本协议的client
    def getClient(String protocol,String url) {}

}

