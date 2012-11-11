
package com.momock.dal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.momock.util.Logger;

public class Database extends SQLiteOpenHelper {

    private static final String INIT_CREATE_SQL = "database_init";
    
    private static final String VERSION_FOR_UPGRADE = "database_version";
        
    private SQLiteDatabase dbObj;
    
    private Context context;
    
    private boolean isUpgrade = false;
    
    public Database(Context context) {
        super(context, null, null, 1);
        this.context = context;
    }
    
    public Database(Context context, String dbName) {
        super(context, dbName, null, Integer.parseInt(context.getResources().getString(context.getResources().getIdentifier(VERSION_FOR_UPGRADE, "string", context.getPackageName()))));
        this.context = context;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        int pointer = context.getResources().getIdentifier(INIT_CREATE_SQL, "string", context.getPackageName());
        if (pointer == 0) {
            Logger.error("undefined sql id - initialize");
        } else {
            String[] createTabelSqls = context.getResources().getString(pointer).split(";");
            for (String sql : createTabelSqls) {
                db.execSQL(sql + ";");
            }
        }
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        isUpgrade = true;
    }
    
    public Map<String, Object> executeForMap(String sqlId, Map<String, Object> bindParams) {
        getDbObject();
        int pointer = context.getResources().getIdentifier(sqlId, "string", context.getPackageName());
        if (pointer == 0) {
            Logger.error("undefined sql id");
            return null;
        }
        String sql = context.getResources().getString(pointer);
        if (bindParams != null) {
            Iterator<String> mapIterator = bindParams.keySet().iterator();
            while (mapIterator.hasNext()) {
                String key = mapIterator.next();
                Object value = bindParams.get(key);
                sql = sql.replaceAll("#" + key.toLowerCase() + "#", value == null ? null : "'" + value.toString() + "'");
            }
        }
        if (sql.indexOf('#') != -1) {
            Logger.error("undefined parameter");
            return null;
        }
        Cursor cursor = dbObj.rawQuery(sql, null);
        List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
        if (cursor == null) {
            return null;
        }
        String[] columnNames = cursor.getColumnNames();
        while(cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            int i = 0;
            for (String columnName : columnNames) {
                map.put(columnName, cursor.getString(i));
                i++;
            }
            mapList.add(map);
        }
        if (mapList.size() <= 0) {
            return null;
        }
        cursor.close();
        dbObj.close();
        return mapList.get(0);
    }
    
