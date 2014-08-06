/**
 * Copyright (c) Red Hat, Inc., contributors and others 2004 - 2014. All rights reserved
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.tools.aesh.core.internal.ansi;

import org.jboss.tools.aesh.core.document.Document;


public class RestoreCursorPosition extends AbstractCommand {

	public RestoreCursorPosition(String arguments) {}

	@Override
	public CommandType getType() {
		return CommandType.RESTORE_CURSOR_POSITION;
	}
	
	@Override
	public void handle(Document document) {
		document.restoreCursor();
	}

}
