package vue

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.*
import grails.converters.*

@Secured('ROLE_ADMIN')
class TestController {
	static responseFormats = ['json', 'xml']

    DocumentService documentService

    def index() {
        render "index"
    }

    def search(String query) {

        def documents = documentService.policySearch(query ?: 'G-GEN-00')

        def documentMap = []

        documents.each {

            List<Object> aspects = it.getProperty("cmis:secondaryObjectTypeIds").getValues()

            documentMap << [name: it.name,
                            policyNumber: it.getPropertyValue("gry:policyNumber"),
                            procedureNumber: it.getPropertyValue("gry:procedureNumber"),
                            documentNumber: it.getPropertyValue("gry:documentNumber"),
                            isRecord: aspects.contains("P:rma:record"),
                            recordCategory: it.getPropertyValue("gry:recordCategory"),
                            nodeRef: it.getPropertyValue("alfcmis:nodeRef")]
            //println "${it.name} ${it.getPropertyValue("gry:policyNumber")}"
            //println it.properties
        }

        render documentMap as JSON
    }
}
