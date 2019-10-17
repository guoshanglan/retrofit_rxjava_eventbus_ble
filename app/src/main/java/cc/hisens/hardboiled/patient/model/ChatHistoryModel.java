package cc.hisens.hardboiled.patient.model;

import java.util.List;

import cc.hisens.hardboiled.patient.websocket.MessageModel;


//聊天记录的model

public class ChatHistoryModel {


    /**
     * result : 0
     * message : 请求成功
     * datas : {"list":[{"id":408,"from":50003,"to":511,"type":1,"create_time":1564815759,"accessory":0,"text":"图片","event":0,"url":"http://10.0.0.200:8083/group/hisens_main/messages/other/20190803/15/02/3/198579061100b00561cecd178333d0f9.JPEG?download=0","thumb_url":"http://10.0.0.200:8083/group/hisens_main/messages/other/20190803/15/02/3/2817097babfcf74c5d4c998311593c0f.JPEG?download=0","height":4608,"width":3456,"duration":0},{"id":407,"from":50003,"to":511,"type":1,"create_time":1564815027,"accessory":0,"text":"图片","event":0,"url":"http://10.0.0.200:8083/group/hisens_main/messages/images/20190803/14/50/3/d445429ce439e8f3c6c9a2b986f00c64.jpg?download=0","thumb_url":"http://10.0.0.200:8083/group/hisens_main/messages/images/20190803/14/50/3/a75723019f03e2347efb58e9bd8909b8.jpg?download=0","height":4608,"width":3456,"duration":0},{"id":406,"from":50003,"to":511,"type":1,"create_time":1564814895,"accessory":0,"text":"图片","event":0,"url":"http://10.0.0.200:8083/group/hisens_main/messages/images/20190803/14/48/3/cd0d3070c521d3bce229c50241c3dee8.jpg?download=0","thumb_url":"http://10.0.0.200:8083/group/hisens_main/messages/images/20190803/14/48/3/355b9410de3cde0c468de67c66e5ade1.jpg?download=0","height":4608,"width":3456,"duration":0}],"total":3}
     */

    private int result;
    private String message;
    private DatasBean datas;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DatasBean getDatas() {
        return datas;
    }

    public void setDatas(DatasBean datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * list : [{"id":408,"from":50003,"to":511,"type":1,"create_time":1564815759,"accessory":0,"text":"图片","event":0,"url":"http://10.0.0.200:8083/group/hisens_main/messages/other/20190803/15/02/3/198579061100b00561cecd178333d0f9.JPEG?download=0","thumb_url":"http://10.0.0.200:8083/group/hisens_main/messages/other/20190803/15/02/3/2817097babfcf74c5d4c998311593c0f.JPEG?download=0","height":4608,"width":3456,"duration":0},{"id":407,"from":50003,"to":511,"type":1,"create_time":1564815027,"accessory":0,"text":"图片","event":0,"url":"http://10.0.0.200:8083/group/hisens_main/messages/images/20190803/14/50/3/d445429ce439e8f3c6c9a2b986f00c64.jpg?download=0","thumb_url":"http://10.0.0.200:8083/group/hisens_main/messages/images/20190803/14/50/3/a75723019f03e2347efb58e9bd8909b8.jpg?download=0","height":4608,"width":3456,"duration":0},{"id":406,"from":50003,"to":511,"type":1,"create_time":1564814895,"accessory":0,"text":"图片","event":0,"url":"http://10.0.0.200:8083/group/hisens_main/messages/images/20190803/14/48/3/cd0d3070c521d3bce229c50241c3dee8.jpg?download=0","thumb_url":"http://10.0.0.200:8083/group/hisens_main/messages/images/20190803/14/48/3/355b9410de3cde0c468de67c66e5ade1.jpg?download=0","height":4608,"width":3456,"duration":0}]
         * total : 3
         */

        private int total;
        private List<MessageModel> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<MessageModel> getList() {
            return list;
        }

        public void setList(List<MessageModel> list) {
            this.list = list;
        }



    }
}
