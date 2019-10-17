package cc.hisens.hardboiled.patient.eventbus;

//支付成功
public class PayResult {
    public boolean isSuccess;

    public PayResult(boolean isSuccess){
        this.isSuccess=isSuccess;
    }


}
