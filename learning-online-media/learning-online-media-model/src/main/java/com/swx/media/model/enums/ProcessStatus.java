package com.swx.media.model.enums;

/**
 * 任务处理状态
 */
public enum ProcessStatus {
    UN_PROCESS("1", "未处理"),
    PROCESS_SUCCESS("2", "处理成功"),
    PROCESS_FAIL("3", "处理失败"),
    PROCESSING("4", "处理中");

    private final String status;
    private final String desc;

    ProcessStatus(String status, String desc) {
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
