/*
 * Licensed to the G.Obal under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  G.Obal licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
package com.gp.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * AccessPoint is the client information that launch operation
 * 
 * @author despird
 * @version 0.1 2014-2-1
 * 
 **/
public class AccessPoint {
	
	/** client name e.g browser ,or device*/
	private String client;
	/** client address */
	private String host;
	/** application name e.g admin */
	private String app;
	/** the version */
	private String version;
	
	/**
	 * Constructor with name
	 * @deprecated not suggest to use this constructor, because of low performance
	 *  @param name the access point name
	 **/
	public AccessPoint(String client) {
		this.client = client;
		try {
			InetAddress iaddr =  InetAddress.getLocalHost();
			this.host = iaddr.getHostAddress();
			
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Constructor with name and ipaddress
	 * 
	 * @param name the access point name
	 * @param ipaddress the ip address 
	 **/
	public AccessPoint(String client, String host) {
		this.client = client;
		this.host = host;
	}

	public AccessPoint(String client, String app, String version) {
		this(client);
		this.app = app;
		this.version = version;
	}
	
	public AccessPoint(String client, String host, String app, String version) {
		this(client, host);
		this.app = app;
		this.version = version;
	}
	
	/**
	 * Get IP address
	 * 
	 *  @return String the ip address
	 **/
	public String getHost() {
		return host;
	}

	/**
	 * Get name 
	 * 
	 * @return String the name
	 **/
	public String getClient() {
		return client;
	}

	public String getApp(){
		
		return this.app;
		
	}
	
	public String getVersion(){
		
		return this.version;
	}
	@Override
	public int hashCode() {
		
		return new HashCodeBuilder(17, 37).append(client)
				.append(host).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AccessPoint other = (AccessPoint) obj;
				
		return new EqualsBuilder()
		.append(this.client, other.client)
		.append(this.host, other.host).isEquals();
	}

	@Override
	public String toString() {
		String retValue = "";

		retValue = "App(" + "name=" + this.client + ", ip=" + this.host
				+ ")";

		return retValue;
	}
}
