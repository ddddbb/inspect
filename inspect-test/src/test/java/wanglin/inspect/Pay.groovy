package wanglin.inspect;

public class Pay {


    public void pay() {
        try {
            def ctx ;//定义上下文，上下问会缓存到redis等，细节不写了
            def channel = dispatchChannel(order);
            channel.pay(order,ctx);//支付，&上下文记录渠道和调用次数
        } catch (Throwable e) {
           def cmd= exceptionHandler.getCommond(order,e);
            switch (cmd.COMAND){
                case RAISE:throw RuntimeException(cmd.CODE,cmd.desc);break;
                case RETRY:channel.pay(order,ctx);break;
                case HIGH:
                    channel = dispatchChannel(order,ctx)//根据上下文渠道，重新调度渠道
                    channel.pay(order,ctx);
                    break;
            }
        }
    }

    public void query() {

    }

    public void retry() {

    }

    public void highPay() {

    }
}
