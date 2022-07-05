import java.util.ArrayList;
public class BestFit extends MemoryAllocationAlgorithm {
//TODO remove Arrays import
    public BestFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;
        /* TODO: you need to add some code here
         * Hint: this should return the memory address where the process was
         * loaded into if the process fits. In case the process doesn't fit, it
         * should return -1. */

        /*
           * @use learn how much remaining memory exists in each block
           * @step clone availableBlocks array to know the initialSize of every block and
           * subtract from it every memory slot that occupies memory inside that block
        */
        int[] blockSizes = new int[availableBlockSizes.length];
        for (int i=0;i<availableBlockSizes.length;i++)
            blockSizes[i] = availableBlockSizes[i];
        int currentFinalAddress = 0;

        for (int i = 0; i < availableBlockSizes.length; i++) {
            int initBlockSize = availableBlockSizes[i];
            currentFinalAddress += initBlockSize;
            for (MemorySlot m : currentlyUsedMemorySlots) {
                if (m.getBlockEnd() + 1 == currentFinalAddress) //+1 because we start from 0
                    blockSizes[i] -= m.getUsedSize();
            }
        }

        //Find the block with the minimum space that can hold the process
        int minBlockSize = Integer.MAX_VALUE;
        int minBlockId=-1;
        for (int i = 0; i < blockSizes.length; i++){
            if (blockSizes[i] <= minBlockSize && blockSizes[i] >= p.getMemoryRequirements()) {
                minBlockSize = blockSizes[i];
                minBlockId = i;
            }
        }
        if (minBlockId == -1) //value unchanged, no block has enough memory
            return -1;

        //Find the starting address of the block the process will be inserted in
        int blockStartAddress = 0;
        for (int i = 0; i<minBlockId;i++)
            blockStartAddress += availableBlockSizes[i];
        int blockEndAddress = blockStartAddress + availableBlockSizes[minBlockId]-1; //-1 because it's 1st inclusive

        //start of the block + entirety of block - the used space = the first empty address in that block :)
        address = blockStartAddress + availableBlockSizes[minBlockId] - blockSizes[minBlockId];
        /*
        System.out.println("Block start: "+blockStartAddress
        + " Block end: "+blockEndAddress
                + " |\tProcess start: "+address
                + " |\tProcess end: "+(address+p.getMemoryRequirements()-1));
                */
        return address;
    }
}
