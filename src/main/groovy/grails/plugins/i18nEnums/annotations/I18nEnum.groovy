package grails.plugins.i18nenums.annotations

import org.codehaus.groovy.transform.GroovyASTTransformationClass
import grails.plugins.i18nenums.DefaultNameCase

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@GroovyASTTransformationClass(["grails.plugins.i18nenums.transformation.I18nEnumTransformation"])
public @interface I18nEnum {
    String prefix() default ''

    String postfix() default ''

    boolean shortName() default false

    DefaultNameCase defaultNameCase() default DefaultNameCase.UNCHANGED
}
