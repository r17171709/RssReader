package com.renyu.rssreader.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */
public class WXBean {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * app_msg_ext_info : {"author":"DthFish","content":"","content_url":"/s?timestamp=1498533925&amp;src=3&amp;ver=1&amp;signature=Eq6DPvkuGJi*G5spckebe8e5*1gHkmuI2jYktaOoojaG8xiIWBqGWBozTT9020mMQbwMc06UV1ZOpIsSOLOgTKrQyWD-BPl*uyfrc6V7uqahBBVJGpxGuXlx1DOZPu88D3Ca*jWriLk5ojjbHNXG7jDq5Z4L4NIFMbQEpjrJgqo=","copyright_stat":100,"cover":"http: //mmbiz.qpic.cn/mmbiz_jpg/v1LbPPWiaSt6JfiaJE3SiaqAUrnickzcJNqvW1iax6eZnM40Pxa9fByWRXZRSac1rfhfxgvJW91heoOVN0hP0clJTsw/0?wx_fmt=jpeg","digest":"自定义View又到了新的高度。","fileid":502756497,"is_multi":0,"multi_app_msg_item_list":[],"source_url":"","subtype":9,"title":"高级自定义View，打造华丽的ViewPagerIndicator效果"}
         * comm_msg_info : {"content":"","datetime":1498521624,"fakeid":"3093276160","id":1000000253,"status":2,"type":49}
         */

        private AppMsgExtInfoBean app_msg_ext_info;
        private CommMsgInfoBean comm_msg_info;

        public AppMsgExtInfoBean getApp_msg_ext_info() {
            return app_msg_ext_info;
        }

        public void setApp_msg_ext_info(AppMsgExtInfoBean app_msg_ext_info) {
            this.app_msg_ext_info = app_msg_ext_info;
        }

        public CommMsgInfoBean getComm_msg_info() {
            return comm_msg_info;
        }

        public void setComm_msg_info(CommMsgInfoBean comm_msg_info) {
            this.comm_msg_info = comm_msg_info;
        }

        public static class AppMsgExtInfoBean {
            /**
             * author : DthFish
             * content :
             * content_url : /s?timestamp=1498533925&amp;src=3&amp;ver=1&amp;signature=Eq6DPvkuGJi*G5spckebe8e5*1gHkmuI2jYktaOoojaG8xiIWBqGWBozTT9020mMQbwMc06UV1ZOpIsSOLOgTKrQyWD-BPl*uyfrc6V7uqahBBVJGpxGuXlx1DOZPu88D3Ca*jWriLk5ojjbHNXG7jDq5Z4L4NIFMbQEpjrJgqo=
             * copyright_stat : 100
             * cover : http: //mmbiz.qpic.cn/mmbiz_jpg/v1LbPPWiaSt6JfiaJE3SiaqAUrnickzcJNqvW1iax6eZnM40Pxa9fByWRXZRSac1rfhfxgvJW91heoOVN0hP0clJTsw/0?wx_fmt=jpeg
             * digest : 自定义View又到了新的高度。
             * fileid : 502756497
             * is_multi : 0
             * multi_app_msg_item_list : []
             * source_url :
             * subtype : 9
             * title : 高级自定义View，打造华丽的ViewPagerIndicator效果
             */

            private String author;
            private String content;
            private String content_url;
            private int copyright_stat;
            private String cover;
            private String digest;
            private int fileid;
            private int is_multi;
            private String source_url;
            private int subtype;
            private String title;
            private List<?> multi_app_msg_item_list;

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getContent_url() {
                return content_url;
            }

            public void setContent_url(String content_url) {
                this.content_url = content_url;
            }

            public int getCopyright_stat() {
                return copyright_stat;
            }

            public void setCopyright_stat(int copyright_stat) {
                this.copyright_stat = copyright_stat;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getDigest() {
                return digest;
            }

            public void setDigest(String digest) {
                this.digest = digest;
            }

            public int getFileid() {
                return fileid;
            }

            public void setFileid(int fileid) {
                this.fileid = fileid;
            }

            public int getIs_multi() {
                return is_multi;
            }

            public void setIs_multi(int is_multi) {
                this.is_multi = is_multi;
            }

            public String getSource_url() {
                return source_url;
            }

            public void setSource_url(String source_url) {
                this.source_url = source_url;
            }

            public int getSubtype() {
                return subtype;
            }

            public void setSubtype(int subtype) {
                this.subtype = subtype;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<?> getMulti_app_msg_item_list() {
                return multi_app_msg_item_list;
            }

            public void setMulti_app_msg_item_list(List<?> multi_app_msg_item_list) {
                this.multi_app_msg_item_list = multi_app_msg_item_list;
            }
        }

        public static class CommMsgInfoBean {
            /**
             * content :
             * datetime : 1498521624
             * fakeid : 3093276160
             * id : 1000000253
             * status : 2
             * type : 49
             */

            private String content;
            private int datetime;
            private String fakeid;
            private int id;
            private int status;
            private int type;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getDatetime() {
                return datetime;
            }

            public void setDatetime(int datetime) {
                this.datetime = datetime;
            }

            public String getFakeid() {
                return fakeid;
            }

            public void setFakeid(String fakeid) {
                this.fakeid = fakeid;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
