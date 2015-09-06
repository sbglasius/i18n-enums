package grails.plugins.i18nEnums

import grails.plugins.*

class Grails3I18nEnumsGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.0.0.M2 > *"
    // resources that are excluded from plugin packaging
    def title = "I18nEnums Grails Plugin"
    def author = "Søren Berg Glasius"
    def authorEmail = "soeren@glasius.dk"
    def description = 'Adds an enumeration usable on Enums to easy add and implement the MessageSourceResolvable interface'
    def documentation = "http://sbglasius.github.io/I18nEnumsGrailsPlugin/"

    def license = "APACHE"
    def organization = [name: "Groovy Freelancer", url: "http://www.groovy-freelancer.dk/"]
    def issueManagement = [system: 'GITHUB', url: 'https://github.com/sbglasius/I18nEnumsGrails3Plugin/issues']
    def scm = [url: 'https://github.com/sbglasius/I18nEnumsGrails3Plugin']
}
