package grails.plugin.i18nEnums.compiler

import grails.plugin.i18nEnums.helper.I18nEnumHelper
import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.ModuleNode
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.MapEntryExpression
import org.codehaus.groovy.ast.expr.MapExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.DelegateASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.springframework.context.MessageSourceResolvable

import java.lang.reflect.Modifier

@GroovyASTTransformation(phase= CompilePhase.CANONICALIZATION)
@CompileStatic
class I18nEnumTransformation implements ASTTransformation {
    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        if (!(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
            throw new RuntimeException("Internal error: wrong types: ${nodes[0].class} / ${nodes[1].class}")
        }

        AnnotationNode annotationNode = nodes[0] as AnnotationNode
        def annotatedNode = nodes[1]


        if (annotatedNode instanceof ClassNode) {
            ClassNode classNode = annotatedNode
            addInterface(classNode)
            addHelper(classNode, annotationNode)
        }

        ModuleNode ast = source.getAST();

    }
    private void addInterface(ClassNode classNode) {
        def clazz = ClassHelper.make(MessageSourceResolvable)
        classNode.addInterface(clazz)
    }

    private void addHelper(ClassNode classNode, AnnotationNode annotationNode) {
        MapExpression mapExpression = createMapExpression(annotationNode)

        Expression thisExpression = new VariableExpression("this", classNode)
        ArgumentListExpression helperArguments = new ArgumentListExpression(thisExpression, mapExpression)
        Expression helperValue = new ConstructorCallExpression(ClassHelper.make(I18nEnumHelper), helperArguments)
        FieldNode helperField = new FieldNode('$helper', Modifier.PRIVATE, ClassHelper.make(I18nEnumHelper), classNode, helperValue)
        AnnotationNode delegateAnnotation = new AnnotationNode(ClassHelper.make(Delegate))

        helperField.addAnnotation(delegateAnnotation)
        classNode.addTransform(DelegateASTTransformation, delegateAnnotation)
        classNode.addField(helperField)
    }

    private MapExpression createMapExpression(AnnotationNode annotationNode) {
        Expression prefix = annotationNode.getMember('prefix')
        Expression postfix = annotationNode.getMember('postfix')
        Expression shortName = annotationNode.getMember('shortName')
        Expression defaultNameCase = annotationNode.getMember('defaultNameCase')

        Expression mapExpression = new MapExpression()
        if (prefix) {
            mapExpression.addMapEntryExpression(createMapEntryExpression("prefix", prefix))
        }
        if (postfix) {
            mapExpression.addMapEntryExpression(createMapEntryExpression("postfix", postfix))
        }
        if (shortName) {
            mapExpression.addMapEntryExpression(createMapEntryExpression("shortName", shortName))
        }
        if (defaultNameCase) {
            mapExpression.addMapEntryExpression(createMapEntryExpression("defaultNameCase", defaultNameCase))
        }
        mapExpression
    }

    private MapEntryExpression createMapEntryExpression(String key, Expression value) {
        new MapEntryExpression(new ConstantExpression(key), value)
    }

}
