/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.scoping.batch;

import java.util.Collections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.xbase.XConstructorCall;
import org.eclipse.xtext.xbase.XbasePackage;
import org.eclipse.xtext.xbase.typesystem.IResolvedTypes;
import org.eclipse.xtext.xbase.typesystem.util.IVisibilityHelper;

/**
 * Encapsulates the creation of constructor scopes.
 * 
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class ConstructorScopes extends DelegatingScopes {

	public static final int CONSTRUCTOR_BUCKET = 1;
	
	/**
	 * Creates the constructor scope for {@link XConstructorCall}.
	 * The scope will likely contain descriptions for {@link JvmConstructor constructors}.
	 * If there is not constructor declared, it may contain {@link JvmType types}.
	 * 
	 * @param session the currently available visibilityHelper data
	 * @param reference the reference that will hold the resolved constructor
	 * @param resolvedTypes the currently known resolved types
	 */
	public IScope createConstructorScope(EObject context, EReference reference, IFeatureScopeSession session, IResolvedTypes resolvedTypes) {
		if (!(context instanceof XConstructorCall)) {
			return IScope.NULLSCOPE;
		}
		/*
		 * We use a type scope here in order to provide better feedback for users,
		 * e.g. if the constructor call refers to an interface or a primitive.
		 */
		final IScope delegateScope = getDelegate().getScope(context, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE__TYPE);
		IScope result = new ConstructorTypeScopeWrapper(context, session, delegateScope);
		return result;
	}
	
	/**
	 * Custom languages that allow to infer anonymous classes may want to use this helper
	 * to access the constructors of those classes.
	 */
	protected IScope createAnonymousClassConstructorScope(final JvmGenericType anonymousType, EObject context, final IFeatureScopeSession session) {
		// we don't care about the type scope since the type is well known here
		IVisibilityHelper protectedIsVisible = new IVisibilityHelper() {
			public boolean isVisible(@NonNull JvmMember member) {
				return member.getVisibility() == JvmVisibility.PROTECTED || member.getVisibility() == JvmVisibility.PUBLIC || session.isVisible(member);
			}
		};
		return new ConstructorTypeScopeWrapper(context, protectedIsVisible, IScope.NULLSCOPE) {
			@Override
			public Iterable<IEObjectDescription> getElements(EObject object) {
				throw new UnsupportedOperationException("TODO implement as necessary");
			}
			@Override
			public Iterable<IEObjectDescription> getElements(QualifiedName name) {
				IEObjectDescription typeDescription = EObjectDescription.create(name, anonymousType);
				return createFeatureDescriptions(Collections.singletonList(typeDescription));
			}
		};
	}
	
	public boolean isConstructorCallScope(EReference reference) {
		return reference == XbasePackage.Literals.XCONSTRUCTOR_CALL__CONSTRUCTOR;
	}
	
}
