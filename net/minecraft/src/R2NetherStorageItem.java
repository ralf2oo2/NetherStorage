package net.minecraft.src;

import forge.ITextureProvider;

public class R2NetherStorageItem extends Item implements ITextureProvider {
    public static final String[] items = new String[]{"netherLabel", "netherFreq", "netherCore"};
    protected R2NetherStorageItem(int i1) {
        super(i1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public String getItemNameIS(ItemStack itemStack1) {
        return super.getItemName() + "." + items[itemStack1.getItemDamage()];
    }

    @Override
    public int getIconFromDamage(int i1) {
        switch (i1){
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public String getTextureFile() {
        return "/netherstorage/items.png";
    }
}
