import java.net.*;
import java.io.*;
import java.util.*;
public class InvalidationServer {
    Stack<Integer> stack;
    public InvalidationServer() {
        stack = new Stack<Integer>();
    }
    void handleclient(Socket theClient) {
        try {
            BufferedReader din = new BufferedReader
            (new InputStreamReader(theClient.getInputStream()));
            PrintWriter pout = new PrintWriter(theClient.getOutputStream());
            String getline = din.readLine();
            StringTokenizer st = new StringTokenizer(getline);
            String tag = st.nextToken();
            if (tag.equals("peek")) {
                try{
                  int peek = stack.peek();
                } catch(EmptyStackException ese)
                {
                  System.out.println("Stack is empty");
                  pout.println("Stack is empty.");
                }
                pout.println(stack.peek());
            }
            else if (tag.equals("empty")) {
                pout.println(stack.empty());
            }
            else if(tag.equals("pop"))
            {
              try
              {
                int top = stack.pop();
              } catch(EmptyStackException ese)
              {
                System.out.println("Stack is empty");
                pout.println("Stack is empty.");
              }
              pout.println(" Pop success. ");
            }

            else if(tag.equals("push"))
            {
              int val = Integer.parseInt(st.nextToken());
              stack.push(val);
              pout.println(" Push success. ");
            }

            pout.flush();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    public static void main(String[] args) {
        InvalidationServer is = new InvalidationServer();
        System.out.println("InvalidationServer started:");
        try {
            ServerSocket listener = new ServerSocket(Symbols.ServerPort);
            while (true) {
                Socket aClient = listener.accept();
                is.handleclient(aClient);
                aClient.close();
            }
        } catch (IOException e) {
            System.err.println("Server aborted:" + e);
        }
    }
}
