public class Process {
    private final ProcessControlBlock pcb;
    private final int arrivalTime;
    private final int burstTime;
    private final int memoryRequirements;
    private int remainingBurstTime;

    private MemorySlot memorySlot;

    public Process(int arrivalTime, int burstTime, int memoryRequirements) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingBurstTime = burstTime;
        this.memoryRequirements = memoryRequirements;
        this.pcb = new ProcessControlBlock();
    }
    
    public ProcessControlBlock getPCB() {
        return this.pcb;
    }

    protected int getArrivaltime(){
        return arrivalTime;
    }

    protected int getBurstTime(){
        return burstTime;
    }

    protected boolean finishedExecution(){
        return remainingBurstTime == 0;
    }

    public void run() {
        /* TODO: you need to add some code here
         * Hint: this should run every time a process starts running */
        if (pcb.getState() == ProcessState.READY) {
            pcb.setState(ProcessState.RUNNING, CPU.clock);
        }
        remainingBurstTime--; //reduces the remaining burst time
    }

    public void waitInBackground() {
        /* TODO: you need to add some code here
         * Hint: this should run every time a process stops running */
        pcb.setState(ProcessState.READY, CPU.clock);
    }

    // Waiting time = Turn Around time - Burst time
    public double getWaitingTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        return getTurnAroundTime() - burstTime ;
    }

    // Response time=Time of the first insertion in the cpu -  Arrival time
    public double getResponseTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        return pcb.getStartTimes().get(0) -  arrivalTime ;
    }

    // Turnaround time = Completion time - Arrival time
    public double getTurnAroundTime() {
        /* TODO: you need to add some code here
         * and change the return value */
        return pcb.getStopTimes().get(pcb.getStopTimes().size() -1) - arrivalTime + 1;
    }

    protected int getMemoryRequirements(){
        return memoryRequirements;
    }
    protected int getArrivalTime(){return arrivalTime;}

    protected void setMemorySlot(MemorySlot m) { memorySlot = m;}
    protected MemorySlot getMemorySlot() {return memorySlot;}

}
