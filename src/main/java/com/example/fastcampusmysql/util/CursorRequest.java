package com.example.fastcampusmysql.util;

public record CursorRequest(
        Long key, //
        int size
) {
    public static final Long NONE_KEY = -1L; // Cursor key that shows there is no more page

    public CursorRequest next(Long key){ // CursorRequest for response
        return new CursorRequest(key,size);
    }

    public Boolean hasKey(){
        return key!=null;
    }
}
