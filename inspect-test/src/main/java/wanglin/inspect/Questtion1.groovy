package wanglin.inspect

class Questtion1 {


}
//模拟客户端软件
class Visitor {
    Server server; //模拟客户端和服务器端通信机制
    Chat applyChat() {
        server.createChat(this);
    }
    void chat(Chat chat,String msg) {
        server.chat(chat,this,msg)
    }
}
//模拟客服软件client
class Agent {
    Server server;//通信机制
    def id;
    def name;
    def status;
    //listChat and choose
    void joinChat(Chat chat){
        server.joinChat(chat,this)
    }
    void chat(Chat chat,String msg) {
        server.chat(chat,this,msg)
    }
}

class Server {
    Set<Agent> agents;
    List<Chat> chats;

    void joinChat(Chat chat,Agent agent){
        chat.joinAgent(agent);
        //refresh chats...
    }
    Chat createChat(Visitor visitor) {
        return new Chat(visitor,getOneAgentIsNotBusy());
    }

    void chat(Chat chat,Object user,String msg) {
    }
    Agent getOneAgentIsNotBusy() {

    }
}
class Chat {
    Visitor visitor;
    Set<Agent> agents = new ArrayList<>();
    public Chat(Visitor v,Agent a){
        this.visitor =v;
        this.agents.add(a)
    }
    void joinAgent(Agent agent) {
        agents.add(agents)
    }
}
