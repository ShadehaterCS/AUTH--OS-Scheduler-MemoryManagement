import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
/*
Tests process execution times  with FCFS scheduling algorithm and nextfit memory allocation algorithm
 */
public class CPUTestFCFS {

    final Process[] FCFSTestProcesses = {
            new Process(2, 5, 10),
            new Process(3, 4, 30),
            new Process(4, 2, 20),
            new Process(20,5, 75),
            new Process(21,1,10)};
    final int[] availableBlockSizes = {100,20,30}; // sizes in kB

    @Test
    public void FCFSProcessExecutionOrder() {
        CPU.clock = 0;
        MemoryAllocationAlgorithm algorithm = new NextFit(availableBlockSizes);
        MMU mmu = new MMU(availableBlockSizes, algorithm);
        Scheduler scheduler = new FCFS();
        CPU cpu = new CPU(scheduler, mmu, FCFSTestProcesses);
        cpu.run();

        ArrayList waitingTimes = new ArrayList<Integer>();
        ArrayList turnAroundTimes = new ArrayList<Integer>();
        ArrayList responseTimes = new ArrayList<Integer>();

        for (Process p : FCFSTestProcesses) {
            waitingTimes.add(p.getWaitingTime());
            turnAroundTimes.add(p.getTurnAroundTime());
            responseTimes.add(p.getResponseTime());
        }

        //calculates all the real times
        ArrayList realWaitingTimes = new ArrayList<Integer>();
        realWaitingTimes.add(0.0); // waiting time of process 0
        realWaitingTimes.add(4.0); // waiting time of process 1
        realWaitingTimes.add(7.0); // waiting time of process 2
        realWaitingTimes.add(0.0); // waiting time of process 3
        realWaitingTimes.add(4.0); // waiting time of process 4
        ArrayList realTurnAroundTimes = new ArrayList<Integer>();
        realTurnAroundTimes.add(5.0); //turn around time of process 0
        realTurnAroundTimes.add(8.0); //turn around time of process 1
        realTurnAroundTimes.add(9.0); //turn around time of process 2
        realTurnAroundTimes.add(5.0); //turn around time of process 3
        realTurnAroundTimes.add(5.0); //turn around time of process 4
        ArrayList realResponseTimes = new ArrayList<Integer>();
        realResponseTimes.add(0.0); // response time of process 0
        realResponseTimes.add(4.0); // response time of process 1
        realResponseTimes.add(7.0); // response time of process 2
        realResponseTimes.add(0.0); // response time of process 3
        realResponseTimes.add(4.0); // response time of process 4

        Assert.assertArrayEquals(realWaitingTimes.toArray(), waitingTimes.toArray());
        Assert.assertArrayEquals(realTurnAroundTimes.toArray(), turnAroundTimes.toArray());
        Assert.assertArrayEquals(realResponseTimes.toArray(), responseTimes.toArray());

    }
}