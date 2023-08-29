package com.swx.media.model.enums;

public enum MediaFileStatusEnum {
    NORMAL("1", "正常"),
    ABNORMAL("2", "不正常");
    private final String status;
    private final String desc;

    MediaFileStatusEnum(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public String status() {
        return status;
    }

    public String desc() {
        return desc;
    }
}
