package net.minecraft.src;

public class R2ContainerFilter extends Container{
    R2TileEntityNetherStorage tileEntityNetherStorage;
    public R2ContainerFilter(IInventory iInventory1, R2TileEntityNetherStorage tileEntityNetherStorage) {
        this.tileEntityNetherStorage = tileEntityNetherStorage;
        R2FilterInventoryImpl filterImpl = tileEntityNetherStorage.getFilterInvImpl();

        int i3;
        int i4;
        for(i4 = 0; i4 < 3; ++i4) {
            this.addSlot(new Slot(filterImpl, i4, 62 + i4 * 18, 17 + 1 * 18));
        }

        for(i3 = 0; i3 < 3; ++i3) {
            for(i4 = 0; i4 < 9; ++i4) {
                this.addSlot(new Slot(iInventory1, i4 + i3 * 9 + 9, 8 + i4 * 18, 84 + i3 * 18));
            }
        }

        for(i3 = 0; i3 < 9; ++i3) {
            this.addSlot(new Slot(iInventory1, i3, 8 + i3 * 18, 142));
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer1) {
        return true;
    }
}
