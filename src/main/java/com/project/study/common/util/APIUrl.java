package com.project.study.common.util;

import org.springframework.stereotype.Component;

@Component
public class APIUrl {

    /* TRC 서버 상태 조회 */
    public static final String TR_SERVER_STATUS_URL = "/transcoder/vod/status";

    /* VOD 트랜스코딩 작업 컨트롤 */
    public static final String TR_CTRL_VOD_URL = "/transcoder/vod";

    /* VOD 트랜스코딩 작업 상태 조회 */
    public static final String TR_REPORT_VOD_URL = "/trc/vod/status/{serverId}";

}
