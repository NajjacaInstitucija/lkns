import java.util.*;
import java.io.*;
public class BCastLinker {
    PrintWriter[] dataOut;
    BufferedReader[] dataIn;
    BufferedReader dIn;
    int myId, N;
    Connector connector;

    int registar; // read/write registar za intove u ovom slučaju


    public IntLinkedList neighbors = new IntLinkedList();
    public BCastLinker(String basename, int id, int numProc) throws Exception {
        myId = id;
        N = numProc;

        registar = 0;

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
        Util.println(" received message " + getline);

        //Util.println(" Dobijena poruka ");

        StringTokenizer st = new StringTokenizer(getline);
        int srcId = Integer.parseInt(st.nextToken());
        int destId = Integer.parseInt(st.nextToken());
        String tag = st.nextToken();
        String msg = st.nextToken("#");

        if(tag.equals("release"))
          Util.println(" enter text: ");

        else if(tag.equals("change"))
          setRegistar(Integer.parseInt(msg.trim()));


        return new Msg(srcId, destId, tag, msg);
    }
    public int getMyId() { return myId; }
    public int getNumProc() { return N; }
    public void close() {connector.closeSockets();}

    public int getRegistar() { return registar; }
    public void setRegistar(int val) { registar = val; }
}
