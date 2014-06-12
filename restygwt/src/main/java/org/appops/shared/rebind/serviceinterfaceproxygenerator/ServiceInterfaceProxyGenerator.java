package org.appops.shared.rebind.serviceinterfaceproxygenerator;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.appops.shared.client.core.Operation;
import org.appops.shared.client.core.annotate.Interface;
import org.appops.shared.rebind.util.AnnotationScanner;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/***
 * 
 * @author komal@ensarm.com This generator helps the sync async operations to be handled though the interface given by
 *         appops.
 * 
 */
public class ServiceInterfaceProxyGenerator extends Generator {

	private GeneratorContext context;
	private TypeOracle typeOracle;
	private TreeLogger logger;
	String packageName = "org.appops.shared.client";
	String className = "ServiceInterfaceProxy";

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName)
			throws UnableToCompleteException {
		this.logger = logger;
		this.context = context;
		typeOracle = context.getTypeOracle();

		generateProxy();

		return packageName + "." + className;
	}

	private void generateProxy() {

		String packageName = "in.appops.trial.domain";
		String className = "ServiceInterfaceProxy";

		PrintWriter printWriter = context.tryCreate(logger, packageName, className);
		if (printWriter == null)
			return;
		ClassSourceFileComposerFactory composer = null;
		composer = new ClassSourceFileComposerFactory(packageName, className);

		composer.addImport(HashMap.class.getCanonicalName());
		composer.addImport(Operation.class.getCanonicalName());
		composer.addImport(ObjectMapper.class.getCanonicalName());
		composer.addImport(GWT.class.getCanonicalName());
		composer.addImport(RequestBuilder.class.getCanonicalName());
		composer.addImport(Logger.class.getCanonicalName());
		composer.addImport(Level.class.getCanonicalName());
		composer.addImport(RequestCallback.class.getCanonicalName());
		composer.addImport(Request.class.getCanonicalName());
		composer.addImport(Response.class.getCanonicalName());
		composer.addImport(Throwable.class.getCanonicalName());
		
		SourceWriter sourceWriter = composer.createSourceWriter(context, printWriter);
		sourceWriter.println();
		sourceWriter.println("private Logger logger = Logger.getLogger(\"ServiceInterfaceProxy\");");
		
		addOperations(sourceWriter);

		sourceWriter.outdent();
		sourceWriter.println("}");
		context.commit(logger, printWriter);
	}

	private void addOperations(SourceWriter sourceWriter) {
		Class[] clazzarray = { Interface.class };
		Map<Class<? extends Annotation>, List<JClassType>> map = AnnotationScanner.scan(logger, typeOracle, clazzarray);
		List<JClassType> list = map.get(Interface.class);
		for (JClassType jClassType : list) {
			try {
				System.out.println("async class found " + jClassType.getName() + "Async");

				JClassType asyncClass = typeOracle.findType(jClassType.getName() + "Async");
				JMethod[] jMethods = jClassType.getMethods();

				for (int j = 0; j < jMethods.length; j++) {
					final JMethod jMethod = jMethods[j];

					String methodDeclaration = jMethod.getReadableDeclaration();
					
					if(methodDeclaration.contains("abstract ")) {
						methodDeclaration = methodDeclaration.replaceFirst("abstract ", "");
					}
					
					sourceWriter.println();
					sourceWriter.println(methodDeclaration + "{");
					sourceWriter.indent();
					sourceWriter.println("try {");
					sourceWriter.indent();
					sourceWriter.println("HashMap parameterMap = new HashMap();");

					// construct operation instance
					String methodName = jMethod.getName();//name of the operation

					JParameter[] parameters = jMethod.getParameters();// operation parameters
					for (int i = 0; i < parameters.length; i++) {
						JParameter parameter = parameters[i];
						if (!(parameter.getType().getSimpleSourceName().equals("AsyncCallback"))) {
							sourceWriter.println("parameterMap.put(\"" + parameter.getName() + "\", "
									+ parameter.getName() + ");");
						} else {
							sourceWriter.println("final " + parameter.getType().getQualifiedSourceName() + " methodCallback = " + parameter.getName() + ";");
						}
					}
					sourceWriter.println();
					sourceWriter.println(Operation.class.getSimpleName() + " operation = new " + Operation.class.getSimpleName() + "();");
					sourceWriter.println("operation.setName(\"" + jClassType.getName() + "." + methodName + "\");");
					sourceWriter.println("operation.setParameters(parameterMap);");
					
					sourceWriter.println("ObjectMapper objWriter = new ObjectMapper();");
					sourceWriter.println("String operationJson = objWriter.writeValueAsString(operation);");
					
					/*sourceWriter.println("ObjectWriter objWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();");
					sourceWriter.println("String operationJson = objWriter.writeValueAsString(operation);");*/
					
					sourceWriter.println("RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, GWT.getHostPageBaseURL() + \"restservlet\");");
					sourceWriter.println("rb.setHeader(\"Content-type\", \"application/x-www-form-urlencoded\");");
					sourceWriter.println("rb.sendRequest(operationJson, new RequestCallback() {");
					sourceWriter.println();
					sourceWriter.indent();
					sourceWriter.println("@Override");
					sourceWriter.println("public void onResponseReceived(Request request, Response response) {");
					sourceWriter.indent();
					if (isVoidMethod(jMethod)) {
						sourceWriter.println("methodCallback.onSuccess(null);");
			        } else {
			        	String typeName = getQualifiedReturnTypeName(jMethod);
			        	sourceWriter.println("String responseResult = response.getText();");
						sourceWriter.println("ObjectMapper objMapper = new ObjectMapper();");
						sourceWriter.println(typeName + " result = null;");
						sourceWriter.println("try {");
						sourceWriter.indent();
						sourceWriter.println("result = objMapper.readValue(responseResult, " + typeName + ".class);");
						sourceWriter.outdent();
						sourceWriter.println("} catch (Exception e) {");
						sourceWriter.indent();
						sourceWriter.println("logger.log(Level.SEVERE, \"[ServiceInterfaceProxy] : [" + methodName + "] : Exception\", e);");
						sourceWriter.outdent();
						sourceWriter.println("}");
						sourceWriter.println("methodCallback.onSuccess(result);");
			        }
					sourceWriter.outdent();
					sourceWriter.println("}");
					sourceWriter.println();
					sourceWriter.println("@Override");
					sourceWriter.println("public void onError(Request request, Throwable exception) {");
					sourceWriter.indent();
					sourceWriter.println("methodCallback.onFailure(exception);");
					sourceWriter.outdent();
					sourceWriter.println("}");
					sourceWriter.outdent();
					sourceWriter.println("});");
					
					sourceWriter.outdent();
					sourceWriter.println("} catch (Exception e) {");
					sourceWriter.indent();
					sourceWriter.println("logger.log(Level.SEVERE, \"[ServiceInterfaceProxy] : [" + methodName + "] : Exception\", e);");
					sourceWriter.outdent();
					sourceWriter.println("}");
					
					if (getMethodReturnValue(jMethod) != null) {
						String returnVal = getMethodReturnValue(jMethod);
						sourceWriter.println("return " + returnVal + ";");
			        }
					
					sourceWriter.outdent();
					sourceWriter.println("}");
					sourceWriter.println();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getMethodReturnValue(JMethod jMethod) {
		if(!("void".equals(jMethod.getReturnType().getQualifiedBinaryName()))) {
			String typeName = jMethod.getReturnType().getSimpleSourceName();
			if(typeName.equals("int") || typeName.equals("long") || typeName.equals("Integer") || typeName.equals("Long")
					|| typeName.equals("float") || typeName.equals("Float") || typeName.equals("double") || typeName.equals("Double")
					|| typeName.equals("byte") || typeName.equals("Byte") || typeName.equals("short") || typeName.equals("Short")
					|| typeName.equals("char") || typeName.equals("Character")) {
				return "0";
			} else if(typeName.equals("boolean") || typeName.equals("Boolean")) {
				return "false";
			} else {
				return "null";
			}
		}
		return null;
	}

	private String getQualifiedReturnTypeName(JMethod jMethod) {
		String returnType = jMethod.getReturnType().getSimpleSourceName();
		String qualifiedReturnTypeName = null;
		if(returnType.equals("int")) {
			qualifiedReturnTypeName = Integer.class.getCanonicalName();
		} else if(returnType.equals("boolean")) {
			qualifiedReturnTypeName = Boolean.class.getCanonicalName();
		} else if(returnType.equals("long")) {
			qualifiedReturnTypeName = Long.class.getCanonicalName();
		} else if(returnType.equals("byte")) {
			qualifiedReturnTypeName = Byte.class.getCanonicalName();
		} else if(returnType.equals("double")) {
			qualifiedReturnTypeName = Double.class.getCanonicalName();
		} else if(returnType.equals("short")) {
			qualifiedReturnTypeName = Short.class.getCanonicalName();
		} else if(returnType.equals("float")) {
			qualifiedReturnTypeName = Float.class.getCanonicalName();
		} else if(returnType.equals("char")) {
			qualifiedReturnTypeName = Character.class.getCanonicalName();
		} else {
			qualifiedReturnTypeName = jMethod.getReturnType().getQualifiedSourceName();
		}
		return qualifiedReturnTypeName;
	}

	public boolean isVoidMethod(JMethod method) {
        return "void".equals(method.getReturnType().getQualifiedBinaryName());
    }

}
