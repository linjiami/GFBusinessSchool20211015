package com.gfbusinessschool.bean;

import java.util.List;

public class BaseListData {

    /**
     * code : 200
     * msg : success
     * data : {"records":[{"id":"1397125427089616898","userId":"1116","headImgUrl":"http://test.imcfc.com/20210326/798ed913a12648498b0d3cac31fb8849.png","userName":"周德伟","title":"分享标题1","content":"分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试","type":"pdf","powerIds":null,"fileName":"分享测试.pdf","fileUrl":"http://test.imcfc.com/20210526/e2e27888a047416db696d3a38f3adea9.pdf","status":"1","studyAmount":58,"likeAmount":10,"createDate":"2021-05-25 17:40:27","companyId":"1"},{"id":"1397128881606447105","userId":"1116","headImgUrl":"http://test.imcfc.com/20210326/798ed913a12648498b0d3cac31fb8849.png","userName":"周德伟","title":"分享标题2word","content":"分享内容测试mp4,分享内容测试mp4分享内容测试mp4分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试","type":"doc","powerIds":null,"fileName":"分享测试.doc","fileUrl":"http://test.imcfc.com/20210526/6b76007ffffd44d191aa4e604ded4333.docx","status":"1","studyAmount":23,"likeAmount":1,"createDate":"2021-05-25 17:54:11","companyId":"1"},{"id":"1397451056340746242","userId":"1116","headImgUrl":"http://test.imcfc.com/20210326/798ed913a12648498b0d3cac31fb8849.png","userName":"周德伟","title":"文件标题3","content":"文件内容3","type":"pptx","powerIds":null,"fileName":"文件标题3.pptx","fileUrl":"http://test.imcfc.com/20210526/3da8eb2cea9a4a0ca18695f51f497b4e.pptx","status":"1","studyAmount":11,"likeAmount":0,"createDate":"2021-05-26 15:14:29","companyId":"1"},{"id":"1397458626627452930","userId":"1116","headImgUrl":"http://test.imcfc.com/20210326/798ed913a12648498b0d3cac31fb8849.png","userName":"周德伟","title":"文件标题4","content":"文件内容4","type":"jpg","powerIds":null,"fileName":"文件标题4.jpg","fileUrl":"http://test.imcfc.com/20210526/1f83350502184d84bf34d2ab069d506c.jpg","status":"1","studyAmount":12,"likeAmount":0,"createDate":"2021-05-26 15:44:34","companyId":"1"},{"id":"1397460070059425794","userId":"1116","headImgUrl":"http://test.imcfc.com/20210326/798ed913a12648498b0d3cac31fb8849.png","userName":"周德伟","title":"文件标题5","content":"文件内容5","type":"mp4","powerIds":null,"fileName":"文件名称5.mp4","fileUrl":"http://test.imcfc.com/20210526/b1d3b9a9de434ee3b24c2a10ec17061b.mp4","status":"1","studyAmount":11,"likeAmount":0,"createDate":"2021-05-26 15:50:18","companyId":"1"},{"id":"1397474546146033666","userId":"1115","headImgUrl":"http://test.imcfc.com/20210409/6612a24cf7a446ee86d32233fbdc06aa.jpg","userName":"林家密","title":"截屏视频测试","content":"急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急","type":null,"powerIds":null,"fileName":null,"fileUrl":"http://test.imcfc.com/156004500121622018782811.mp4","status":"1","studyAmount":20,"likeAmount":21,"createDate":"2021-05-26 16:47:50","companyId":"1"},{"id":"1397486988502712321","userId":"1115","headImgUrl":"http://test.imcfc.com/20210409/6612a24cf7a446ee86d32233fbdc06aa.jpg","userName":"林家密","title":"心心相印","content":"心心相印图片测试","type":null,"powerIds":null,"fileName":null,"fileUrl":"http://test.imcfc.com/156004500121622021810889.jpg","status":"1","studyAmount":0,"likeAmount":0,"createDate":"2021-05-26 17:37:16","companyId":"1"},{"id":"1397754740970807298","userId":"1115","headImgUrl":"http://test.imcfc.com/20210409/6612a24cf7a446ee86d32233fbdc06aa.jpg","userName":"林家密","title":"Word测试","content":"文件上传测试，Word","type":null,"powerIds":null,"fileName":null,"fileUrl":"http://test.imcfc.com/156004500121622085594366.doc","status":"1","studyAmount":3,"likeAmount":0,"createDate":"2021-05-27 11:21:13","companyId":"1"},{"id":"1397794511873060865","userId":"1115","headImgUrl":"http://test.imcfc.com/20210409/6612a24cf7a446ee86d32233fbdc06aa.jpg","userName":"林家密","title":"Word测试2","content":"Word文档分享内容，不仅仅斤斤计较斤斤计较急急急急急急急急急急急急急急急急急急急急急","type":null,"powerIds":null,"fileName":null,"fileUrl":"http://test.imcfc.com/156004500121622095106674.doc","status":"1","studyAmount":1,"likeAmount":0,"createDate":"2021-05-27 13:59:16","companyId":"1"}],"total":9,"size":15,"current":1,"orders":[],"searchCount":true,"pages":1}
     * dataCode : 200
     * time : 2021-05-27 16:06:07
     */

