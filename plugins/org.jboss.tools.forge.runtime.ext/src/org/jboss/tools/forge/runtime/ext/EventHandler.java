/**
 * Copyright (c) Red Hat, Inc., contributors and others 2004 - 2014. All rights reserved
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.tools.forge.runtime.ext;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.forge.project.Project;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.events.CommandExecuted;

public class EventHandler {
	
	private static final String ESCAPE = new String(new char[] { 27, '%'} );
	
	@Inject
	private Shell shell;
	
	private static boolean ENABLED = true;
	
	public void handleCommandExecuted(@Observes CommandExecuted event) {
		if (!ENABLED) return;
		Resource<?> currentResource = shell.getCurrentResource();
		String currentResourceName = currentResource.getFullyQualifiedName();
		String currentResourceType = currentResource.getClass().getSimpleName();
		Project project = shell.getCurrentProject();
		String projectName = "";
		if (project != null) {
			projectName = project.getProjectRoot().getFullyQualifiedName();
		}
		String parameterString = getParameterString(event);
		String command = event.getCommand().getParent().getName() + " " + event.getCommand().getName();
		sendEscaped(
				" EC: " + command + 
				" CRN: " + currentResourceName + 
				" CRT: " + currentResourceType + 
				" CPN: " + projectName +
				" PAR: " + parameterString);
	}
	
	public static void setEnabled(boolean b) {
		ENABLED = b;
	}
	
	public static boolean isEnabled() {
		return ENABLED;
	}
	
	
	private String getParameterString(CommandExecuted event) {
		return flattenObjectArray(event.getParameters());
	}
	
	private String flattenObjectArray(Object[] objects) {
		String result = "";
		for (Object object : objects) {
			if (object instanceof Object[]) {
				result += '[' + flattenObjectArray((Object[])object) + ']';
			} else {
				result += object;
				result += ' ';
			}
		}
		return result.trim();
	}
	
	private void sendEscaped(String str) {
		synchronized(shell) {
			shell.print(ESCAPE + str + ESCAPE); 
		}
	}

}
