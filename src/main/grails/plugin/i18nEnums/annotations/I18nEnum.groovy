package grails.plugin.i18nEnums.annotations

import grails.plugin.i18nEnums.DefaultNameCase
import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@GroovyASTTransformationClass(["grails.plugin.i18nEnums.compiler.I18nEnumTransformation"])
public @interface I18nEnum {
    String prefix() default ''

    String postfix() default ''

    boolean shortName() default false

    DefaultNameCase defaultNameCase() default DefaultNameCase.UNCHANGED
}
