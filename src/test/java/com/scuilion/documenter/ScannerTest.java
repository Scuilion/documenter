package com.scuilion.documenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class ScannerTest{
    
    //visitExecutable, visitPackage, visitType, visitTypeParameter, visitVariable
    @Mock private ExecutableElement executableElement;
    @Mock private PackageElement packageElement;
    @Mock private TypeElement typeElement;
    @Mock private TypeParameterElement typeParameterElement;
    @Mock private VariableElement variableElement;
    @Spy private static Scanner scanner;// = new Scanner();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void visitVariableTest(){
        resetupMockDocument(variableElement, 50, "variable");
        
        Map<String, Note> documents = new HashMap<>();
        scanner.visitVariable(variableElement, documents);

        verify(scanner).addDocument(any(Element.class), anyStringSetMap());//anyListOf(Note.class));
//        assertEquals(50, documents.get(0).getPriority());
        Note actual = documents.get("variableElement.variable");
        assertEquals(50, actual.getPriority());
        assertEquals("variableElement.variable", actual.getKey());
    }
    private static Map<String, Note> anyStringSetMap() {
    	  return any();
    }
    @Test
    public void visitExecutableTest(){
        resetupMockDocument(executableElement, 40, "executable");
        when(executableElement.getEnclosingElement()).thenReturn(executableElement);

        Map<String, Note> documents = new HashMap<>();
        scanner.visitExecutable(executableElement, documents);

        verify(scanner).addDocument(any(Element.class), anyStringSetMap());
        Note actual = documents.get("executableElement.executable");
        assertEquals(40, actual.getPriority());
        assertEquals("executableElement.executable", actual.getKey());
    }

    @Test
    public void visitPackageElementTest(){
        resetupMockDocument(packageElement, 30, "package");
        
        Map<String, Note> documents = new HashMap<>();
        scanner.visitPackage(packageElement, documents);

        verify(scanner).addDocument(any(Element.class), anyStringSetMap());

        Note actual = documents.get("packageElement.package");
        assertEquals(30, actual.getPriority());
        assertEquals("packageElement.package", actual.getKey());
    }

    @Test
    public void visitTypeTest(){
        resetupMockDocument(typeElement, 10, "type");
        
        Map<String, Note> documents = new HashMap<>();
        scanner.visitType(typeElement, documents);

        verify(scanner).addDocument(any(Element.class), anyStringSetMap());
        Note actual = documents.get("typeElement.type");
        assertEquals(10, actual.getPriority());
        assertEquals("typeElement.type", actual.getKey());
    }
    
    @Test
    public void visitTypeParameterTest(){
        resetupMockDocument(typeParameterElement, 20, "parameter");

        Map<String, Note> documents = new HashMap<>();
        scanner.visitTypeParameter(typeParameterElement, documents);

        verify(scanner).addDocument(any(Element.class), anyStringSetMap());
        Note actual = documents.get("typeParameterElement.parameter");
        assertEquals(20, actual.getPriority());
        assertEquals("typeParameterElement.parameter", actual.getKey());
    }

    private void resetupMockDocument(Element e, int priority, String key) {
        Document document = mock(Document.class);
        when(document.priority()).thenReturn(priority);
        when(document.key()).thenReturn(key);

        when(e.getAnnotation(Document.class)).thenReturn(document);
    }

}

