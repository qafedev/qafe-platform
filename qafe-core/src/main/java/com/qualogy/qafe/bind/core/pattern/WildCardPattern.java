/**
 * Copyright 2008-2014 Qualogy Solutions B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qualogy.qafe.bind.core.pattern;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
/**
 * a pattern is a string to which a field, like an id can be applied to.
 * a pattern can be wildcarded. 
 * patterns:
 * 	- *
 *  - *a
 *  - a*
 *  - aa
 *  not supported:
 *  - a*a
 *
 */

public class WildCardPattern implements Comparable<WildCardPattern>, Serializable{
	
	private static final long serialVersionUID = -7093827130572562410L;

	protected String pattern;
	
	/**
	 * fields for optimalization reasons
	 */
	private String regex;
	private String strippedPattern;
	
	public final static char WILDCARD = '*';
	
	public WildCardPattern() {
		super();
	}

	public WildCardPattern(String pattern) {
		super();
		this.pattern = pattern;
	}

	public boolean isWildCarded(){
		return isWildCardedAtTheBegin() || isWildCardedAtTheEnd();
	}
	
	public String toString() {
		return pattern;
	}

	public String stripWildCards(){
		if(strippedPattern==null)
			strippedPattern = StringUtils.strip(pattern, ""+WILDCARD);
		return strippedPattern;
	}
	
	private boolean isWildCardedAtPosition(int position){
		return pattern.charAt(position)==WILDCARD;
	}
	
	public boolean isWildCardedAtTheBegin(){
		return isWildCardedAtPosition(0);
	}
	
	public boolean isWildCardedAtTheEnd(){
		return isWildCardedAtPosition(pattern.length()-1);
	}
	
	public boolean matches(String toMatch){
		return Pattern.matches(toRegex(), toMatch);
	}
	
	private String toRegex(){
        if(regex==null)
        	regex = createRegex(pattern);
        return regex;
	}
	
    private String createRegex(String wildcard){
        StringBuffer s = new StringBuffer(wildcard.length());
        
        s.append('^');
        for (int i = 0, is = wildcard.length(); i < is; i++) {
            char c = wildcard.charAt(i);
            switch(c) {
                case '*':
                    s.append(".*");
                    break;
                case '?':
                    s.append(".");
                    break;
                    // escape special regexp-characters
                case '(': case ')': case '[': case ']': case '$':
                case '^': case '.': case '{': case '}': case '|':
                case '\\':
                    s.append("\\");
                    s.append(c);
                    break;
                default:
                    s.append(c);
                    break;
            }
        }
        s.append('$');
        return(s.toString());
    }

	
	public int compareTo(WildCardPattern other) {
 		int outcome = 0;
 		
 		if(this.stripWildCards().length()==0)
 			outcome = 1;
 		else if(((WildCardPattern)other).stripWildCards().length()==0)
 			outcome = -1;
 		else if(this.isWildCarded() && !((WildCardPattern)other).isWildCarded())
			outcome = 1;
		else if(!this.isWildCarded() && ((WildCardPattern)other).isWildCarded())
			outcome = -1;
		else if(this.isWildCarded() && ((WildCardPattern)other).isWildCarded()){
			if(this.stripWildCards().length()>((WildCardPattern)other).stripWildCards().length())
				outcome = -1;
			else
				outcome = 1;
		}
		return outcome;
	}
}
