/**
 * Copyright (c) Red Hat, Inc., contributors and others 2004 - 2014. All rights reserved
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.tools.forge.ui.internal.ext.quickaccess;

import org.eclipse.jface.resource.DeviceResourceException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jboss.tools.forge.ui.internal.ForgeUIPlugin;

class QuickAccessEntry {
	boolean firstInCategory;
	boolean lastInCategory;
	QuickAccessElement element;
	QuickAccessProvider provider;
	int[][] elementMatchRegions;
	int[][] providerMatchRegions;

	QuickAccessEntry(QuickAccessElement element, QuickAccessProvider provider,
			int[][] elementMatchRegions, int[][] providerMatchRegions) {
		this.element = element;
		this.provider = provider;
		this.elementMatchRegions = elementMatchRegions;
		this.providerMatchRegions = providerMatchRegions;
	}

	Image getImage(QuickAccessElement element, ResourceManager resourceManager) {
		Image image = findOrCreateImage(element.getImageDescriptor(),
				resourceManager);
		if (image == null) {
			image = ForgeUIPlugin.getForgeIcon().createImage();
		}
		return image;
	}

	private Image findOrCreateImage(ImageDescriptor imageDescriptor,
			ResourceManager resourceManager) {
		if (imageDescriptor == null) {
			return null;
		}
		Image image = (Image) resourceManager.find(imageDescriptor);
		if (image == null) {
			try {
				image = resourceManager.createImage(imageDescriptor);
			} catch (DeviceResourceException e) {
				ForgeUIPlugin.log(e);
			}
		}
		return image;
	}

	public void measure(Event event, TextLayout textLayout,
			ResourceManager resourceManager, TextStyle boldStyle) {
		Table table = ((TableItem) event.item).getParent();
		textLayout.setFont(table.getFont());
		event.width = 0;
		switch (event.index) {
		case 0:
			if (firstInCategory || providerMatchRegions.length > 0) {
				textLayout.setText(provider.getName());
				if (boldStyle != null) {
					for (int i = 0; i < providerMatchRegions.length; i++) {
						int[] matchRegion = providerMatchRegions[i];
						textLayout.setStyle(boldStyle, matchRegion[0],
								matchRegion[1]);
					}
				}
			} else {
				textLayout.setText(""); //$NON-NLS-1$
			}
			break;
		case 1:
			Image image = getImage(element, resourceManager);
			Rectangle imageRect = image.getBounds();
			event.width += imageRect.width + 4;
			event.height = Math.max(event.height, imageRect.height + 2);
			textLayout.setText(element.getLabel());
			if (boldStyle != null) {
				for (int i = 0; i < elementMatchRegions.length; i++) {
					int[] matchRegion = elementMatchRegions[i];
					textLayout.setStyle(boldStyle, matchRegion[0],
							matchRegion[1]);
				}
			}
			break;
		}
		Rectangle rect = textLayout.getBounds();
		event.width += rect.width + 4;
		event.height = Math.max(event.height, rect.height + 2);
	}

	public void paint(Event event, TextLayout textLayout,
			ResourceManager resourceManager, TextStyle boldStyle,
			Color grayColor) {
		final Table table = ((TableItem) event.item).getParent();
		textLayout.setFont(table.getFont());
		switch (event.index) {
		case 0:
			if (firstInCategory || providerMatchRegions.length > 0) {
				textLayout.setText(provider.getName());
				if (boldStyle != null) {
					for (int i = 0; i < providerMatchRegions.length; i++) {
						int[] matchRegion = providerMatchRegions[i];
						textLayout.setStyle(boldStyle, matchRegion[0],
								matchRegion[1]);
					}
				}
				if (grayColor != null && providerMatchRegions.length > 0
						&& !firstInCategory) {
					event.gc.setForeground(grayColor);
				}
				Rectangle availableBounds = ((TableItem) event.item)
						.getTextBounds(event.index);
				Rectangle requiredBounds = textLayout.getBounds();
				textLayout
						.draw(event.gc,
								availableBounds.x + 1,
								availableBounds.y
										+ (availableBounds.height - requiredBounds.height)
										/ 2);
			}
			break;
		case 1:
			String label = element.getLabel();
			Image image = getImage(element, resourceManager);
			event.gc.drawImage(image, event.x + 1, event.y + 1);
			textLayout.setText(label);
			if (boldStyle != null) {
				for (int i = 0; i < elementMatchRegions.length; i++) {
					int[] matchRegion = elementMatchRegions[i];
					textLayout.setStyle(boldStyle, matchRegion[0],
							matchRegion[1]);
				}
			}
			Rectangle availableBounds = ((TableItem) event.item)
					.getTextBounds(event.index);
			Rectangle requiredBounds = textLayout.getBounds();
			textLayout.draw(event.gc, availableBounds.x + 1
					+ image.getBounds().width, availableBounds.y
					+ (availableBounds.height - requiredBounds.height) / 2);
			break;
		}
		if (lastInCategory) {
			if (grayColor != null)
				event.gc.setForeground(grayColor);
			Rectangle bounds = ((TableItem) event.item).getBounds(event.index);
			event.gc.drawLine(Math.max(0, bounds.x - 1), bounds.y
					+ bounds.height - 1, bounds.x + bounds.width, bounds.y
					+ bounds.height - 1);
		}
	}

	/**
	 * @param event
	 */
	public void erase(Event event) {
		// We are only custom drawing the foreground.
		event.detail &= ~SWT.FOREGROUND;
	}
}