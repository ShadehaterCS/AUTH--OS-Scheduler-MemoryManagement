import java.util.ArrayList;
public class WorstFit extends MemoryAllocationAlgorithm {
    public WorstFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        /*
         * {Code block}
         * learn how much remaining memory exists in each block
         * clone availableBlocks array to know initialSize and subtract
         *  from it every memory block that occupies memory inside that block
         */
        int[] blockSizes = new int[availableBlockSizes.length];
        for (int i=0;i<availableBlockSizes.length;i++)
            blockSizes[i] = availableBlockSizes[i];

        int currentFinalAddress = 0;
        int maxSpace = -1;
        int maxBlockId = -1;

        for (int i = 0; i < availableBlockSizes.length; i++) {
            int initBlockSize = availableBlockSizes[i];
            currentFinalAddress += initBlockSize;
            for (MemorySlot m : currentlyUsedMemorySlots) { //subtract the memorySlots from the original sizes
                if (m.getBlockEnd()+1 == currentFinalAddress) //TODO need to solve 0or1 based counting
                    blockSizes[i] -= m.getUsedSize();
            }
            if (maxSpace < blockSizes[i]) { //save the block with the maximum unused space for later
                maxSpace = blockSizes[i];
                maxBlockId = i;
            }
        }
        //find the block with the biggest empty space and check if that space is enough to fit the process
        if (maxSpace < p.getMemoryRequirements() || maxSpace == -1) {
            //System.out.println("ooopsie daisy, we ain't got no spacey");
            return -1;
        }

        /*
            * Find the exact address to return
            * The start address is the sum of addresses up to and including the block that the process will be
            *   inserted into - the already used space inside that block
         */
        int blockStartAddress = 0;
        for (int i = 0; i<maxBlockId;i++)
            blockStartAddress += availableBlockSizes[i];

        int blockEndAddress = blockStartAddress+availableBlockSizes[maxBlockId]-1; //-1 because it's 1st inclusive

        //start of the block + entirety of block - the used space is the first empty address in that block :)
        address = blockStartAddress + availableBlockSizes[maxBlockId] - blockSizes[maxBlockId];

        /*
        System.out.println("Block start: "+blockStartAddress
        + " Block end: "+blockEndAddress
                + " |\tProcess start: "+address
                + " |\tProcess end: "+(address+p.getMemoryRequirements()-1));
                */
        return address;
    }
}