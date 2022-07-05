import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class NextFitTest {
    int[] availableBlocks={20,10,40};
    NextFit nextFit=new NextFit(availableBlocks);
    MMU x=new MMU(availableBlocks,nextFit);


    @Test
    public void rollAroundTest()
    {
        //Use MMU to test the NextFit algorithm.
        MMU x=new MMU(availableBlocks,nextFit);

        x.loadProcessIntoRAM(new Process(0,0,15));
        x.loadProcessIntoRAM(new Process(0,0,8));
        x.loadProcessIntoRAM(new Process(0,0,36));

        //There is a slot sized 4kB left in the last slot of RAM.
        //The algorithm will not stop on the last slot of the RAM and it will continue from the start
        // untill it reach the block of the last address added.
        x.loadProcessIntoRAM(new Process(0,0,5));

        Integer[] result=makeResultAndOutPut(x.getCurrentlyUsedMemorySlots());
        Assert.assertArrayEquals(new Integer[]{0,14,0,19,15,19,0,19,20,27,20,29,30,65,30,69},result);

    }

    @Test
    public void NotEnoughSpaceTest()
    {
        //Use MMU to test the NextFit algorithm.
        MMU x=new MMU(availableBlocks,nextFit);

        x.loadProcessIntoRAM(new Process(0,0,15));
        x.loadProcessIntoRAM(new Process(0,0,6));
        x.loadProcessIntoRAM(new Process(0,0,30));
        x.loadProcessIntoRAM(new Process(0,0,1));
        //There is not a 10kB slob available at this moment so the Process will be rejected.
        x.loadProcessIntoRAM(new Process(0,0,10));

        Integer[] result=makeResultAndOutPut(x.getCurrentlyUsedMemorySlots());
        Assert.assertArrayEquals(new Integer[]{0,14,0,19,20,25,20,29,30,59,30,69,60,60,30,69},result);

    }

    @Test
    public void unsuitableProcessesTest()
    {
        //Use MMU to test the NextFit algorithm.
        MMU x=new MMU(availableBlocks,nextFit);

        //This Process can be saved in the third block.
        x.loadProcessIntoRAM(new Process(0,0,21));

        //These wont be saved.
        x.loadProcessIntoRAM(new Process(0,0,42));
        x.loadProcessIntoRAM(new Process(0,0,0));
        x.loadProcessIntoRAM(new Process(0,0,-1));

        Integer[] result=makeResultAndOutPut(x.getCurrentlyUsedMemorySlots());

        Assert.assertArrayEquals(new Integer[]{30,50,30,69},result);

    }
    @Test
    public void AddAndRemoveProcessesTest()
    {
        //This Algorithm is very important

        //Use MMU to test the NextFit algorithm.
        MMU x=new MMU(availableBlocks,nextFit);

        //Three Processes come normally.
        x.loadProcessIntoRAM(new Process(0,0,20));
        x.loadProcessIntoRAM(new Process(0,0,10));
        Process p1=new Process(0,0,15);
        x.loadProcessIntoRAM(p1);

        //This Process is saved in slots 45-59.
        x.loadProcessIntoRAM(new Process(0,0,25));
        //With this deletion the above Process move to 30-54.
        x.clearProcessFromRAM(p1);
        //And then there is enough space for this Process too at: 55-64.
        x.loadProcessIntoRAM(new Process(0,0,10));

        Integer[] result=makeResultAndOutPut(x.getCurrentlyUsedMemorySlots());

        Assert.assertArrayEquals(new Integer[]{0,19,0,19,20,29,20,29,30,54,30,69,55,64,30,69},result);



    }

    public Integer[] makeResultAndOutPut(ArrayList<MemorySlot> m)
    {
        ArrayList<Integer> result=new ArrayList<>();
        for(MemorySlot memorySlot:m)
        {
            result.add(memorySlot.getStart());
            result.add(memorySlot.getEnd());
            result.add(memorySlot.getBlockStart());
            result.add(memorySlot.getBlockEnd());
            System.out.println("Start address:"+memorySlot.getStart()+"  End address:"
                    +memorySlot.getEnd()+"  || Start block:"+memorySlot.getBlockStart()
                    +" End block:"+memorySlot.getBlockEnd());
        }
        Integer[] array=new Integer[m.size()];
        return result.toArray(array);
    }


}