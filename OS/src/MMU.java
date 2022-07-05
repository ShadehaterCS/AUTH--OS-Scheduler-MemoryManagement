import java.lang.reflect.Array;
import java.util.ArrayList;

public class MMU {
    private final int[] availableBlockSizes;
    private MemoryAllocationAlgorithm algorithm;
    private ArrayList<MemorySlot> currentlyUsedMemorySlots;

    public MMU(int[] availableBlockSizes, MemoryAllocationAlgorithm algorithm) {
        this.availableBlockSizes = availableBlockSizes;
        this.algorithm = algorithm;
        this.currentlyUsedMemorySlots = new ArrayList<MemorySlot>();
    }

    /*
        * @use guarantees that every process to be run will fit into memory at some point in time
        * @returns an ArrayList of processes which @int MemoryRequirements is less than the maxBlockSize of memory
        * will return empty ArrayList if none can fit
    */
    protected ArrayList<Process> filterProcessesForMemory(Process[] processes) {
        int maxBlockSize = 0;
        ArrayList<Process> canFIt = new ArrayList<>();

        for (int i = 0; i < availableBlockSizes.length; i++)
            maxBlockSize = Math.max(maxBlockSize, availableBlockSizes[i]);
        for (int i = 0; i < processes.length; i++)
            if (processes[i].getMemoryRequirements() > maxBlockSize)
                canFIt.add(processes[i]);
        return canFIt;
    }
    //@returns true if process can possibly fit in memory at some point
    protected boolean checkMemoryRequirements(Process p) {
        int maxBlockSize = 0;
        for (int i = 0; i < availableBlockSizes.length; i++)
            maxBlockSize = Math.max(maxBlockSize, availableBlockSizes[i]);
        return p.getMemoryRequirements() <= maxBlockSize;
    }
    /*
        @method
        Gets the address to assign the process in from the given algorithm
        goes through the blockAddresses array where bounds are calculated for every given memory block.
        Once the address falls between those bounds it creates a new memory slot, adds it to the list, assigns it to the
        process and sorts the memory slots list.
        @return true if a MemorySlot was successfully created
    */
    public boolean loadProcessIntoRAM(Process p) {
        boolean fit = false;
        /* TODO: you need to add some code here
         * Hint: this should return true if the process was able to fit into memory
         * and false if not */

        //Calculate the starting address of every block.
        ArrayList<Integer> blockAddresses = new ArrayList<>();
        for (int i = 0; i < availableBlockSizes.length; i++) {
            int sum = 0;
            for (int j = 0; j < i; j++) {
                sum += availableBlockSizes[j];
            }
            blockAddresses.add(sum);
        }

        //find max block's size.
        int max = -1;
        for (int i = 0; i < availableBlockSizes.length; i++)
            max = Math.max(max, availableBlockSizes[i]);

        //edge cases for memory requirements.
        if (p.getMemoryRequirements() <= 0 || p.getMemoryRequirements() > max)
            return fit;

        //Get the address to save the Process.
        //Then find the block addresses that contain this address and create the new memory slot for this Process.
        int address = algorithm.fitProcess(p, currentlyUsedMemorySlots);
        if (address != -1) {
            fit = true;
            for (int i = 0; i < blockAddresses.size(); i++) {
                if (address >= blockAddresses.get(i) && address <= (blockAddresses.get(i) + availableBlockSizes[i]) - 1) {
                    MemorySlot temp = new MemorySlot(address, address + p.getMemoryRequirements() - 1, blockAddresses.get(i), blockAddresses.get(i) + availableBlockSizes[i] - 1);
                    p.setMemorySlot(temp);
                    currentlyUsedMemorySlots.add(temp);
                        currentlyUsedMemorySlots.sort((m1, m2) -> m1.getStart() - m2.getStart());
                    break;
                }
            }
        }
        return fit;
    }

    /*
     * Will the delete the MemorySlot associated with the process
     * If the process shared its memory block with others and was not the last in that block
     * then it will call the defrag function before removing the process
     */
    protected void clearProcessFromRAM(Process p) {
        MemorySlot mSlot = p.getMemorySlot();
        int index = currentlyUsedMemorySlots.indexOf(mSlot);
        if (index + 1 < currentlyUsedMemorySlots.size() && currentlyUsedMemorySlots.get(index + 1).getBlockStart() == mSlot.getBlockStart())
            defragMemory(mSlot);
        currentlyUsedMemorySlots.remove(mSlot);
    }

    /*
     * Defragments the memory so a process can been deleted.
     * If the Memory Slot of that process was behind others in the same block then it slides all the
     * slots in front of it towards the back, so the memory can remain continuous and without empty spaces between
     * assigned memory slots
     */
    private void defragMemory(MemorySlot toBeDeleted) {
        int blockStartAddress = toBeDeleted.getBlockStart();
        MemorySlot temp;
        for (int i = currentlyUsedMemorySlots.indexOf(toBeDeleted) + 1; i < currentlyUsedMemorySlots.size(); i++) {
            temp = currentlyUsedMemorySlots.get(i);
            if (temp.getBlockStart() == blockStartAddress) {
                temp.setStart(temp.getStart() - toBeDeleted.getUsedSize());
                temp.setEnd(temp.getEnd() - toBeDeleted.getUsedSize());
            } else
                break;
        }
    }

    protected ArrayList<MemorySlot> getCurrentlyUsedMemorySlots() {
        return currentlyUsedMemorySlots;
    }
}

