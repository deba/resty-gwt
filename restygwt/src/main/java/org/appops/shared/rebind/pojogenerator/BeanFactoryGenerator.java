package org.appops.shared.rebind.pojogenerator;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

import org.appops.shared.client.core.Property;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class BeanFactoryGenerator {

	private GeneratorContext context;
	private TreeLogger logger;
	private PrintWriter interfacePrintWriter;
	private PrintWriter implPrintWriter;
	private SourceWriter interfaceSourceWriter;
	private SourceWriter implSourceWriter;
	
	public BeanFactoryGenerator(TreeLogger logger, GeneratorContext context) {
		this.logger = logger;
		this.context = context;
	}
	
	public void generate(String packageName) {
		try {
			if(interfacePrintWriter == null & implPrintWriter == null & interfaceSourceWriter == null & implSourceWriter == null) {
				generateBeanFactoryInterface(packageName);
				generateBeanFactoryImpl(packageName);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void generateBeanFactoryInterface(String packageName) {
		try {
			String className = "BeanFactory";
			interfacePrintWriter = context.tryCreate(logger, packageName, className);
			if (interfacePrintWriter == null)
				return;
			ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, className);
			composer.makeInterface();
			interfaceSourceWriter = composer.createSourceWriter(context, interfacePrintWriter);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void generateBeanFactoryImpl(String packageName) {
		try {
			String className = "BeanFactoryImpl";
			implPrintWriter = context.tryCreate(logger, packageName, className);
			if (implPrintWriter == null)
				return;
			ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, className);
			composer.addImplementedInterface("BeanFactory");
			composer.addImport(ArrayList.class.getCanonicalName());
			composer.addImport(Property.class.getCanonicalName());
			composer.addImport(Serializable.class.getCanonicalName());
			implSourceWriter = composer.createSourceWriter(context, implPrintWriter);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addCreateMethod(String pojoName, ArrayList<String> fieldList) {
		try {
			String parameter = "";
			for (String field : fieldList) {
				int index = fieldList.indexOf(field);
				if(index == 0) {
					parameter = field;
				} else {
					parameter = parameter + ", " + field;
				}
			}
			createMethodInInterface(pojoName, parameter);
			createMethodInImpl(pojoName, parameter, fieldList);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createMethodInImpl(String pojoName, String parameter, ArrayList<String> fieldList) {
		try {
			String propertyCanonicalName = Property.class.getSimpleName() + "<" + Serializable.class.getSimpleName() + ">";
			String propertyListName = Property.class.getSimpleName().toLowerCase() + "List";
			implSourceWriter.println();
			implSourceWriter.println("@Override");
			implSourceWriter.println("public " + pojoName + " create" + pojoName + "(" + parameter + ") {");
			implSourceWriter.indent();
			implSourceWriter.println(ArrayList.class.getSimpleName() + "<" + propertyCanonicalName + "> " + propertyListName + " = new " + ArrayList.class.getSimpleName() + "<" + propertyCanonicalName + ">();");
			for (String field : fieldList) {
				String[] strArr = field.split(" ");
				field = strArr[1];
				implSourceWriter.println(propertyListName + ".add(new " + Property.class.getSimpleName() + "(\"" + field + "\", " + field + "));");
			}
			implSourceWriter.println(propertyCanonicalName + "[] propertyArray = (" + propertyCanonicalName + "[]) " + propertyListName + ".toArray();");
			implSourceWriter.println(pojoName + " " + pojoName.toLowerCase() + " = new " + pojoName + "Pojo(propertyArray);");
			implSourceWriter.println("return " + pojoName.toLowerCase() + ";");
			implSourceWriter.outdent();
			implSourceWriter.println("}");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createMethodInInterface(String pojoName, String parameter) {
		try {
			interfaceSourceWriter.println();
			interfaceSourceWriter.println(pojoName + " create" + pojoName + "(" + parameter + ");");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void commit() {
		try {
			if(interfacePrintWriter != null & interfaceSourceWriter != null) {
				interfaceSourceWriter.outdent();
				interfaceSourceWriter.println("}");
				context.commit(logger, interfacePrintWriter);
			}
			
			if(implPrintWriter != null & implSourceWriter != null) {
				implSourceWriter.outdent();
				implSourceWriter.println("}");
				context.commit(logger, implPrintWriter);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
