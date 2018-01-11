package com.example.zhoajie.sxeqem.global;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/1/7.
 */
public class Feature
{
    private HashMap attributes=new HashMap();

    /**
     * 添加一项属性内容
     * @param fieldName
     * @param fieldValue
     */
    public void AddAttributeItem(String fieldName,Object fieldValue)
    {
        attributes.put(fieldName,String.valueOf(fieldValue));
    }

    /**
     * 获取属性集合hashmap
     * @return
     */
    public HashMap GetAttributeItems()
    {
        return attributes;
    }

}