    public List<Map<String, Object>> executeForMapList(String sqlId, Map<String, Object> bindParams) {
        getDbObject();
        int pointer = context.getResources().getIdentifier(sqlId, "string", context.getPackageName());
        if (pointer == 0) {
            Logger.error("undefined sql id");
            return null;
        }
        String sql = context.getResources().getString(pointer);
        if (bindParams != null) {
            Iterator<String> mapIterator = bindParams.keySet().iterator();
            while (mapIterator.hasNext()) {
                String key = mapIterator.next();
                Object value = bindParams.get(key);
                sql = sql.replaceAll("#" + key.toLowerCase() + "#", value == null ? null : "'" + value.toString() + "'");
            }
        }
        if (sql.indexOf('#') != -1) {
            Logger.error("undefined parameter");
            return null;
        }
        Cursor cursor = dbObj.rawQuery(sql, null);
        List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
        if (cursor == null) {
            return null;
        }
        String[] columnNames = cursor.getColumnNames();
        while(cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            int i = 0;
            for (String columnName : columnNames) {
                map.put(columnName, cursor.getString(i));
                i++;
            }
            mapList.add(map);
        }
        cursor.close();
        dbObj.close();
        return mapList;
    }
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T executeForBean(String sqlId, Map<String, Object> bindParams, Class bean) {
        getDbObject();
        int pointer = context.getResources().getIdentifier(sqlId, "string", context.getPackageName());
        if (pointer == 0) {
            Logger.error("undefined sql id");
            return null;
        }
        String sql = context.getResources().getString(pointer);
        if (bindParams != null) {
            Iterator<String> mapIterator = bindParams.keySet().iterator();
            while (mapIterator.hasNext()) {
                String key = mapIterator.next();
                Object value = bindParams.get(key);
                sql = sql.replaceAll("#" + key.toLowerCase() + "#", value == null ? null : "'" + value.toString() + "'");
            }
        }
        if (sql.indexOf('#') != -1) {
            Logger.error("undefined parameter");
            return null;
        }
        Cursor cursor = dbObj.rawQuery(sql, null);
        List<T> objectList = new ArrayList<T>();
        if (cursor == null) {
            return null;
        }
        String[] columnNames = cursor.getColumnNames();
        List<String> dataNames = new ArrayList<String>();
        for (String columnName : columnNames) {
            dataNames.add(chgDataName(columnName));
        } 
        T beanObj = null;
        // get bean class package
        Package beanPackage = bean.getPackage();
        while(cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            int i = 0;
            for (String dataName : dataNames) {
                map.put(dataName, cursor.getString(i));
                i++;
            }
            JSONObject json = new JSONObject(map);
            try {
                beanObj = (T)parse(json.toString(), bean, beanPackage.getName());
            } catch (Exception e) {
                Logger.debug(e.toString());
                return null;
            } 
            objectList.add(beanObj);
        }
        if (objectList.size() <= 0) {
            return null;
        }
        cursor.close();
        dbObj.close();
        return objectList.get(0);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T>List<T> executeForBeanList(String sqlId, Map<String, Object> bindParams, Class bean) {
        getDbObject();
        int pointer = context.getResources().getIdentifier(sqlId, "string", context.getPackageName());
        if (pointer == 0) {
            Logger.error("undefined sql id");
            return null;
        }
        String sql = context.getResources().getString(pointer);
        if (bindParams != null) {
            Iterator<String> mapIterator = bindParams.keySet().iterator();
            while (mapIterator.hasNext()) {
                String key = mapIterator.next();
                Object value = bindParams.get(key);
                sql = sql.replaceAll("#" + key.toLowerCase() + "#", value == null ? null : "'" + value.toString() + "'");
            }
        }
        if (sql.indexOf('#') != -1) {
            Logger.error("undefined parameter");
            return null;
        }
        Cursor cursor = dbObj.rawQuery(sql, null);
        List<T> objectList = new ArrayList<T>();
        if (cursor == null) {
            return null;
        }
        String[] columnNames = cursor.getColumnNames();
        List<String> dataNames = new ArrayList<String>();
        for (String columnName : columnNames) {
            dataNames.add(chgDataName(columnName));
        } 
        T beanObj = null;
        // get bean class package
        Package beanPackage = bean.getPackage();
        while(cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            int i = 0;
            for (String dataName : dataNames) {
                map.put(dataName, cursor.getString(i));
                i++;
            }
            JSONObject json = new JSONObject(map);
            try {
                beanObj = (T)parse(json.toString(), bean, beanPackage.getName());
            } catch (Exception e) {
                // Logger.debug(e.toString());
                return null;
            } 
            objectList.add(beanObj);
        }
        cursor.close();
        dbObj.close();
        return objectList;
    }
    
    public int execute(String sqlId, Map<String, Object> bindParams) {
        getDbObject();
        int row = 0;
        int pointer = context.getResources().getIdentifier(sqlId, "string", context.getPackageName());
        if (pointer == 0) {
            Logger.error("undefined sql id");
            return row;
        }
        String sql = context.getResources().getString(pointer);
        if (bindParams != null) {
            Iterator<String> mapIterator = bindParams.keySet().iterator();
            while (mapIterator.hasNext()) {
                String key = mapIterator.next();
                Object value = bindParams.get(key);
                sql = sql.replaceAll("#" + key.toLowerCase() + "#", value == null ? null : "'" + value.toString() + "'");
            }
        }
        if (sql.indexOf('#') != -1) {
            Logger.error("undefined parameter");
            return row;
        }
        try {
            dbObj.execSQL(sql);
            dbObj.close();
            row += 1;
        } catch (SQLException e) {
            return row;
        }
        return row;
    }
    
    private SQLiteDatabase getDbObject() {
        if (dbObj == null || !dbObj.isOpen()) {
            dbObj = getWritableDatabase();
        }
        return dbObj;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object parse(String jsonStr, Class beanClass, String basePackage) throws Exception {
        Object obj = null;
        JSONObject jsonObj = new JSONObject(jsonStr);
        // Check bean object
        if(beanClass == null){
            Logger.debug("Bean class is null");
            return null;
        }
        // Read Class member fields
        Field[] props = beanClass.getDeclaredFields();
        if(props == null || props.length == 0) {
            Logger.debug("Class"+ beanClass.getName() +" has no fields");
            return null;
        }
        // Create instance of this Bean class
        obj = beanClass.newInstance();
        // Set value of each member variable of this object
        for (int i=0; i<props.length; i++) {
            String fieldName = props[i].getName();
            // Skip public and static fields
            if (props[i].getModifiers() == (Modifier.PUBLIC | Modifier.STATIC)) {
                continue;
            }
            // Date Type of Field 
            Class type = props[i].getType();
            String typeName = type.getName();
            // Check for Custom type
            if (typeName.equals("int")) {
                Class[] parms = {type};
                Method m = beanClass.getDeclaredMethod(getBeanMethodName(fieldName, 1), parms);
                m.setAccessible(true);
                // Set value
                try {
                    m.invoke(obj, jsonObj.getInt(fieldName));
                } catch (Exception ex) {
                    Logger.debug(ex.getMessage());
                }
            } else if (typeName.equals("long")) {
                Class[] parms = {type};
                Method m = beanClass.getDeclaredMethod(getBeanMethodName(fieldName, 1), parms);
                m.setAccessible(true);
                // Set value
                try {
                    m.invoke(obj, jsonObj.getLong(fieldName));
                }catch(Exception ex){
                    Logger.debug(ex.getMessage());
                }
            } else if (typeName.equals("java.lang.String")) {
                Class[] parms = {type};
                Method m = beanClass.getDeclaredMethod(getBeanMethodName(fieldName, 1), parms);
                m.setAccessible(true);
                // Set value
                try {
                    m.invoke(obj, jsonObj.getString(fieldName));
                } catch (Exception ex) {
                    Logger.debug(ex.getMessage());
                }
            } else if (typeName.equals("double")) {
                Class[] parms = {type};
                Method m = beanClass.getDeclaredMethod(getBeanMethodName(fieldName, 1), parms);
                m.setAccessible(true);
                // Set value
                try {
                    m.invoke(obj,  jsonObj.getDouble(fieldName));
                } catch(Exception ex){
                    Logger.debug(ex.getMessage());
                }
            } else if (type.getName().equals(List.class.getName()) || 
                    type.getName().equals(ArrayList.class.getName())) {
                // Find out the Generic
                String generic = props[i].getGenericType().toString();
                if (generic.indexOf("<") != -1) {
                    String genericType = generic.substring(generic.lastIndexOf("<") + 1, generic.lastIndexOf(">"));
                    if (genericType != null) {
                        JSONArray array = null;
                        try {
                            array = jsonObj.getJSONArray(fieldName);
                        } catch(Exception ex) {
                            Logger.debug(ex.getMessage());
                            array = null;
                        }
                        if (array == null) {
                            continue;
                        }
                        ArrayList arrayList = new ArrayList();
                        for (int j=0; j<array.length(); j++) {
                            arrayList.add(parse(array.getJSONObject(j).toString(), Class.forName(genericType), basePackage));
                        }
                        // Set value
                        Class[] parms = {type};
                        Method m = beanClass.getDeclaredMethod(getBeanMethodName(fieldName, 1), parms);
                        m.setAccessible(true);
                        m.invoke(obj, arrayList);
                    }
                } else {
                    // No generic defined
                    generic = null;
                }
            } else if (typeName.startsWith(basePackage)) {
                Class[] parms = {type};
                Method m = beanClass.getDeclaredMethod(getBeanMethodName(fieldName, 1), parms);
                m.setAccessible(true);
                // Set value
                try{
                    JSONObject customObj = jsonObj.getJSONObject(fieldName);
                    if (customObj != null) {
                        m.invoke(obj, parse(customObj.toString(), type, basePackage));
                    }
                } catch (JSONException ex) {
                    Logger.debug(ex.getMessage());
                }
            } else {
                // Skip
                Logger.debug("Field " + fieldName + "#" + typeName + " is skip");
            }
        }
        return obj;
    }
    
    private String getBeanMethodName(String fieldName, int type){
        if(fieldName == null || fieldName == "") {
            return "";
        }
        String methodName = "";
        if(type == 0) {
            methodName = "get";
        } else {
            methodName = "set";
        }
        methodName += fieldName.substring(0, 1).toUpperCase();
        if (fieldName.length() == 1) {
            return methodName;
        }
        methodName += fieldName.substring(1);
        return methodName;
    }
    
    private String chgDataName(String targetStr) {
        Pattern p = Pattern.compile("_([a-z])");
        Matcher m = p.matcher(targetStr.toLowerCase());

        StringBuffer sb = new StringBuffer(targetStr.length());
        while (m.find()) {
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }
    
    public boolean isUpgrade() {
        // log and set upgrade flag
        Logger.debug(String.valueOf(getDbObject().getVersion())); 
        return isUpgrade;
    }
}
