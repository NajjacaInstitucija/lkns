import java.util.*;
import java.io.*;

import java.lang.*;
import java.net.*;

public class ManagerLinker {
    PrintWriter[] dataOut;
    BufferedReader[] dataIn;
    BufferedReader dIn;
    int myId, N;
    ManagerConnector connector;

    // BufferedReader sdin;
    // PrintStream spout;

  //  int registar; // read/write registar za intove u ovom slučaju
    // boolean inCharge;
    // boolean isCurrent;

    public IntLinkedList neighbors = new IntLinkedList();
    public ManagerLinker(String basename, int id, int numProc) throws Exception {
        myId = id;
        N = numProc;

        //inCharge = (myId == 0) ? true : false;
        // inCharge = false;
        // isCurrent = false;
        // registar = 0;

        dataIn = new BufferedReader[numProc];
        dataOut = new PrintWriter[numProc];
        Topology.readNeighbors(myId, N, neighbors);
        connector = new ManagerConnector();
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
      //  Util.println(" received message " + getline);

        //Util.println(" Dobijena poruka ");

        StringTokenizer st = new StringTokenizer(getline);
        int srcId = Integer.parseInt(st.nextToken());
        int destId = Integer.parseInt(st.nextToken());
        String tag = st.nextToken();
        String msg = st.nextToken("#");

        if(tag.equals("release"))
          Util.println(" enter (read / write + (int)): ");

        else if(tag.equals("invalidate"))
          {
            int novi = Integer.parseInt(msg.trim());
            //setMyCurrent(novi);
            setMyCurrent(false);
            setMyOwnerInt(novi);

            Util.println(" Registar invalidated. ");
          }

       else if(tag.equals("downgrade"))
       {

         Util.println(" Saving to server ... ");
         writeRegistar(readMyRegistar());
         Util.mySleep(2000);
         setMyOwnerInt(Integer.parseInt(msg.trim()));
         //Util.println(msg);

         Util.println(" Ownership downgraded. New owner = " + getMyOwnerInt());
       }



        return new Msg(srcId, destId, tag, msg);
    }
    public int getMyId() { return myId; }
    public int getNumProc() { return N; }
    public void close() { connector.closeSockets(); }


    // --------------------------------------------------------------------------
    // --------------------------------------------------------------------------
    // --------------------------------------------------------------------------
    // server functions
    public int readRegistar() throws IOException
    {
      ManagerInvalidationClient client = connector.getClient();
      int reg = client.read();
      return reg;
    }

    public void writeRegistar(int value) throws IOException
    {
      ManagerInvalidationClient client = connector.getClient();
      client.write(value);
    }

    public int readOwner() throws IOException
    {
      ManagerInvalidationClient client = connector.getClient();
      int owner = client.getOwn();
      return owner;
    }

    public void writeOwner(int value) throws IOException
    {
      ManagerInvalidationClient client = connector.getClient();
      client.setOwn(value);
    }

    public boolean readCurrent() throws IOException
    {
      ManagerInvalidationClient client = connector.getClient();
      int c = client.getCurr();
      if(c == myId) return true;
      return false;
    }

    public void writeCurrent(int value) throws IOException
    {
      ManagerInvalidationClient client = connector.getClient();
      client.setCurr(value);
    }


// --------------------------------------------------------------------------
// --------------------------------------------------------------------------
// --------------------------------------------------------------------------
// NON-server functions
    public int readMyRegistar()
    {
      ManagerInvalidationClient client = connector.getClient();
      return client.getRegistar();
    }

    public void writeMyRegistar(int val)
    {
      ManagerInvalidationClient client = connector.getClient();
      client.setRegistar(val);
    }
    //
    // public boolean getMyCurrent()
    // {
    //   ManagerInvalidationClient client = connector.getClient();
    //   if (client.getIsCurrent() != myId) return false;
    //   return true;
    // }
    //
    // public void setMyCurrent(int val)
    // {
    //   ManagerInvalidationClient client = connector.getClient();
    //   client.setIsCurrent(val);
    // }

    public boolean getMyCurrent()
    {
      ManagerInvalidationClient client = connector.getClient();
      return client.getIsCurrent();
    }

    public void setMyCurrent(boolean val)
    {
      ManagerInvalidationClient client = connector.getClient();
      client.setIsCurrent(val);
    }



    public void setMyOwnerInt(int val)
    {
      ManagerInvalidationClient client = connector.getClient();
      client.setOwner(val);
    }

    public int getMyOwnerInt()
    {
      ManagerInvalidationClient client = connector.getClient();
      return client.getOwner();
    }


    // public void setMyOwnerBoolean(boolean val)
    // {
    //   ManagerInvalidationClient client = connector.getClient();
    //   client.setIsOwner(val);
    // }
    //
    // public boolean getMyOwnerBoolean()
    // {
    //   ManagerInvalidationClient client = connector.getClient();
    //   return client.getIsOwner();
    // }



}
