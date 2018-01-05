package com.example.zhoajie.sxeqem.global;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */

public interface IMeetParser {
    /**
     *
     * 解析输入流，获取Beauty列表
     * @param is
     * @return
     * @throws Exception
     */
    public List<MetaIS> parse(InputStream is) throws Exception;

    /**
     *
     * 序列化Beauty对象集合，得到XML形式的字符串
     * @param beauties
     * @return
     * @throws Exception
     */
    public String serialize(List<MetaIS> beauties) throws Exception;

}
