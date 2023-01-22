package net.minecraft.src;
import org.lwjgl.opengl.GL11;

public class R2GuiFilter extends GuiContainer{
    public R2GuiFilter(InventoryPlayer inventoryPlayer, R2TileEntityNetherStorage tileEntityNetherStorage) {
        super(new R2ContainerFilter(inventoryPlayer, tileEntityNetherStorage));
    }

    protected void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawString("Channel", 60, 6, 4210752);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float f1) {
        int i2 = this.mc.renderEngine.getTexture("/netherstorage/channel.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i2);
        int i3 = (this.width - this.xSize) / 2;
        int i4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.ySize);
    }
}
