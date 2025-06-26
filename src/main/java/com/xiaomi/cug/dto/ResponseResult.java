package com.xiaomi.cug.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> {
        private int status;
        private String msg;
        private T data;

        // 快捷创建成功响应
        public static <T> ResponseResult<T> success(T data) {
                return new ResponseResult<>(200, "ok", data);
        }

        // 快捷创建失败响应（带错误信息）
        public static <T> ResponseResult<T> error(String msg) {
                return new ResponseResult<>(500, msg, null);
        }

        // 可指定状态码和消息
        public static <T> ResponseResult<T> of(int status, String msg, T data) {
                return new ResponseResult<>(status, msg, data);
        }
}
