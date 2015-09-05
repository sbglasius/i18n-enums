package grails.plugins.i18nenums.traits

import grails.plugins.i18nenums.DefaultNameCase
import grails.util.Holders
import groovy.transform.SelfType
import org.springframework.context.MessageSourceResolvable

@SelfType(Enum)
trait I18nEnumTrait implements MessageSourceResolvable {
    /**
     * The name() method on enums
     * @return
     */
    abstract public String name()

    @Override
    String[] getCodes() {
        String className = i18nEnumConfig['shortName'] ? this.class.simpleName : this.class.name

        [name.toUpperCase(), name, name.toLowerCase()].collect {
            "${i18nEnumPrefix}${className}.${it}${i18nEnumPostfix}".toString()
        } as String[]
    }

    @Override
    Object[] getArguments() {
        [] as Object[]
    }

    @Override
    String getDefaultMessage() {
        switch (i18nEnumConfig['defaultNameCase'] as DefaultNameCase) {
            case DefaultNameCase.UPPER_CASE:
                return name.toUpperCase()
            case DefaultNameCase.LOWER_CASE:
                return name.toLowerCase()
            case DefaultNameCase.CAPITALIZE:
                return name.toLowerCase().capitalize()
            case DefaultNameCase.ALL_CAPS:
                return name.split('_').collect { String it -> it.toLowerCase().capitalize() }.join(' ')
            default:
                name
        }
    }

    /**
     * Convenient method to get name() return value.
     * @return
     */
    String getName() {
        this.name()
    }

    /**
     * Returns the i18nEnumConfig.
     * If the I18nEnumTrait is used from an annotation, the annotation config overrules
     * the Grails config settings.
     * @return a config map (Empty if no config)
     */
    private static Map<String, Object> getI18nEnumConfig() {
        Map config = Holders.config?.grails?.plugin?.i18nEnum as Map ?: [:]
        config = config + i18nEnumASTConfig
        config
    }

    /**
     * This method returns an empty map. The method body can be replaced
     * by the I18nEnumTransformation if the @I18nEnum provides arguments
     * @return and empty config unless replaced by the AST transformation.
     */
    private static Map getI18nEnumASTConfig() {
        [:]
    }

    /**
     * Normalizes the prefix
     * @return
     */
    private static String getI18nEnumPrefix() {
        String prefix = i18nEnumConfig['prefix']
        return prefix ? prefix + (prefix.endsWith('.') ? '' : '.') : ''
    }

    /**
     * Normalizes the postfix
     * @return
     */
    private static String getI18nEnumPostfix() {
        String postfix = i18nEnumConfig['postfix']
        return postfix ? (postfix.startsWith('.') ? '' : '.') + postfix : ''
    }
}