    private String code;
    private String msg;
    private DataBean data;
    private String dataCode;
    private String time;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static class DataBean {
        /**
         * records : [{"id":"1397125427089616898","userId":"1116","headImgUrl":"http://test.imcfc.com/20210326/798ed913a12648498b0d3cac31fb8849.png","userName":"周德伟","title":"分享标题1","content":"分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试","type":"pdf","powerIds":null,"fileName":"分享测试.pdf","fileUrl":"http://test.imcfc.com/20210526/e2e27888a047416db696d3a38f3adea9.pdf","status":"1","studyAmount":58,"likeAmount":10,"createDate":"2021-05-25 17:40:27","companyId":"1"},{"id":"1397128881606447105","userId":"1116","headImgUrl":"http://test.imcfc.com/20210326/798ed913a12648498b0d3cac31fb8849.png","userName":"周德伟","title":"分享标题2word","content":"分享内容测试mp4,分享内容测试mp4分享内容测试mp4分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试分享内容测试","type":"doc","powerIds":null,"fileName":"分享测试.doc","fileUrl":"http://test.imcfc.com/20210526/6b76007ffffd44d191aa4e604ded4333.docx","status":"1","studyAmount":23,"likeAmount":1,"createDate":"2021-05-25 17:54:11","companyId":"1"},{"id":"1397451056340746242","userId":"1116","headImgUrl":"http://test.imcfc.com/20210326/798ed913a12648498b0d3cac31fb8849.png","userName":"周德伟","title":"文件标题3","content":"文件内容3","type":"pptx","powerIds":null,"fileName":"文件标题3.pptx","fileUrl":"http://test.imcfc.com/20210526/3da8eb2cea9a4a0ca18695f51f497b4e.pptx","status":"1","studyAmount":11,"likeAmount":0,"createDate":"2021-05-26 15:14:29","companyId":"1"},{"id":"1397458626627452930","userId":"1116","headImgUrl":"http://test.imcfc.com/20210326/798ed913a12648498b0d3cac31fb8849.png","userName":"周德伟","title":"文件标题4","content":"文件内容4","type":"jpg","powerIds":null,"fileName":"文件标题4.jpg","fileUrl":"http://test.imcfc.com/20210526/1f83350502184d84bf34d2ab069d506c.jpg","status":"1","studyAmount":12,"likeAmount":0,"createDate":"2021-05-26 15:44:34","companyId":"1"},{"id":"1397460070059425794","userId":"1116","headImgUrl":"http://test.imcfc.com/20210326/798ed913a12648498b0d3cac31fb8849.png","userName":"周德伟","title":"文件标题5","content":"文件内容5","type":"mp4","powerIds":null,"fileName":"文件名称5.mp4","fileUrl":"http://test.imcfc.com/20210526/b1d3b9a9de434ee3b24c2a10ec17061b.mp4","status":"1","studyAmount":11,"likeAmount":0,"createDate":"2021-05-26 15:50:18","companyId":"1"},{"id":"1397474546146033666","userId":"1115","headImgUrl":"http://test.imcfc.com/20210409/6612a24cf7a446ee86d32233fbdc06aa.jpg","userName":"林家密","title":"截屏视频测试","content":"急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急急","type":null,"powerIds":null,"fileName":null,"fileUrl":"http://test.imcfc.com/156004500121622018782811.mp4","status":"1","studyAmount":20,"likeAmount":21,"createDate":"2021-05-26 16:47:50","companyId":"1"},{"id":"1397486988502712321","userId":"1115","headImgUrl":"http://test.imcfc.com/20210409/6612a24cf7a446ee86d32233fbdc06aa.jpg","userName":"林家密","title":"心心相印","content":"心心相印图片测试","type":null,"powerIds":null,"fileName":null,"fileUrl":"http://test.imcfc.com/156004500121622021810889.jpg","status":"1","studyAmount":0,"likeAmount":0,"createDate":"2021-05-26 17:37:16","companyId":"1"},{"id":"1397754740970807298","userId":"1115","headImgUrl":"http://test.imcfc.com/20210409/6612a24cf7a446ee86d32233fbdc06aa.jpg","userName":"林家密","title":"Word测试","content":"文件上传测试，Word","type":null,"powerIds":null,"fileName":null,"fileUrl":"http://test.imcfc.com/156004500121622085594366.doc","status":"1","studyAmount":3,"likeAmount":0,"createDate":"2021-05-27 11:21:13","companyId":"1"},{"id":"1397794511873060865","userId":"1115","headImgUrl":"http://test.imcfc.com/20210409/6612a24cf7a446ee86d32233fbdc06aa.jpg","userName":"林家密","title":"Word测试2","content":"Word文档分享内容，不仅仅斤斤计较斤斤计较急急急急急急急急急急急急急急急急急急急急急","type":null,"powerIds":null,"fileName":null,"fileUrl":"http://test.imcfc.com/156004500121622095106674.doc","status":"1","studyAmount":1,"likeAmount":0,"createDate":"2021-05-27 13:59:16","companyId":"1"}]
         * total : 9
         * size : 15
         * current : 1
         * orders : []
         * searchCount : true
         * pages : 1
         */

        private int total;
        private int size;
        private int current;
        private boolean searchCount;
        private int pages;
        private List<Object> records;
        private List<?> orders;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public boolean isSearchCount() {
            return searchCount;
        }

        public void setSearchCount(boolean searchCount) {
            this.searchCount = searchCount;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<Object> getRecords() {
            return records;
        }

        public void setRecords(List<Object> records) {
            this.records = records;
        }

        public List<?> getOrders() {
            return orders;
        }

        public void setOrders(List<?> orders) {
            this.orders = orders;
        }
    }
}
