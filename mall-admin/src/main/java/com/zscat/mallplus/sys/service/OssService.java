package com.zscat.mallplus.sys.service;


import com.zscat.mallplus.sys.vo.OssCallbackResult;
import com.zscat.mallplus.sys.vo.OssPolicyResult;

import javax.servlet.http.HttpServletRequest;

/**
 * oss上传管理Service
 * https://github.com/shenzhuan/mallplus on 2018/5/17.
 */
public interface OssService {
    OssPolicyResult policy();

    OssCallbackResult callback(HttpServletRequest request);
}
