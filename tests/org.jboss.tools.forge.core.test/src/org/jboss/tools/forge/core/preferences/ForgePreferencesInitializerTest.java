package org.jboss.tools.forge.core.preferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.jboss.tools.forge.core.internal.preferences.ForgePreferencesInitializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ForgePreferencesInitializerTest {
	
	private static final String FORGE_CORE_PLUGIN_ID = "org.jboss.tools.forge.core";
	
	private IEclipsePreferences preferences;
	private String preferredRuntime;
	
	@Before
	public void setUp() {
		preferences = InstanceScope.INSTANCE.getNode(FORGE_CORE_PLUGIN_ID);
		preferredRuntime = preferences.get(ForgeRuntimesPreferences.PREF_FORGE_RUNTIMES, null);
		preferences.remove(ForgeRuntimesPreferences.PREF_FORGE_RUNTIMES);
	}
	
	@After
	public void tearDown() {
		if (preferredRuntime != null) {
			preferences.put(
					ForgeRuntimesPreferences.PREF_FORGE_RUNTIMES, 
					preferredRuntime);
		}
	}
	
	@Test
	public void testInitializeDefaultPreferences() {
		assertNull(preferences.get(ForgeRuntimesPreferences.PREF_FORGE_RUNTIMES, null));
		new ForgePreferencesInitializer().initializeDefaultPreferences();
		assertEquals(
				ForgePreferencesInitializer.INITIAL_RUNTIMES_PREFERENCE, 
				preferences.get(ForgeRuntimesPreferences.PREF_FORGE_RUNTIMES, null));
	}

}
