import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;


/*
Tests process execution times  with ROBIN ROUND scheduling algorithm and BESTFIT memory allocation algorithm
 */

public class CPUTestRobinRound {final Process[] RoundRobinTestProcesses = {
        // Process parameters are: arrivalTime, burstTime, memoryRequirements (kB)
        new Process(0, 4, 100),
        new Process(3, 5, 10),
        new Process(3, 2, 30),
        new Process(5, 3, 50)};
    final int[] availableBlockSizes = {100,20,40}; // sizes in kB


    @Test
    public void RobinRoundProcessExecutionOrder() {
        CPU.clock = 0;
        MemoryAllocationAlgorithm algorithm = new BestFit(availableBlockSizes);
        MMU mmu = new MMU(availableBlockSizes, algorithm);
        Scheduler scheduler = new RoundRobin(2);

        CPU cpu = new CPU(scheduler, mmu, RoundRobinTestProcesses);
        cpu.run();

        ArrayList waitingTimes = new ArrayList<Integer>();
        ArrayList turnAroundTimes = new ArrayList<Integer>();
        ArrayList responseTimes = new ArrayList<Integer>();
        for (Process p : RoundRobinTestProcesses) {
            waitingTimes.add(p.getWaitingTime());
            turnAroundTimes.add(p.getTurnAroundTime());
            responseTimes.add(p.getResponseTime());
        }

        //calculates all the real times
        ArrayList realWaitingTimes = new ArrayList<Integer>();
        realWaitingTimes.addAll(Arrays.asList(0.0,6.0,3.0,5.0));

        ArrayList realTurnAroundTimes = new ArrayList<Integer>();
        realTurnAroundTimes.addAll(Arrays.asList(4.0,11.0,5.0,8.0));

        ArrayList realResponseTimes = new ArrayList<Integer>();
        realResponseTimes.addAll(Arrays.asList(0.0,1.0,3.0,3.0));

        Assert.assertArrayEquals(realWaitingTimes.toArray(), waitingTimes.toArray());
        Assert.assertArrayEquals(realTurnAroundTimes.toArray(), turnAroundTimes.toArray());
        Assert.assertArrayEquals(realResponseTimes.toArray(), responseTimes.toArray());
    }

}