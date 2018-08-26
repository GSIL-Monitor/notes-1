package com.api.wechat.entity.message;

/**
 * Author   NieYinjun
 * Date     2018/8/15 13:58
 * TAG   多媒体id的实体类
 */

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class MediaIdMessage {
    @XStreamAlias("MediaId")
    @XStreamCDATA
    private String MediaId;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}