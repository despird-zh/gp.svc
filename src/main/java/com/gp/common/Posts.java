package com.gp.common;

/**
 * Helper class to define the constants required in post
 **/
public class Posts {

	/**
	 * The scope of the post, the scope can be square / workgroup
	 **/
	public static enum Scope{
		SQUARE, // publish to square
		WGROUP,  // publish to work group internal only
		PRIVATE, // only visible to attendee
	}

	/**
	 * the type of post, could be discussion or voting purpose
	 **/
	public static enum Type{
		DISCUSSION,
		VOTING,
	}

	/**
	 * Indicate the state of post, the post support recall function.
	 **/
	public static enum State{
		DRAFT,
		REVIEW,
		PUBLISHED,
		CLOSE,
		RECALL // recall the post
	}
}
