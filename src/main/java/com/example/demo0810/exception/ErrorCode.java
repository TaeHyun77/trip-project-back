package com.example.demo0810.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.PrimitiveIterator;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNKNOWN("000_UNKNOWN", "알 수 없는 에러가 발생했습니다."),


    DUPLICATED_ID_FAILED("001_DUPLICATED_ID", "중복된 ID입니다."),

    NOT_EXIST_USER("002_NOT_EXIST_USER", "존재하지 않는 사용자입니다."),

    INCONSISTENT_USER("INCONSISTENT_USER", "일치하지 않는 사용자입니다."),

    DUPLICATED_USER("DUPLICATED_USER", "중복된 유저입니다."),

    NOT_EXIST_POST("003_NOT_EXIST_POST", "존재하지 않는 게시물입니다."),

    NOT_EXIST_COMMENT("003_NOT_EXIST_COMMENT", "존재하지 않는 댓글입니다."),

    POST_DELETE_ERROR("POST_DELETE_ERROR", "게시글 삭제 중 에러"),

    NOT_CORRECT_PASSWORD("004_NOT_CORRECT_PASSWORD", "비밀번호가 맞지 않습니다."),

    UNATHORIZATION("006_UNATHORIZATION", "인증되지 않아습니다."),

    INVALID_DATE_FORMAT("INVALID_DATE_FORMAT", "잘못된 날짜 변환"),

    FILE_SAVE_FAILED("FILE_SAVE_FAILED", "파일 저장 실패"),

    FILE_UPLOAD_FAILED("FILE_UPLOAD_FAILED", "파일 업로드 실패"),

    PARTICIPATION_FAILED("PARTICIPATION_FAILED", "참여 실패"),

    ALREADY_PARTICIPATION("ALREADY_PARTICIPATION", "이미 참여 되어 있습니다."),

    FAIL_TO_LOAD_REGION("FAIL_TO_LOAD_REGION", "REGION 데이터 업로드 실패"),

    NOT_EXIST_PARTICIPATION("NOT_EXIST_PARTICIPATION", "참여해 있는 사용자가 아닙니다."),

    NOT_EXIST_MESSAGE("NOT_EXIST_MESSAGE", "존재하지 않는 쪽지입니다.");

    private final String errorCode;

    private final String message;
}
