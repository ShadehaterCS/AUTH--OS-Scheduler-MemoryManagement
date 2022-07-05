import java.util.ArrayList;

public class CPU {
    public static int clock = 0; // this should be incremented on every CPU cycle

    private Scheduler scheduler;
    private MMU mmu;
    private Process[] processes;

    private ArrayList<Process> availableProcesses;
    private Process currentProcess;

    public CPU(Scheduler scheduler, MMU mmu, Process[] processes) {
        this.scheduler = scheduler;
        this.mmu = mmu;
        this.processes = processes;
    }

    public void run() {
        currentProcess = null;
        availableProcesses = new ArrayList<>();
        for (Process p : processes)
            availableProcesses.add(p);

        do {
            System.out.println("\n");
            System.out.println("CLOCK: " + clock);

            tick();
            clock++;

            if (availableProcesses.size() == 0) {
                System.out.println("FINISHED SUCCESSFULLY");
                printFaultyProcesses();
                return;
            }

        }
        while (true);
    }

    public void tick() {
        /* TODO: you need to add some code here
         * Hint: this method should run once for every CPU cycle */
        /*
        Checks the initial array of processes, if the clock has reached its individual arrival time
            the process is checked if it's already running of if it can't fit into memory.
        Uses the associated list @availableProcesses to dynamically hold the READY or RUNNING processes

         */
        for (Process p : processes) {
            if (p.getPCB().getState() == ProcessState.NEW && p.getArrivalTime() <= clock) {
                if (!mmu.checkMemoryRequirements(p)) {
                    availableProcesses.remove(p); // contains() is implicit
                    continue;
                }
                if (mmu.loadProcessIntoRAM(p)) {
                    scheduler.addProcess(p);
                    p.waitInBackground();
                    System.out.println("PROCESS " + p.getPCB().getPid() + " ADDED TO MEMORY.");
                } else
                    System.out.println("PROCESS " + p.getPCB().getPid() + " CAN'T FIT INTO MEMORY RIGHT NOW.");
            }
        }

        /*
        If the scheduler has no processes in its queue, does not mean that program should exit because some may arrive
        later.
        Exits the loop and goes from the start because none of the code below it can run without a process ready.
         */
        if (scheduler.getExecutableProcesses() == 0) {
            System.out.println("Scheduler:: No process available to execute");
            return;
        }

        /*
        @Process prev is used to check against the process given by the Scheduler. If it's the same then
        only run() should be executed and the loop should end
        If the processes are different and the previous hasn't ended execution (can only happen in RoundRobin)
        then we call waitInBackGround() and

         */
        Process prev = currentProcess;
        currentProcess = scheduler.getNextProcess();

        if (prev != currentProcess && prev != null && !prev.finishedExecution())
            prev.waitInBackground();

        currentProcess.run();
        System.out.println("PROCESS EXECUTING: " + currentProcess.getPCB().getPid());


        /*
        Runs when the process has finished running in this clock cycle
        It's removed from all associated list and its memory is freed
        @functions zeroTimes() will only matter when using RoundRobin scheduler
         */
        if (currentProcess.finishedExecution()) {
            scheduler.removeProcess(currentProcess);
            mmu.clearProcessFromRAM(currentProcess);
            availableProcesses.remove(currentProcess);

            currentProcess.getPCB().setState(ProcessState.TERMINATED, CPU.clock);
            System.out.println("PROCESS " + currentProcess.getPCB().getPid() + " HAS FINISHED EXECUTION.");
            scheduler.zeroTimes();
        }


        for (Process p : processes) {
            System.out.println("PROCESS: " + p.getPCB().getPid() + " HAS STATE: " + p.getPCB().getState());
        }
    }

    protected ArrayList<Process> getAvailableProcesses() {
        return availableProcesses;
    }

    protected void printFaultyProcesses() {
        for (Process p : processes)
            if (p.getPCB().getState() == ProcessState.NEW)
                System.err.println("PROCESS: " + p.getPCB().getPid() + " was never considered because its memory requirements were bigger than the max memory block");
    }
}




