package com.example.zhoajie.sxeqem.global;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2018/1/5.
 */

public class GpResult {
    //参照json结果集合进行映射class
    private List<Field> fieldslist=new ArrayList<Field>();
    private List<Feature> featurelist=new ArrayList<Feature>();

    /**
     * 添加字段项到result的fields集合中 包括 字段名称 类型 别名 三个
     * @param values 三个字符串值
     */
    public void AddfieldItem(String[] values)
    {
        Field f=new Field();
        f.setName(values[0]);
        f.setAlias(values[1]);
        f.setType(values[2]);
        fieldslist.add(f);
    }

    /**
     *获取全部字段项
     * @return
     */
    public List<Field> GetfieldItems()
    {
        return fieldslist;

    }

    /**
     * 添加feature中的一条属性内容
     * @param _name 字段名称
     * @param _value 属性值
     */
    public void AddfeatureItem(Feature f)
    {

        featurelist.add(f);

    }

    public List<Feature> GetFeatureItems()
    {
        return featurelist;

    }



}

