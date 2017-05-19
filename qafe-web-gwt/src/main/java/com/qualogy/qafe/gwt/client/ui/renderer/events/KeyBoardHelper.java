/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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
package com.qualogy.qafe.gwt.client.ui.renderer.events;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;

public class KeyBoardHelper {

	public static final int KEY_F1							= 112;
	public static final int KEY_F2							= 113;
	public static final int KEY_F3							= 114;
	public static final int KEY_F4							= 115;
	public static final int KEY_F5							= 116;
	public static final int KEY_F6							= 117;
	public static final int KEY_F7							= 118;
	public static final int KEY_F8							= 119;
	public static final int KEY_F9							= 120;
	public static final int KEY_F10							= 121;
	public static final int KEY_F11							= 122;
	public static final int KEY_F12							= 123;
	public static final int KEY_CAPSLOCK					= 20;
	public static final int KEY_SPACE						= 32;
	public static final int KEY_INSERT						= 45;
	public static final int KEY_NUMPAD_0					= 96;
	public static final int KEY_NUMPAD_1					= 97;
	public static final int KEY_NUMPAD_2					= 98;
	public static final int KEY_NUMPAD_3					= 99;
	public static final int KEY_NUMPAD_4					= 100;
	public static final int KEY_NUMPAD_5					= 101;
	public static final int KEY_NUMPAD_6					= 102;
	public static final int KEY_NUMPAD_7					= 103;
	public static final int KEY_NUMPAD_8					= 104;
	public static final int KEY_NUMPAD_9					= 105;
	public static final int KEY_NUMPAD_MULTIPLY 			= 106;
	public static final int KEY_NUMPAD_ADD					= 107;
	public static final int KEY_NUMPAD_SUBTRACT				= 109;
	public static final int KEY_NUMPAD_DECIMAL				= 110;
	public static final int KEY_NUMPAD_DIVIDE				= 111;
	public static final int KEY_NUM_LOCK					= 144;
	public static final int KEY_SEMICOLON					= 186;
	public static final int KEY_EQUALS						= 187;
	public static final int KEY_COMMA						= 188;
	public static final int KEY_HYPHEN						= 189;
	public static final int KEY_DOT							= 190;
	public static final int KEY_SLASH						= 191;
	public static final int KEY_BACKSLASH					= 220;
	public static final int KEY_BACKTICK					= 192;
	public static final int KEY_SQUAREBRACKET_OPEN			= 219;
	public static final int KEY_SQUAREBRACKET_CLOSE			= 221;
	public static final int KEY_SINGLEQUOTE					= 222;

	public static final String KEY_NAME_F1					= "F1";
	public static final String KEY_NAME_F2					= "F2";
	public static final String KEY_NAME_F3					= "F3";
	public static final String KEY_NAME_F4					= "F4";
	public static final String KEY_NAME_F5					= "F5";
	public static final String KEY_NAME_F6					= "F6";
	public static final String KEY_NAME_F7					= "F7";
	public static final String KEY_NAME_F8					= "F8";
	public static final String KEY_NAME_F9					= "F9";
	public static final String KEY_NAME_F10					= "F10";
	public static final String KEY_NAME_F11					= "F11";
	public static final String KEY_NAME_F12					= "F12";
	public static final String KEY_NAME_SPACE 				= "KEY_SPACE";
	public static final String KEY_NAME_INSERT 				= "KEY_INSERT";
	public static final String KEY_NAME_DELETE 				= "KEY_DELETE";
	public static final String KEY_NAME_HOME 				= "KEY_HOME";
	public static final String KEY_NAME_END 				= "KEY_END";
	public static final String KEY_NAME_PAGEUP 				= "KEY_PAGEUP";
	public static final String KEY_NAME_PAGEDOWN			= "KEY_PAGEDOWN";
	public static final String KEY_NAME_UP 					= "KEY_UP";
	public static final String KEY_NAME_DOWN 				= "KEY_DOWN";
	public static final String KEY_NAME_LEFT 				= "KEY_LEFT";
	public static final String KEY_NAME_RIGHT 				= "KEY_RIGHT";
	public static final String KEY_NAME_ALT					= "KEY_ALT";
	public static final String KEY_NAME_BACKSPACE 			= "KEY_BACKSPACE";
	public static final String KEY_NAME_CTRL				= "KEY_CTRL";
	public static final String KEY_NAME_ENTER 				= "KEY_ENTER";
	public static final String KEY_NAME_ESCAPE 				= "KEY_ESCAPE";
	public static final String KEY_NAME_SHIFT 				= "KEY_SHIFT";
	public static final String KEY_NAME_TAB 				= "KEY_TAB";
	public static final String KEY_NAME_NUM_LOCK 			= "KEY_NUMLOCK";

