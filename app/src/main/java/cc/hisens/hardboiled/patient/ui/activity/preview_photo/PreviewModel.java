package cc.hisens.hardboiled.patient.ui.activity.preview_photo;

public class PreviewModel {
    private boolean isClick=false;
    private String imagePath;



    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
