package org.appops.shared.rebind.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

public class SuperClassTypeScanner {
	
	public static List<JClassType> scan(TreeLogger logger, TypeOracle typeOracle, Class<?> superclass) {
		
		List<JClassType> listOfClasses = new ArrayList<JClassType>();
		int nbClasses = 0;
		
		Date start = new Date();
		
		for (JPackage pack : typeOracle.getPackages()) {
			for (JClassType type : pack.getTypes()) {
				if (type.getSuperclass() != null/* && type.getName().equals("PlaySnippet")*/) {
					if (type.getSuperclass().getQualifiedSourceName().equals(superclass.getCanonicalName()))
						listOfClasses.add(type);
				}
				
				JClassType[] implementedInterfaces = type.getImplementedInterfaces();
				for (int i = 0; i < implementedInterfaces.length; i++) {
					if (implementedInterfaces[i].getQualifiedSourceName().equals(superclass.getCanonicalName()))
						listOfClasses.add(type);
				}
				nbClasses += implementedInterfaces.length;
			}
			nbClasses += pack.getTypes().length;
		}
		Date end = new Date();
		logger.log(TreeLogger.INFO, nbClasses + " classes scanned in " + Long.toString(end.getTime() - start.getTime()) + " ms.");
		
		return listOfClasses;
	}
	
}
