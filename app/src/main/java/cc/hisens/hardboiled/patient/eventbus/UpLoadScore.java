package cc.hisens.hardboiled.patient.eventbus;

import cc.hisens.hardboiled.patient.model.UploadEHS;
import cc.hisens.hardboiled.patient.model.UploadIIEF;

//上传分数的类
public class UpLoadScore {
  public UploadIIEF iief5Score;
  public UploadEHS ehsScore;



  public UpLoadScore(UploadIIEF score){
      this.iief5Score=score;
  }


    public UpLoadScore(UploadEHS score){
        this.ehsScore=score;
    }


}
