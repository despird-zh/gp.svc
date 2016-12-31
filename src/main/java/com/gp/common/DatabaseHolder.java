package com.gp.common;

/**
 * The database context holder keep the context setting(DataSource Name)
 * in the ThreadLocal variable.
 * 
 * @author diaogc
 * @version 0.1 2016-12-29
 * 
 **/
public class DatabaseHolder {

	public static final String DATA_SOURCE_ACM = "dataSource_acm";
	
	/**
	 * the thread-local variable 
	 **/
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();  
      
    /**
     * set the database type to thread local 
     **/
    public static void setDatabaseType(String databaseType) {  
        contextHolder.set(databaseType);  
    }  
    
    /**
     * get the database type from the thread-local 
     **/
    public static String getDatabaseType() {  
        return contextHolder.get();  
    }  
    
    /**
     * reset the database type from thread-local 
     **/
    public static void resetDatabaseType() {  
        contextHolder.remove();  
    }  
}
