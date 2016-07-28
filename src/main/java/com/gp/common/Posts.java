package com.gp.common;

/**
 * Helper class to define the constants required in post
 **/
public class Posts {

	public static enum Scope{

		SQUARE, // publish to square
		WGROUP,  // publish to work group internal only
		PRIVATE, // only visible to attendee
	}
	
	public static enum Type{
		DISCUSSION,
		VOTING,
	}

	public static enum State{
		DRAFT,
		REVIEW,
		PUBLISHED,
		CLOSE,
	}
}
