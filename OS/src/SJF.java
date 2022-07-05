public class SJF extends Scheduler {
    private Process lastExecutedProcess;
    public SJF() {
        /* TODO: you _may_ need to add some code here */
    }

    /*
    Adds a process to the list
    If the last one has finished then the list is sorted by burstTime so the first one is the one that should
    be returned to the CPU
     */
    public void addProcess(Process p){
        /* TODO: you need to add some code here */
        processes.add(p);
        if (lastExecutedProcess != null && lastExecutedProcess.finishedExecution())
            processes.sort((p1,p2) -> p1.getBurstTime() - p2.getBurstTime());
        executableProcesses++;
    }
    
    public Process getNextProcess() {
        /* TODO: you need to add some code here
         * and change the return value */
        lastExecutedProcess = processes.get(0);
        return processes.get(0);
    }
}
