package org.eclipse.xtext.xbase.tests.jvmmodel

import com.google.inject.Inject
import org.eclipse.xtext.common.types.JvmAnnotationType
import org.eclipse.xtext.common.types.JvmStringAnnotationValue
import org.eclipse.xtext.common.types.TypesFactory
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.xbase.annotations.xAnnotations.XAnnotationsFactory
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.eclipse.xtext.xbase.tests.AbstractXbaseTestCase

import static junit.framework.Assert.*

class JvmTypesBuilderTest extends AbstractXbaseTestCase {
	
	@Inject TypeReferences references
	
	@Inject extension JvmTypesBuilder
	
	def void testEmptyAnnotation() {
		val f = XAnnotationsFactory::eINSTANCE
		val typesFactory = TypesFactory::eINSTANCE
		val e = expression("'Foo'");
		
		val anno = f.createXAnnotation;
		anno.annotationType = references.findDeclaredType(typeof(Inject), e) as JvmAnnotationType
		val type = typesFactory.createJvmGenericType
		newArrayList(anno).translateAnnotationsTo(type)
		
		assertEquals(anno.annotationType, type.annotations.head.annotation)
	}
	
	def void testStringAnnotation() {
		val f = XAnnotationsFactory::eINSTANCE
		val typesFactory = TypesFactory::eINSTANCE
		val e = expression("'Foo'");
		
		val anno = f.createXAnnotation;
		anno.annotationType = references.findDeclaredType(typeof(Inject), e) as JvmAnnotationType
		anno.value = e
		val type = typesFactory.createJvmGenericType
		newArrayList(anno).translateAnnotationsTo(type)
		
		assertEquals(anno.annotationType, type.annotations.head.annotation)
		assertEquals("Foo", (type.annotations.head.values.head as JvmStringAnnotationValue).values.head)
	}
	
	def void testStringArrayAnnotation() {
		val f = XAnnotationsFactory::eINSTANCE
		val typesFactory = TypesFactory::eINSTANCE
		val e = expression("'Foo'");
		val e2 = expression("'Bar'");
		
		val anno = f.createXAnnotation
		anno.annotationType = references.findDeclaredType(typeof(Inject), e) as JvmAnnotationType
		val array = f.createXAnnotationValueArray
		anno.value = array
		array.values += e
		array.values += e2
		
		val type = typesFactory.createJvmGenericType
		newArrayList(anno).translateAnnotationsTo(type)
		
		assertEquals(anno.annotationType, type.annotations.head.annotation)
		assertEquals("Foo", (type.annotations.head.values.head as JvmStringAnnotationValue).values.head)
		assertEquals("Bar", (type.annotations.head.values.head as JvmStringAnnotationValue).values.get(1))
	}
}