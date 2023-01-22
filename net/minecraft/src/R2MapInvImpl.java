package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class R2MapInvImpl implements IInventory{
    public ItemStack[] inventory;
    public String invName;
    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i1) {
        return inventory[i1];
    }

    @Override
    public ItemStack decrStackSize(int i1, int i2) {
        if(inventory[i1] != null) {
            ItemStack itemStack3;
            if(inventory[i1].stackSize <= i2) {
                itemStack3 = inventory[i1];
                inventory[i1] = null;
                this.onInventoryChanged();
                return itemStack3;
            } else {
                itemStack3 = inventory[i1].splitStack(i2);
                if(inventory[i1].stackSize == 0) {
                    inventory[i1] = null;
                }

                this.onInventoryChanged();
                return itemStack3;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i1, ItemStack itemStack2) {
        inventory[i1] = itemStack2;
        if(itemStack2 != null && itemStack2.stackSize > this.getInventoryStackLimit()) {
            itemStack2.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    @Override
    public String getInvName() {
        return invName;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void onInventoryChanged() {
        mod_NetherStorage.saveStorage(ModLoader.getMinecraftInstance().theWorld);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer1) {
        return true;
    }
}
