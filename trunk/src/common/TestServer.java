package common;

public class TestServer {

    public static void main(String[] argv) {
        TcpServer serv = new TcpServer(9999);
        TcpClient cli = new TcpClient("localhost", 9999, "connect1");
        Thread t = new Thread(serv);
        Thread t2 = new Thread(cli);
        t.start();
        t2.start();
        try {
            t.sleep(100);
            t2.sleep(100);
        } catch (Exception e) {
            System.err.println("Problème Thread " + e.getMessage());
        }
        cli.sendMsg("10101100000");
        serv.sendMsg("toto");
        cli.sendMsg("10101100000lol");
        serv.sendMsg("totolol");
        cli.sendMsg("msg3");
        serv.sendMsg("totomsg3");
        cli.sendMsg("msg4");
        serv.sendMsg("totololmsg4");
        cli.disconnect();
        serv.close();
    }
}
