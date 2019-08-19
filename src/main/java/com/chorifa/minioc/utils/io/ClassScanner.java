package com.chorifa.minioc.utils.io;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {
    private static final String CLASS_SUFFIX = ".class";

    public static List<String> getAllClassName(String packageName, boolean recursive, boolean allowInnerClass){
        List<String> names = new ArrayList<>();
        String suffixPath = packageName.replace('.','/');
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = loader.getResources(suffixPath);
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                if(url != null){
                    switch (url.getProtocol()){
                        case "file":
                            getAllClassNameInFile(packageName, new File(url.getPath()), recursive, allowInnerClass, names);
                            break;
                        case "jar":
                        	JarFile jarFile = null;
                        	try {
                        		jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                        		if(jarFile != null) {
			                        getAllClassNameInJar(packageName, jarFile, recursive, allowInnerClass, names);
			                        jarFile.close();
		                        }
	                        }catch (IOException e){
                        		e.printStackTrace();
	                        }
                            break;
                        default: break;
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return names;
    }

    private static void getAllClassNameInFile(String packageName, File dir, boolean recursive, boolean allowInnerClass, List<String> list){
        if(dir == null || !dir.exists() || !dir.isDirectory()) return; // dir not allow to be File
        // get all files(dictionary include)
        File[] files = dir.listFiles(file -> (recursive && file.isDirectory()) || (file.getName().endsWith(CLASS_SUFFIX)));
        if(files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getAllClassNameInFile(packageName + "." + file.getName(), file, recursive, allowInnerClass, list);
                } else { // must be .class
                	if(allowInnerClass || file.getName().indexOf('$') == -1)
                		list.add(packageName + '.' + file.getName().substring(0, file.getName().length() - CLASS_SUFFIX.length()));
                }
            }
        }
    }

    private static void getAllClassNameInJar(String packageName, JarFile jarFile, boolean recursive, boolean allowInnerClass, List<String> list){
    	Enumeration<JarEntry> entries = jarFile.entries();
    	while(entries.hasMoreElements()){
    		String name = entries.nextElement().getName();
    		if(name.endsWith(CLASS_SUFFIX)){ // must be .class
    			name = name.substring(0,name.length()-CLASS_SUFFIX.length()).replace('/','.'); // class name
    			if(recursive){
    				if(name.startsWith(packageName) && (allowInnerClass || name.indexOf('$') == -1))
    					list.add(name);
			    }else{ // must be packageName.className
    				if(packageName.equals(name.substring(0,name.lastIndexOf('.'))) && (allowInnerClass || name.indexOf('$') == -1))
    					list.add(name);
			    }
		    }
	    }
    }

}
