public class RecipeStep {
    private int ord;
    private int timer;
    private String desc;

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public int getOrd() {
        return ord;
    }
    public void setOrd(int ord) {
        this.ord = ord;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public RecipeStep(String desc, int timer) {
        this.desc = desc;
        this.timer = timer;
    }
}
