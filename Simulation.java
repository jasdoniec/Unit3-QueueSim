public class Simulation {
    // Set to true while building/debugging; set to false for clean statistical runs
    private static final boolean VERBOSE = true;

    private final int SIMULATION_MINUTES = 480;

    private Hades queue;

    public Simulation(Hades type){
        queue = type;
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
        queue.processDepartures(tick);
    }

    private void processArrivals(int tick) {
        queue.newCustomers(tick);
    }

    private void dispatchCustomers(int tick) {
        queue.dispatchCustomers();
    }

    private void printState(int tick) {
        
    }

    private void printSummaryStats() {

    }
}
