import java.lang.*; import java.util.*;
import java.net.*; import java.io.*;
public class InvalidationClient {
    BufferedReader din;
    PrintStream pout;
    Stack<Integer> myStack;

    public InvalidationClient() { myStack = new Stack<Integer>(); }

    public void getSocket() throws IOException {
        Socket server = new Socket(Symbols.nameServer,
                                                Symbols.ServerPort);
        din = new BufferedReader(
                    new InputStreamReader(server.getInputStream()));
        pout = new PrintStream(server.getOutputStream());
    }

    //treba ubacit requestove prije pisanja
    public void push(int val)
            throws IOException {
        getSocket();
        pout.println("push " + val);
        pout.flush();
    }

    public boolean empty()
      throws IOException {
        getSocket();
        pout.println("empty");
        pout.flush();
        return Boolean.parseBoolean(din.readLine());
      }

    public void pop()
      throws IOException, EmptyStackException {
        getSocket();
        pout.println("pop");
        pout.flush();
      }

    public int peek()
      throws IOException, EmptyStackException {
        getSocket();
        pout.println("peek");
        pout.flush();
        return Integer.parseInt(din.readLine());
      }


    public static void main(String[] args) {
        InvalidationClient myClient = new InvalidationClient();
        try {

            while(true)
            {
      
              System.out.println(" enter (empty / peek / pop / push + (int)): ");
              Scanner keyboard = new Scanner(System.in);
              String input = keyboard.nextLine();

              StringTokenizer st = new StringTokenizer(input);
              String naredba = st.nextToken();

              if(naredba.equals("empty")) System.out.println(" Empty = " + myClient.empty());
              else if(naredba.equals("pop")) myClient.pop();
              else if(naredba.equals("peek")) System.out.println(" Peek = " + myClient.peek());
              else if(naredba.equals("push"))
              {
                int val = Integer.parseInt(st.nextToken());
                myClient.push(val);
               }
            }

        } catch (Exception e) {
            System.err.println("Server aborted:" + e);
        }
    }
}
