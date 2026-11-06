public class Simulation {
    // Set to true while building/debugging; set to false for clean statistical runs
    private static final boolean VERBOSE = true;

    private final int SIMULATION_MINUTES = 480;

    private Hades[] queues = new Hades[2];

    private int count = 1;

    public Simulation(){
        queues[0] = new MultiQueue();
        queues[1] = new SingleQueue();
    }

    public void run() {
        for (int tick = 0; tick < SIMULATION_MINUTES; tick++) {
            processDepartures(tick);
            processArrivals(tick);
            dispatchCustomers(tick);

            // Print tick-by-tick snapshot only when VERBOSE is enabled
            if (VERBOSE && tick < 30) { // Limit output to first 30 ticks during debug
                printState(tick);
            }
        }

        // Always print final summary statistics regardless of VERBOSE flag
        printSummaryStats();
    }

    private void processDepartures(int tick) {
        queues[0].processDepartures(tick);
        queues[1].processDepartures(tick);
    }

    private void processArrivals(int tick) {

        if (Math.random() < SimulationVars.ARRIVAL_CHANCE) {
            int time = (int)(Math.random()*(SimulationVars.MAX_TIME-SimulationVars.MIN_TIME+1)+SimulationVars.MIN_TIME);

            Customer tachyon = new Customer(tick, "C" + count, time);
            Customer cafe = new Customer(tick, "C" + count, time);

            count ++;

            queues[0].newCustomers(tachyon);
            queues[1].newCustomers(cafe);
        }
    }

    private void dispatchCustomers(int tick) {
        queues[0].dispatchCustomers();
        queues[1].dispatchCustomers();
    }

    private void printState(int tick) {
        System.out.println(queues[0]);
        System.out.println(queues[1]);
    }

    private void printSummaryStats() {
        System.out.println(queues[0].finalSumm());
        System.out.println(queues[1].finalSumm());
    }
}
