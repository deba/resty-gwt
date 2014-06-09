package org.appops.shared.rebind.pojogenerator;

import java.io.PrintWriter;

import org.appops.shared.client.core.Entity;
import org.appops.shared.client.core.Type;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class PojoTypeGeneratorHelper {

	public void createPojoType(GeneratorContext context, TreeLogger logger, String packageName, String className) {

		PrintWriter printWriter = context.tryCreate(logger, packageName, className);
		if (printWriter == null)
			return;
		ClassSourceFileComposerFactory composer = null;
		composer = new ClassSourceFileComposerFactory(packageName, className);

		// implements from type
		composer.addImplementedInterface(Type.class.getSimpleName());
		//add imports
		composer.addImport(Entity.class.getCanonicalName());
		composer.addImport(Type.class.getCanonicalName());

		SourceWriter sourceWriter = composer.createSourceWriter(context, printWriter);

		sourceWriter.println();
		sourceWriter.println("private String typeName = null;");
		sourceWriter.println("private Long typeId = null;");
		sourceWriter.println("private Long serviceId = null;");
		sourceWriter.println();

		//add constructor
		sourceWriter.println("public " + className + "(){ ");
		sourceWriter.indent();
		sourceWriter.println("setTypeName(" + className + ".class.getCanonicalName());");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();

		sourceWriter.println("public String getName() {");
		sourceWriter.indent();
		sourceWriter.println("return typeName;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();

		sourceWriter.println("public void setName(String str) {");
		sourceWriter.indent();
		sourceWriter.println("typeName = str;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();

		sourceWriter.println("public boolean equals(Type t) {");
		sourceWriter.indent();

		sourceWriter.println("return typeName.equals(t.getName());");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();

		/*sourceWriter.println("@Override");
		sourceWriter.println("public String getDisplayName() {");
		sourceWriter.indent();
		sourceWriter.println("return typeName;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();*/

		/*sourceWriter.println("@Override");
		sourceWriter.println("public Entity newInstance() {");
		sourceWriter.indent();
		sourceWriter.println("return null;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();*/

		/*sourceWriter.println("@Override");
		sourceWriter.println("public Long getTypeId() {");
		sourceWriter.indent();
		sourceWriter.println("return typeId;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();

		sourceWriter.println("@Override");
		sourceWriter.println("public void setTypeId(Long id) {");
		sourceWriter.indent();
		sourceWriter.println("this.typeId = id;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();*/

		sourceWriter.println("@Override");
		sourceWriter.println("public Long getServiceId() {");
		sourceWriter.indent();
		sourceWriter.println("return serviceId;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();

		sourceWriter.println("@Override");
		sourceWriter.println("public void setServiceId(Long serviceId) {");
		sourceWriter.indent();
		sourceWriter.println("this.serviceId = serviceId;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println();

		sourceWriter.outdent();
		sourceWriter.println("}");
		context.commit(logger, printWriter);

	}
}
