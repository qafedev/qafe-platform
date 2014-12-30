package com.qualogy.qafe.gwt.client.vo.handlers;

public enum BuiltInState {
	EXECUTED("EXECUTED"), SUSPEND("SUSPEND"), REPEAT("REPEAT");
	
	private String literal;
	
	private BuiltInState(String literal) {
		this.literal = literal;
	}
	
	public String getLiteral() {
		return literal;
	}
}
