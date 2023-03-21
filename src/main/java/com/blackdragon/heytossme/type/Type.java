package com.blackdragon.heytossme.type;

import lombok.Getter;

@Getter
public enum Type {

    BOOKMARK("북마크"),    //등록한 상품을 누군가가 북마크처리했을때
    KEYWORD("키워드"),    //키워드로 등록해둔 상품이 업로드 되었을때
    DEAL("거래");        //거래가 완료되었을때

    final String toKorean;

    Type(String toKorean) {
        this.toKorean = toKorean;
    }
}
