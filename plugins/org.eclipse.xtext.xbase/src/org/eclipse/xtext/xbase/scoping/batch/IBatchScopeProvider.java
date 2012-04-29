/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.scoping.batch;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.scoping.IScopeProvider;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

import com.google.inject.ImplementedBy;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 * TODO JavaDoc, remove ImplementedBy annotation
 */
@ImplementedBy(XbaseBatchScopeProvider.class)
public interface IBatchScopeProvider extends IScopeProvider {

	/**
	 * Returns a preconfigured feature scope session that is aware of
	 * implicitly imported types such as {@link CollectionLiterals}.
	 * @return a new feature scope session. Never <code>null</code>.
	 */
	IFeatureScopeSession newSession(Resource context);
	
}
