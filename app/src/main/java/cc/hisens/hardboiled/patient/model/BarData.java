package cc.hisens.hardboiled.patient.model;

public  class BarData {
    private int count;
    private String bottomText;

    public BarData(int count, String bottomText) {
        this.count = count;
        this.bottomText = bottomText;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBottomText() {
        return bottomText == null ? "" : bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }
}