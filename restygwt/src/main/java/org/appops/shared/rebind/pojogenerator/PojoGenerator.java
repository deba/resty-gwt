package org.appops.shared.rebind.pojogenerator;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.appops.shared.client.core.Entity;
import org.appops.shared.client.core.EntityPojo;
import org.appops.shared.client.core.EntityType;
import org.appops.shared.client.core.Property;
import org.appops.shared.rebind.util.SuperClassTypeScanner;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/***
 * 
 * @author komal@ensarm.com This class finds classes annotated with @Pojo and generates an implementation that works
 *         with entity.
 */
public class PojoGenerator extends Generator {

	private GeneratorContext context;
	private TypeOracle typeOracle;
	private TreeLogger logger;
	private PojoTypeGeneratorHelper typeGeneratorHelper = new PojoTypeGeneratorHelper();
	private String packageName;
	private String className;
	private JClassType type;
	private final String pojo = "Pojo";
	private BeanFactoryGenerator factoryGen;

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName)
			throws UnableToCompleteException {
		try {
			this.logger = logger;
			this.context = context;
			this.typeOracle = context.getTypeOracle();
			generatePojoTypes();
		}
		catch (Exception e) {

		}
		return null;
	}

	private void generatePojoTypes() {
		// find all the classes annotated with Pojo
		// generate its implementation with Entity
		factoryGen = new BeanFactoryGenerator(logger, context);
		
		/*Class[] clazzarray = { Pojo.class };
		Map<Class<? extends Annotation>, List<JClassType>> map = AnnotationScanner.scan(logger, typeOracle, clazzarray);
		List<JClassType> list = map.get(Pojo.class);*/
		List<JClassType> list = SuperClassTypeScanner.scan(logger, typeOracle, Entity.class);
		for (JClassType jClassType : list) {
			try {
				if(jClassType.isInterface() != null) {
					createPojo(jClassType);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		factoryGen.commit();
	}

	private void createPojo(JClassType jClassType) {
		
		ArrayList<String> fieldList = new ArrayList<String>();
		/*Pojo pojoAnnotation = jClassType.getAnnotation(Pojo.class);
		String typeClassName = jClassType.getSimpleSourceName() + "Type";
		if (pojoAnnotation.frindlyTypeName() != "")
			typeClassName = pojoAnnotation.frindlyTypeName();*/

		//String typePackageName = "in.appops.trial.type";

		this.type = jClassType;
		packageName = type.getPackage().getName();
		className = type.getName() + pojo;

		PrintWriter printWriter = context.tryCreate(logger, packageName + ".generated", className);
		if (printWriter == null)
			return;
		ClassSourceFileComposerFactory composer = null;
		composer = new ClassSourceFileComposerFactory(packageName + ".generated", className);

		// extend from EntityPojo
		composer.setSuperclass(EntityPojo.class.getSimpleName());
		composer.addImplementedInterface(type.getName());

		//add imports
		//composer.addImport(Entity.class.getCanonicalName());
		composer.addImport(EntityPojo.class.getCanonicalName());
		composer.addImport(packageName + "." +type.getName());
		composer.addImport(EntityType.class.getCanonicalName());
		composer.addImport(Property.class.getCanonicalName());
		composer.addImport(Serializable.class.getCanonicalName());
		//composer.addImport(typePackageName + "." + typeClassName);

		SourceWriter sourceWriter = composer.createSourceWriter(context, printWriter);

		//add constructor
		sourceWriter.println();
		sourceWriter.println(className + "(){ ");
		sourceWriter.indent();
		sourceWriter.println("this.setType(new EntityType(\"" + type.getName() + "\", \"" + packageName + "." +type.getName() + "\"));");
		//sourceWriter.println("this.setType(new " + typeClassName + "());");
		//sourceWriter.println("this.setType(new MetaType(\"" + jClassType.getQualifiedSourceName() + "\"));");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();

		sourceWriter.println("public " + className + "(EntityPojo entityPojo){ ");
		sourceWriter.indent();
		//sourceWriter.println("this.setType(new " + typeClassName + "());");
		sourceWriter.println("this.setType(new EntityType(\"" + type.getName() + "\", \"" + packageName + "." +type.getName() + "\"));");
		sourceWriter.println("this.setPropertyValue(ENTITYPOJO, entityPojo);");
		sourceWriter.outdent();
		sourceWriter.println("}");

		JMethod[] methods = jClassType.getMethods();
		for (JMethod method : methods) {
			try {
				String fieldNameWithType = genAccessMethods(method, sourceWriter);
				if(!fieldList.contains(fieldNameWithType)) {
					fieldList.add(fieldNameWithType);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		String parameter = "";
		for (String field : fieldList) {
			int index = fieldList.indexOf(field);
			if(index == 0) {
				parameter = field;
			} else {
				parameter = parameter + ", " + field;
			}
		}
		
		sourceWriter.println();
		sourceWriter.println("public " + className + "(" + Property.class.getSimpleName() + "<" + Serializable.class.getSimpleName() + ">... propertyArr" + ") {");
		sourceWriter.indent();
		sourceWriter.println("super(propertyArr);");
		sourceWriter.println("this.setType(new EntityType(\"" + type.getName() + "\", \"" + packageName + "." +type.getName() + "\"));");
		sourceWriter.outdent();
		sourceWriter.println("}");
		/*sourceWriter.println();
		sourceWriter.println("public " + className + "(" + parameter + ") {");
		sourceWriter.indent();
		sourceWriter.println("this.setType(new EntityType(\"" + type.getName() + "\", \"" + packageName + "." +type.getName() + "\"));");
		for (String field : fieldList) {
			String[] strArr = field.split(" ");
			field = strArr[1];
			String firstLetter = String.valueOf(field.charAt(0));
			String methodCall = field.replaceFirst(firstLetter, firstLetter.toUpperCase());
			sourceWriter.println("this.set" + methodCall + "(" + field + ");");
		}
		sourceWriter.outdent();
		sourceWriter.println("}");*/
		
		sourceWriter.outdent();
		sourceWriter.println("}");
		context.commit(logger, printWriter);
		
		factoryGen.generate(packageName + ".generated");
		factoryGen.addCreateMethod(type.getName(), fieldList);
		//typeGeneratorHelper.createPojoType(context, logger, typePackageName, typeClassName);
	}

	private String genAccessMethods(JMethod method, SourceWriter sourceWriter) {

		String methodDeclaration = method.getReadableDeclaration();
		if(methodDeclaration.contains("abstract ")) {
			methodDeclaration = methodDeclaration.replaceFirst("abstract ", "");
		}
		
		// construct operation instance
		String methodName = method.getName();//name of the operation
		String fieldName = methodName.substring(3);
		String firstLetter = String.valueOf(fieldName.charAt(0));
		fieldName = fieldName.replaceFirst(firstLetter, firstLetter.toLowerCase());
		String fieldInUpperCase = fieldName.toUpperCase();
		String fieldType = null;
		
		if(methodName.startsWith("get")) {
			sourceWriter.println();
			sourceWriter.println("private final String " + fieldInUpperCase + " = \"" + fieldName + "\";");

			sourceWriter.println();
			sourceWriter.println("@Override");
			sourceWriter.println(methodDeclaration);
			sourceWriter.println("{");
			sourceWriter.indent();
			sourceWriter.println("return this.getPropertyValue(" + fieldInUpperCase + ");");
			sourceWriter.outdent();
			sourceWriter.println("}");
			
			fieldType = method.getReturnType().getSimpleSourceName();
		} else if(methodName.startsWith("set")) {
			JParameter[] parameters = method.getParameters();
			JParameter methodParam = parameters[0];
			
			sourceWriter.println();
			sourceWriter.println("@Override");
			sourceWriter.println(methodDeclaration);
			sourceWriter.println("{");
			sourceWriter.indent();
			sourceWriter.println("this.setPropertyValue(" + fieldInUpperCase + ", " + methodParam.getName() + ");");

			sourceWriter.outdent();
			sourceWriter.println("}");
			
			fieldType = methodParam.getType().getSimpleSourceName();
		}
		return fieldType + " " + fieldName;
	}
}
