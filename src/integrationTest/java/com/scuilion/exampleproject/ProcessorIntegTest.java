package com.scuilion.exampleproject;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Test;
import org.mockito.Mock;

import com.scuilion.documenter.AnnotationProcessor;

public class ProcessorIntegTest {

    @Mock
    AnnotationProcessor annotationProcessor;
    

    @Test
    public void createCompilerTest() {

        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                throw new RuntimeException("No system java compiler available.\n Verify that you are running using the jdk, NOT the jre.");
            }

            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

            File dir = getFilesToCompile();
            Collection<File> javaFiles = FileUtils.listFiles(dir, new RegexFileFilter(".*\\.java$"), TrueFileFilter.INSTANCE);

            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(javaFiles);
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, getOptions(), null, compilationUnits);

            assertTrue(task.call());

            fileManager.close();

        } catch (Exception e) {
            System.out.println("failure");
            e.printStackTrace();
        }
    }
    
    private File getRootDir() {
        Path currentRelativePath = Paths.get("");
        return currentRelativePath.toAbsolutePath().toFile();
    }

    private File getFilesToCompile() {
        return new File(getRootDir(), "src/integrationTest/resources/projectRoot");
    }

    private List<String> getOptions() throws IOException {
        List<String> options = new ArrayList<>();
        options.add("-d");
        options.add(getBuildDir());
        return options;

    }
    private String getBuildDir() {
        File buildDir = new File(getRootDir(), "build");
        if (!buildDir.exists()) {
            buildDir.mkdir();
        }
        return buildDir.getAbsolutePath().toString();
    }
}
