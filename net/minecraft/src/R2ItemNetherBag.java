package net.minecraft.src;

import forge.ITextureProvider;

public class R2ItemNetherBag extends Item implements ITextureProvider {
    public static final String[] colors = new String[]{"black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white"};
    protected R2ItemNetherBag(int i) {
        super(i);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
    }

    public String getKey(ItemStack item){
        if(item != null && item.itemData.hasKey("key")){
            return item.itemData.getString("key");
        }
        else{
            return "empty-empty-empty";
        }
    }

    public int getIconFromDamage(int i1) {
        return 3 + i1;
    }

    public String getItemNameIS(ItemStack itemStack1) {
        return super.getItemName() + "." + colors[itemStack1.getItemDamage()];
    }

    @Override
    public String getTextureFile() {
        return "/netherstorage/items.png";
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
        if(world.getBlockId(i, j, k) == mod_NetherStorage.r2NetherStorageBlock.blockID){
            R2TileEntityNetherStorage tileEntityNetherStorage = (R2TileEntityNetherStorage) world.getBlockTileEntity(i, j, k);
            itemstack.itemData.setString("key", tileEntityNetherStorage.getKey());
            return true;
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        entityplayer.displayGUIChest(mod_NetherStorage.getMapInvImpl(getKey(itemstack)));
        return super.onItemRightClick(itemstack, world, entityplayer);
    }
}