	public static final String PARAM_KEY 					= "key";
	public static final String PARAM_KEY_DELIMITER 			= "\\+";

	private KeyBoardHelper() {
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	public static boolean isValidKeyInput(NativeEvent keyEvent) {
		if (keyEvent == null) {
			return false;
		}
		int keyCode = keyEvent.getKeyCode();
		switch (keyCode) {
			case KeyCodes.KEY_ALT:
			case KeyCodes.KEY_CTRL:
			case KeyCodes.KEY_SHIFT:
			case KeyCodes.KEY_UP:
			case KeyCodes.KEY_DOWN:
			case KeyCodes.KEY_LEFT:
			case KeyCodes.KEY_RIGHT:
			case KeyCodes.KEY_TAB:
			case KeyCodes.KEY_ENTER:
			case KeyCodes.KEY_ESCAPE:
			case KEY_CAPSLOCK:
			case KEY_INSERT:
			case KeyCodes.KEY_HOME:
			case KeyCodes.KEY_END:
			case KeyCodes.KEY_PAGEUP:
			case KeyCodes.KEY_PAGEDOWN:
			case KEY_F1:
			case KEY_F2:
			case KEY_F3:
			case KEY_F4:
			case KEY_F5:
			case KEY_F6:
			case KEY_F7:
			case KEY_F8:
			case KEY_F9:
			case KEY_F10:
			case KEY_F11:
			case KEY_F12:
			case KEY_NUM_LOCK: {
				return false;
			}
		}
		return true;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	public static boolean isKeyInput(final String param, final String value, final String keyInput) {
		if (PARAM_KEY.equals(param) && (value != null)) {
			return value.equals(keyInput);
		}
		return false;
	}

	public static boolean isKeyInput(final String param, final String value, final NativeEvent keyEvent) {
		if (PARAM_KEY.equals(param) && (value != null)) {
			Set<String> keyInputs = getKeyInputs(keyEvent);
			String[] keyValues = value.toUpperCase().split(PARAM_KEY_DELIMITER);
			if ((keyInputs.size() > 0) && (keyInputs.size() == keyValues.length)) {
				for (int i=0; i<keyValues.length; i++) {
					String keyValue = keyValues[i];
					if (!keyInputs.contains(keyValue.trim())) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

    // CHECKSTYLE.OFF: CyclomaticComplexity
	private static Set<String> getKeyInputs(final NativeEvent keyEvent) {
		Set<String> keyInputs =  new HashSet<String>();
		if (keyEvent != null) {
			int keyCode = keyEvent.getKeyCode();
			if (keyEvent.getAltKey()) {
				keyInputs.add(KEY_NAME_ALT);
			}
			if (keyEvent.getCtrlKey()) {
				keyInputs.add(KEY_NAME_CTRL);
			}
			if (keyEvent.getShiftKey()) {
				keyInputs.add(KEY_NAME_SHIFT);
			}
			switch (keyCode) {
				case KeyCodes.KEY_ALT : {
					keyInputs.add(KEY_NAME_ALT);
				} break;
				case KeyCodes.KEY_CTRL : {
					keyInputs.add(KEY_NAME_CTRL);
				} break;
				case KeyCodes.KEY_SHIFT : {
					keyInputs.add(KEY_NAME_SHIFT);
				} break;
				case KeyCodes.KEY_UP : {
					keyInputs.add(KEY_NAME_UP);
				} break;
				case KeyCodes.KEY_DOWN : {
					keyInputs.add(KEY_NAME_DOWN);
				} break;
				case KeyCodes.KEY_LEFT : {
					keyInputs.add(KEY_NAME_LEFT);
				} break;
				case KeyCodes.KEY_RIGHT : {
					keyInputs.add(KEY_NAME_RIGHT);
				} break;
				case KeyCodes.KEY_TAB : {
					keyInputs.add(KEY_NAME_TAB);
				} break;
				case KeyCodes.KEY_ENTER : {
					keyInputs.add(KEY_NAME_ENTER);
				} break;
				case KeyCodes.KEY_ESCAPE : {
					keyInputs.add(KEY_NAME_ESCAPE);
				} break;
				case KeyCodes.KEY_BACKSPACE : {
					keyInputs.add(KEY_NAME_BACKSPACE);
				} break;
				case KEY_INSERT : {
					keyInputs.add(KEY_NAME_INSERT);
				} break;
				case KeyCodes.KEY_DELETE : {
					keyInputs.add(KEY_NAME_DELETE);
				} break;
				case KeyCodes.KEY_HOME : {
					keyInputs.add(KEY_NAME_HOME);
				} break;
				case KeyCodes.KEY_END : {
					keyInputs.add(KEY_NAME_END);
				} break;
				case KeyCodes.KEY_PAGEUP : {
					keyInputs.add(KEY_NAME_PAGEUP);
				} break;
				case KeyCodes.KEY_PAGEDOWN : {
					keyInputs.add(KEY_NAME_PAGEDOWN);
				} break;
				case KEY_SPACE : {
					keyInputs.add(KEY_NAME_SPACE);
				} break;
				case KEY_F1 : {
					keyInputs.add(KEY_NAME_F1);
				} break;
				case KEY_F2 : {
					keyInputs.add(KEY_NAME_F2);
				} break;
				case KEY_F3 : {
					keyInputs.add(KEY_NAME_F3);
				} break;
				case KEY_F4 : {
					keyInputs.add(KEY_NAME_F4);
				} break;
				case KEY_F5 : {
					keyInputs.add(KEY_NAME_F5);
				} break;
				case KEY_F6 : {
					keyInputs.add(KEY_NAME_F6);
				} break;
				case KEY_F7 : {
					keyInputs.add(KEY_NAME_F7);
				} break;
				case KEY_F8 : {
					keyInputs.add(KEY_NAME_F8);
				} break;
				case KEY_F9 : {
					keyInputs.add(KEY_NAME_F9);
				} break;
				case KEY_F10 : {
					keyInputs.add(KEY_NAME_F10);
				} break;
				case KEY_F11 : {
					keyInputs.add(KEY_NAME_F11);
				} break;
				case KEY_F12 : {
					keyInputs.add(KEY_NAME_F12);
				} break;
				case KEY_NUM_LOCK : {
					keyInputs.add(KEY_NAME_NUM_LOCK);
				} break;
				case KEY_NUMPAD_DIVIDE : {
					keyInputs.add("/");
				} break;
				case KEY_NUMPAD_MULTIPLY : {
					keyInputs.add("*");
				} break;
				case KEY_NUMPAD_SUBTRACT : {
					keyInputs.add("-");
				} break;
				case KEY_NUMPAD_ADD : {
					keyInputs.add("+");
				} break;
				case KEY_NUMPAD_DECIMAL : {
					keyInputs.add(".");
				} break;
				case KEY_NUMPAD_0 : {
					keyInputs.add("0");
				} break;
				case KEY_NUMPAD_1 : {
					keyInputs.add("1");
				} break;
				case KEY_NUMPAD_2 : {
					keyInputs.add("2");
				} break;
				case KEY_NUMPAD_3 : {
					keyInputs.add("3");
				} break;
				case KEY_NUMPAD_4 : {
					keyInputs.add("4");
				} break;
				case KEY_NUMPAD_5 : {
					keyInputs.add("5");
				} break;
				case KEY_NUMPAD_6 : {
					keyInputs.add("6");
				} break;
				case KEY_NUMPAD_7 : {
					keyInputs.add("7");
				} break;
				case KEY_NUMPAD_8 : {
					keyInputs.add("8");
				} break;
				case KEY_NUMPAD_9 : {
					keyInputs.add("9");
				} break;
				case KEY_SEMICOLON : {
					keyInputs.add(";");
				} break;
				case KEY_EQUALS : {
					keyInputs.add("=");
				} break;
				case KEY_COMMA : {
					keyInputs.add(",");
				} break;
				case KEY_HYPHEN : {
					keyInputs.add("-");
				} break;
				case KEY_DOT : {
					keyInputs.add(".");
				} break;
				case KEY_SLASH : {
					keyInputs.add("/");
				} break;
				case KEY_BACKSLASH : {
					keyInputs.add("\\");
				} break;
				case KEY_BACKTICK : {
					keyInputs.add("`");
				} break;
				case KEY_SQUAREBRACKET_OPEN : {
					keyInputs.add("[");
				} break;
				case KEY_SQUAREBRACKET_CLOSE : {
					keyInputs.add("]");
				} break;
				case KEY_SINGLEQUOTE : {
					keyInputs.add("'");
				} break;
				default : {
					if (isLetter(keyCode) || isDigit(keyCode)) {
						keyInputs.add(Character.toString((char)keyCode));
					}
				} break;
			}
		}
		return keyInputs;
	}
    // CHECKSTYLE.ON: CyclomaticComplexity

	private static boolean isLetter(int keyCode) {
		return (keyCode >= 65) && (keyCode <= 90);
	}

	private static boolean isDigit(int keyCode) {
		return (keyCode >= 48) && (keyCode <= 57);
	}
}