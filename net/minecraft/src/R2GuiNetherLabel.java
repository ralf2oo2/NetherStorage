package net.minecraft.src;
import org.lwjgl.input.Keyboard;

public class R2GuiNetherLabel extends GuiScreen{
    private GuiTextField field_22111_h;
    private String key;

    public R2GuiNetherLabel(String key) {
        this.key = key;
    }
    public boolean doesGuiPauseGame() {
        return true;
    }
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, "Set"));
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
        this.field_22111_h = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, this.height / 4 - 10 + 50 + 18, 200, 20, "");
        this.field_22111_h.isFocused = true;
        this.field_22111_h.setMaxStringLength(128);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton guiButton1) {
        if(guiButton1.enabled) {
            if(guiButton1.id == 1) {
                this.mc.displayGuiScreen(null);
            } else if(guiButton1.id == 0) {
                String string2 = this.field_22111_h.getText().trim();
                if(string2.length() > 0)
                {
                    mod_NetherStorage.netherLabels.put(key, string2);
                    this.mc.displayGuiScreen(null);
                }
            }
        }
    }

    protected void keyTyped(char c1, int i2) {
        this.field_22111_h.textboxKeyTyped(c1, i2);
        if(c1 == 13) {
            this.actionPerformed((GuiButton)this.controlList.get(0));
        }

        ((GuiButton)this.controlList.get(0)).enabled = this.field_22111_h.getText().length() > 0;
    }

    protected void mouseClicked(int i1, int i2, int i3) {
        super.mouseClicked(i1, i2, i3);
        this.field_22111_h.mouseClicked(i1, i2, i3);
    }

    public void drawScreen(int i1, int i2, float f3) {
        this.drawWorldBackground(0);
        this.drawCenteredString(this.fontRenderer, "Set label", this.width / 2, this.height / 4 - 60 + 20, 0xFFFFFF);
        this.field_22111_h.drawTextBox();
        super.drawScreen(i1, i2, f3);
    }
}
