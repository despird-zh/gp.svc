/*******************************************************************************
 * Copyright 2016 Gary Diao - gary.diao@outlook.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.gp.common;

public class TagVotes {

	/**
	 * the type of tag, to identify what resources the tag could be attached on
	 * while the category is used to group up the tags
	 **/
	public static enum TagType{
		
		FOLDER, // folder tag
		FILE, // file tag
		POST, // post tag
		TASK, // task tag
		WORKGROUP // workgroup tag
	}
	
	public static enum TagColor{
		
		RED,
		ORANGE,
		YELLOW,
		GREEN,
		BLANK,
		BLUE,
		DEFAULT
	}

	/**
	 * the possible opinion in voting table
	 **/
	public static enum VoteOpinion{
		LIKE,
		DISLIKE,
		AGREE,
		DISAGREE, // disagree
		RESERVE, // reserve the personal suggestion
		ABSTAIN // give up the voting
	}
}
