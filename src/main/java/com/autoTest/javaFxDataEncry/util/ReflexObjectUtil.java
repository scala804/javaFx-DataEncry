package com.autoTest.javaFxDataEncry.util;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
     * 反射处理Bean，得到里面的属性值
     *
     * @author liulinsen
     *
     */
    public class ReflexObjectUtil {

        private static final Logger logger=  LoggerFactory.getLogger(ReflexObjectUtil.class);

        /**
         * 单个对象的所有键值
         *
         * @param
         *
         *
         * @return Map<String, Object> map 所有 String键 Object值 ex：{pjzyfy=0.00,
         *         xh=01, zzyl=0.00, mc=住院患者压疮发生率, pjypfy=0.00, rs=0, pjzyts=0.00,
         *         czydm=0037, lx=921, zssl=0.00}
         */
        public static Map<String, Object> getMap(Object obj) {
            Map<String, Object> map = new HashMap<String, Object>();
            // 得到类对象
            Class userCla = (Class) obj.getClass();
            /* 得到类中的所有属性集合 */
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                // 设置些属性是可以访问的
                f.setAccessible(true);
                Object val = new Object();
                try {
                    val = f.get(obj);
                    if("null".equals(val)){
                        map.put(f.getName(), "");
                    }else {
                        // 得到此属性的值
                        // 设置键值
                        map.put(f.getName(), val);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("单个对象的所有键值==反射==" + map.toString());
            return map;
        }


        public static <T,K,V>T map2Bean(Map<K,V> map, Class<T> beanCls){
          T  t=null;
            try{
                t=beanCls.newInstance();
                BeanUtils.populate(t,(Map<String,? extends Object>) map);
            }catch (Exception e){
                logger.error("map为"+map+"转换为"+beanCls+"失败！");
            }
            return t;
        }


        /**
         * 把javaBean中的null转为为“”；
         * @param beanCls
         * @return
         */
        public static Class<T> getValueNotNull(Class<T> beanCls){
            BeanInfo beanInfo = null;
            try {
                beanInfo = Introspector.getBeanInfo(beanCls);
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
            PropertyDescriptor[] propertyDescriptors=beanInfo.getPropertyDescriptors();
                for(PropertyDescriptor propertyDescriptor:propertyDescriptors) {
                    String key=propertyDescriptor.getName();
                    Object value=propertyDescriptor.getValue(key);
                    if("null".equals(value)){
                        propertyDescriptor.setValue(key,"");
                    }
                }
            return beanCls;
        }


        /**
         * 单个对象的某个键的值
         *
         * @param
         *
         *
         * @param key
         *            键
         *
         * @return Object 键在对象中所对应得值 没有查到时返回空字符串
         */
        public static Object getValueByKey(Object obj, String key) {
            // 得到类对象
            Class userCla = (Class) obj.getClass();
            /* 得到类中的所有属性集合 */
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); // 设置些属性是可以访问的
                try {

                    if (f.getName().endsWith(key)) {
                        System.out.println("单个对象的某个键的值==反射==" + f.get(obj));
                        return f.get(obj);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            // 没有查到时返回空字符串
            return "";
        }
        /**
         * 多个（列表）对象的所有键值
         *
         * @param object
         * @return List<Map<String,Object>> 列表中所有对象的所有键值 ex:[{pjzyfy=0.00, xh=01,
         *         zzyl=0.00, mc=住院患者压疮发生率, pjypfy=0.00, rs=0, pjzyts=0.00,
         *         czydm=0037, lx=921, zssl=0.00}, {pjzyfy=0.00, xh=02, zzyl=0.00,
         *         mc=新生儿产伤发生率, pjypfy=0.00, rs=0, pjzyts=0.00, czydm=0037, lx=13,
         *         zssl=0.00}, {pjzyfy=0.00, xh=03, zzyl=0.00, mc=阴道分娩产妇产伤发生率,
         *         pjypfy=0.00, rs=0, pjzyts=0.00, czydm=0037, lx=0, zssl=0.00},
         *         {pjzyfy=0.00, xh=04, zzyl=0.75, mc=输血反应发生率, pjypfy=0.00, rs=0,
         *         pjzyts=0.00, czydm=0037, lx=0, zssl=0.00}, {pjzyfy=5186.12,
         *         xh=05, zzyl=0.00, mc=剖宫产率, pjypfy=1611.05, rs=13, pjzyts=7.15,
         *         czydm=0037, lx=13, zssl=0.00}]
         */
        public static List<Map<String, Object>> getKeysAndValues(List<Object> object) {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (Object obj : object) {
                Class userCla;
                // 得到类对象
                userCla = (Class) obj.getClass();
                /* 得到类中的所有属性集合 */
                Field[] fs = userCla.getDeclaredFields();
                Map<String, Object> listChild = new HashMap<String, Object>();
                for (int i = 0; i < fs.length; i++) {
                    Field f = fs[i];
                    f.setAccessible(true); // 设置些属性是可以访问的
                    Object val = new Object();
                    try {
                        val = f.get(obj);
                        // 得到此属性的值
                        listChild.put(f.getName(), val);// 设置键值
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                list.add(listChild);// 将map加入到list集合中
            }
            System.out.println("多个（列表）对象的所有键值====" + list.toString());
            return list;
        }
        /**
         * 多个（列表）对象的某个键的值
         *
         * @param object
         * @param key
         * @return List<Object> 键在列表中对应的所有值 ex:key为上面方法中的mc字段 那么返回的数据就是： [住院患者压疮发生率,
         *         新生儿产伤发生率, 阴道分娩产妇产伤发生率, 输血反应发生率, 剖宫产率]
         */
        public static List<Object> getValuesByKey(List<Object> object, String key) {
            List<Object> list = new ArrayList<Object>();
            for (Object obj : object) {
                // 得到类对象
                Class userCla = (Class) obj.getClass();
                /* 得到类中的所有属性集合 */
                Field[] fs = userCla.getDeclaredFields();
                for (int i = 0; i < fs.length; i++) {
                    Field f = fs[i];
                    f.setAccessible(true); // 设置些属性是可以访问的
                    try {
                        if (f.getName().endsWith(key)) {
                            list.add(f.get(obj));
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("多个（列表）对象的某个键的值列表====" + list.toString());
            return list;
        }
    }

