import java.util.*;
import java.io.*;
public class StackLinker {
    PrintWriter[] dataOut;
    BufferedReader[] dataIn;
    BufferedReader dIn;
    int myId, N;
    Connector connector;

    Stack<Integer> stack; //stack (kompliciranija struktura od obicnog registra)


    public IntLinkedList neighbors = new IntLinkedList();
    public StackLinker(String basename, int id, int numProc) throws Exception {
        myId = id;
        N = numProc;

        stack = new Stack<Integer>();

        dataIn = new BufferedReader[numProc];
        dataOut = new PrintWriter[numProc];
        Topology.readNeighbors(myId, N, neighbors);
        connector = new Connector();
        connector.Connect(basename, myId, numProc, dataIn, dataOut);
    }
    public void sendMsg(int destId, String tag, String msg) {
        dataOut[destId].println(myId + " " + destId + " " +
				      tag + " " + msg + "#");
        //Util.println(" Poslao poruku procesu " + destId);
        dataOut[destId].flush();
    }
    public void sendMsg(int destId, String tag) {
        sendMsg(destId, tag, " 0 ");
    }
    public void multicast(IntLinkedList destIds, String tag, String msg){
        for (int i=0; i<destIds.size(); i++) {
            sendMsg(destIds.getEntry(i), tag, msg);
        }
    }
    public Msg receiveMsg(int fromId) throws IOException  {
        String getline = dataIn[fromId].readLine();
        //Util.println(" received message " + getline);

        //Util.println(" Dobijena poruka ");

        StringTokenizer st = new StringTokenizer(getline);
        int srcId = Integer.parseInt(st.nextToken());
        int destId = Integer.parseInt(st.nextToken());
        String tag = st.nextToken();
        String msg = st.nextToken("#");

        if(tag.equals("release"))
          Util.println(" enter (empty / peek / pop / push + (int)): ");

        else if(tag.equals("push"))
          stack.push(Integer.parseInt(msg.trim()));

        else if(tag.equals("pop"))
          stack.pop();


        return new Msg(srcId, destId, tag, msg);
    }
    public int getMyId() { return myId; }
    public int getNumProc() { return N; }
    public void close() {connector.closeSockets();}

    public void stackPush(int val) { stack.push(val); }
    public void stackPop() { stack.pop(); }
    public boolean stackEmpty() { return stack.empty(); }
    public int stackPeek() { return stack.peek(); }

}
