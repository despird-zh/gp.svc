package com.gp.common;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * The database dynamic which switch between different data source
 * 
 * @author diaogc
 * @version 0.1 2016-12-29
 **/
public class DatabaseDynamic extends AbstractRoutingDataSource{  
  
    @Override  
    protected Object determineCurrentLookupKey() {  
        return DatabaseHolder.getDatabaseType();  
    }
}  
