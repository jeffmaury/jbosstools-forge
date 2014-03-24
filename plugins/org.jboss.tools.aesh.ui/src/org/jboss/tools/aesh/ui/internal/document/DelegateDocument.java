package org.jboss.tools.aesh.ui.internal.document;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.text.Document;

public class DelegateDocument extends Document {
	
	public interface CursorListener {
		void cursorMoved();
	}
	
	private Set<CursorListener> cursorListeners = new HashSet<CursorListener>();
	
	void moveCursorTo(int newOffset) {
		for (CursorListener listener : cursorListeners) {
			listener.cursorMoved();
		}
	}

	public void addCursorListener(CursorListener listener) {
		cursorListeners.add(listener);
	}
	
	public void removeCursorListener(CursorListener listener) {
		cursorListeners.remove(listener);
	}
	
}