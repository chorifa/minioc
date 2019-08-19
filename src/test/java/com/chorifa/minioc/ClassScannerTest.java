package com.chorifa.minioc;

import com.chorifa.minioc.utils.io.ClassScanner;
import org.junit.Test;

import javax.inject.Named;
import java.util.List;

public class ClassScannerTest {

	@Test
	public void scannerTest(){
		List<String> list = ClassScanner.getAllClassName(this.getClass().getPackage().getName(),true, false);
		list.forEach(System.out::println);
		for(String s : list){
			try {
				Class<?> clazz = Class.forName(s);
				Named named;
				if( (named = clazz.getAnnotation(Named.class)) != null){
					System.out.println("Class -> "+s+" has @Named annotation so called >>> "+named.value());
				}
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
