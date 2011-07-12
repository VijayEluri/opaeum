/*******************************************************************************
 * Copyright (c) 2007 Anyware Technologies. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jacques Lescot (Anyware Technologies) - initial API and
 * implementation
 ******************************************************************************/
package org.topcased.modeler.uml.internal.ui.navigator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory;

/**
 * Provides the shared adapter factory for the content and label provider of an UML model file
 * 
 * Creation 4 janv. 07
 * 
 * @author <a href="mailto:jacques.lescot@anyware-tech.com">Jacques LESCOT</a>
 */
public final class UMLModelAdapterFactoryProvider
{
    private static ComposedAdapterFactory adapterFactory;

    /**
     * Private constructor
     */
    private UMLModelAdapterFactoryProvider()
    {
        // Do nothing
    }

    /**
     * Create the list of AdapterFactory
     * 
     * @return a list of AdapterFactory
     */
    public static List<AdapterFactory> createFactoryList()
    {
        List<AdapterFactory> factories = new ArrayList<AdapterFactory>();

        // These are the custom provider generated by EMF
        factories.add(new UMLItemProviderAdapterFactory());

        // These are EMF generic providers
        factories.add(new ResourceItemProviderAdapterFactory());
        factories.add(new ReflectiveItemProviderAdapterFactory());

        return factories;
    }

    /**
     * Return the single instance of ComposedAdapterFactory
     * 
     * @return ComposedAdapterFactory the single instance
     */
    public static ComposedAdapterFactory getAdapterFactory()
    {
        if (adapterFactory == null)
        {
            adapterFactory = new ComposedAdapterFactory(createFactoryList());
        }
        return adapterFactory;
    }
}
