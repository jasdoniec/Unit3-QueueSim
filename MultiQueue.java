import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Queue;

public class MultiQueue implements Hades{

    private Queue<Customer>[] queues = (Queue<Customer>[]) new Queue<?>[SimulationVars.NUM_SERVERS];

    private Customer[] servers = new Customer[SimulationVars.NUM_SERVERS];

    private int count = 1;

    private int tick = 0;

    private int empty = 0;

    private int cusCount = 0;

    private int longestQueue = 0;

    private ArrayList<Integer> waitTimes = new ArrayList<Integer>();

    private ArrayList<String> events = new ArrayList<String>();

    public MultiQueue(){
        for (int i = 0; i < queues.length; i++) {
            queues[i] = new LinkedList<>();
        }
    }

    private int len(int i){
        if (servers[i] == null){
            return queues[i].size();
        }
        return queues[i].size() + 1;
    }

    public void newCustomers(Customer customer) {
        
        
            count++;
            //TODO: Add customer to the shortes queue in queues
            int shortest = 0;
            int longest = 0;
            for (int i = 1; i < queues.length; i++){
                if (len(i) < len(shortest)){
                    shortest = i;
                }
                if (len(i) > len(longest)){
                    longest = i;
                }
            }

            if (len(longest) > longestQueue){
                longestQueue = len(longest);
            }

            queues[shortest].add(customer);
	        events.add("Customer #"+ customer.ID+" joined the queue #"+shortest);
        
    }

    public void dispatchCustomers() {
        //TODO: add customers from queue if server empty
        for (int i = 0; i < servers.length; i ++){
            if (servers[i] == null){
                if (!queues[i].isEmpty()){
                    servers[i] = queues[i].remove();
		            events.add("Customer " + servers[i].ID+" has joined server #"+i);
		        }
                else
                    empty++;
            }
        }
    }

    public void processDepartures(int tick) {
        this.tick = tick;
        //TODO: decrement wait time in server and remove if empty
        for (int i = 0; i < servers.length; i++){
            if (servers[i] != null){
                servers[i].serviceTime--;
                if (servers[i].serviceTime == 0){
                    cusCount++;
		            events.add("Customer "+servers[i].ID+" left server #"+i);
                    waitTimes.add(tick - servers[i].arrivalTime);
                    servers[i] = null;
                }
            }
        }
    }

    public String toString(){
        StringBuilder output = new StringBuilder();

        output.append("\n--- MULTI-QUEUE STATUS ---\n");

        output.append("Time: ");
	    output.append(tick);
	    output.append("\n");

	    for (String s : events){
	        output.append(s); 
            output.append("\n");
	    }
	    events.clear();
        output.append("\n");

        for (int i = 0; i < servers.length; i++){
            output.append("Queue ");
            output.append(i);
            if (servers[i] == null){
                output.append("(empty)\n");
                continue;
            }
            output.append(" ");
            output.append(servers[i].ID);
            output.append("(");
            output.append(servers[i].serviceTime);
            output.append(" min left)");
            output.append(" ");
            for (Customer c : queues[i]){
                output.append(c.ID);
                output.append(" ");
            }
            output.append("\n");
        }

        return output.toString();
    }


    public String finalSumm(){

        float[] temp = getAvgAndMaxTime();

        float avgTime = temp[1];
        float maxTime = temp[0];
    
        StringBuilder output = new StringBuilder();
        output.append("--- MULTI-QUEUE RESULTS ---");
        output.append("\nTotal Customers Served: ");
        output.append(cusCount);
        output.append("\nAverage Wait Time: ");
        output.append(avgTime);
        output.append("\nMax Wait Time: ");
        output.append(maxTime);
        output.append("\nMax Queue Length: ");
        output.append(longestQueue);
        output.append("\nServer Utilization: ");
        output.append((480*servers.length-empty)/(servers.length*480.0)*100);
        output.append("%\nCustomers Remaining: ");
        output.append(count-1-cusCount);
        
        return output.toString();
    }
    
    private float[] getAvgAndMaxTime(){
        float[] output = new float[2];

        int max = waitTimes.get(0);

        int total = 0;

        for (Integer i : waitTimes){
            total += i;
            if (i > max){
                max = i;
            }
        }
        
        total /= (float)waitTimes.size();
        output[0] = max;
        
        output[1] = total;

        return output;
    }
    
}
