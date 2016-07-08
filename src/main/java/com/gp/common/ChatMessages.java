package com.gp.common;

public class ChatMessages {

	/**
	 * the type of chat 
	 **/
	public static enum ChatType{
		
		WORKGROUP, // workgroup internal chat
		PEER2PEER, // peer 2 peer chat
		DISCUSS, // discussion chat
		TEMPORARY // temporary chat
	}
	
	/**
	 * the message type 
	 **/
	public static enum MsgType{
		
		TEXT, // text message
		IMAGE, // image message
		FILE // file message
	}
}
