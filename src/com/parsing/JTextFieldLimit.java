package com.parsing;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

class JTextFieldLimit extends PlainDocument {
	
	private int limit;
	JTextFieldLimit(int limit) {
		//super();
		this.limit = limit;
	}
	
	@Override
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {

		if ((getLength() ) < limit) {
			super.insertString(offset, str, attr);
    	}
  	}
}