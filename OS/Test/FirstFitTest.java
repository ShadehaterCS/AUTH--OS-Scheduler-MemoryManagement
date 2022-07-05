import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FirstFitTest {
    int[] availableBlocks={20,10,40};
    FirstFit firstFit=new FirstFit(availableBlocks);


    @Test
    public void NotEnoughSpaceTest()
    {
        //Use MMU to test the FirstFit algorithm.
        MMU x=new MMU(availableBlocks,firstFit);

        //These Processes fit in the RAM.
        x.loadProcessIntoRAM(new Process(0,0,30));
        x.loadProcessIntoRAM(new Process(0,0,17));
        x.loadProcessIntoRAM(new Process(0,0,1));
        x.loadProcessIntoRAM(new Process(0,0,2));

        //Not enough memory for these Processes at this moment :/ .
        //There is available space for a Process <= 10.
        x.loadProcessIntoRAM(new Process(0,0,13));
        x.loadProcessIntoRAM(new Process(0,0,20));

        Integer[] result=makeResultAndOutPut(x.getCurrentlyUsedMemorySlots());

        Assert.assertArrayEquals(new Integer[]{0,16,0,19,17,17,0,19,18,19,0,19,30,59,30,69},result);

    }
    @Test
    public void unsuitableProcessesTest()
    {
        //Use MMU to test the FirstFit algorithm.
        MMU x=new MMU(availableBlocks,firstFit);

        //These two can occur by errors.
        x.loadProcessIntoRAM(new Process(0,0,-30));
        x.loadProcessIntoRAM(new Process(0,0,0));

        //Maybe some oversized Processes want to reach RAM but that is not possible.
        x.loadProcessIntoRAM(new Process(0,0,41));
        x.loadProcessIntoRAM(new Process(0,0,888));

        Integer[] result=makeResultAndOutPut(x.getCurrentlyUsedMemorySlots());

        Assert.assertArrayEquals(new Integer[]{},result);
    }
    @Test
    public void AddAndRemoveProcessesTest()
    {
        //Use MMU to test the FirstFit algorithm.
        MMU x=new MMU(availableBlocks,firstFit);

        //Fill 1st and 2nd blocks.
        Process p=new Process(0,0,18);
        x.loadProcessIntoRAM(p);
        x.loadProcessIntoRAM(new Process(0,0,10));

        //The second Process will end, and a new Process come and take its place instead of going in the third block.
        x.clearProcessFromRAM(p);
        x.loadProcessIntoRAM(new Process(0,0,20));

        Integer[] result=makeResultAndOutPut(x.getCurrentlyUsedMemorySlots());
        Assert.assertArrayEquals(new Integer[]{0,19,0,19,20,29,20,29},result);


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