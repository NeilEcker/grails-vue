package vue

import org.apache.chemistry.opencmis.client.api.CmisObject
import org.apache.chemistry.opencmis.client.api.Document
import org.apache.chemistry.opencmis.client.api.Folder
import org.apache.chemistry.opencmis.client.api.ItemIterable
import org.apache.chemistry.opencmis.client.api.OperationContext
import org.apache.chemistry.opencmis.client.api.QueryResult
import org.apache.chemistry.opencmis.client.api.Rendition
import org.apache.chemistry.opencmis.client.api.Session
import org.apache.chemistry.opencmis.client.api.SessionFactory
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl
import org.apache.chemistry.opencmis.commons.PropertyIds
import org.apache.chemistry.opencmis.commons.SessionParameter
import org.apache.chemistry.opencmis.commons.data.ContentStream
import org.apache.chemistry.opencmis.commons.data.PropertyData
import org.apache.chemistry.opencmis.commons.enums.*
import org.apache.chemistry.opencmis.commons.exceptions.*

class DocumentService {


    def grailsApplication

    Session cachedSession

    /**
     * Checks for cached session and creates a new one if it doesn't exist
     * @return  CMIS Session
     */
    protected Session getCmisSession () {

        if (cachedSession == null) {
            synchronized (this) {
                if (cachedSession == null) {

                    log.info("No CMIS session cached, creating...")

                    SessionFactory factory = SessionFactoryImpl.newInstance()
                    Map<String, String> parameters = new HashMap<String, String>()

                    String username = grailsApplication.config.alfresco.user
                    String password = grailsApplication.config.alfresco.password
                    String url = grailsApplication.config.alfresco.url
                    String scope = grailsApplication.config.alfresco.scope

                    parameters.put(SessionParameter.ATOMPUB_URL, url)
                    parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value())
                    parameters.put(SessionParameter.AUTH_HTTP_BASIC, "true")
                    parameters.put(SessionParameter.USER, username)
                    parameters.put(SessionParameter.PASSWORD, password)
                    parameters.put(SessionParameter.REPOSITORY_ID, scope)

                    cachedSession = factory.createSession(parameters)
                }
            }
        }

        return cachedSession
    }

    /**
     * Get Document object for the provided object ID
     * @param objectId Object ID
     * @param oc OperationContext
     * @return Document object
     */
    Document getDocumentByIdWithContext(String objectId, OperationContext oc) {
        oc.setCacheEnabled(false)
        try {
            Document doc = (Document) getCmisSession().getObject(objectId)
            return doc
        } catch (CmisObjectNotFoundException confe) {
            return null
        }
    }

    /**
     * Function to query documents by policy and procedure number
     * @param search
     * @return
     */
    List policySearch(searchQuery) {
        OperationContext context = getCmisSession().createOperationContext()
        context.setCacheEnabled(false)

        def documents = []

        def qs

        qs = 'SELECT d.cmis:objectId FROM cmis:document d' +
                ' join gry:policy m on d.cmis:objectId = m.cmis:objectId ' +
                ' join gry:procedure p on d.cmis:objectId = p.cmis:objectId ' +
                'WHERE d.cmis:name LIKE \'%' + searchQuery + '%\' OR m.gry:policyNumber LIKE \'%' + searchQuery + '%\''

        /*if(search.policy && search.procedure) {
            qs = 'SELECT d.cmis:objectId FROM cmis:document d' +
                    ' join gry:policy m on d.cmis:objectId = m.cmis:objectId ' +
                    ' join gry:procedure p on d.cmis:objectId = p.cmis:objectId ' +
                    'WHERE m.gry:policyNumber IS NOT NULL AND m.gry:policyNumber LIKE \'%' + search.policy + '%\'' +
                    ' AND p.gry:procedureNumber IS NOT NULL AND p.gry:procedureNumber LIKE \'%' + search.procedure + '%\''
        } else if(search.policy){
            qs = 'SELECT d.cmis:objectId FROM cmis:document d join gry:policy m on d.cmis:objectId = m.cmis:objectId WHERE m.gry:policyNumber IS NOT NULL AND m.gry:policyNumber LIKE \'%' + search.policy + '%\''
        } else{
            qs = 'SELECT d.cmis:objectId FROM cmis:document d join gry:procedure m on d.cmis:objectId = m.cmis:objectId WHERE m.gry:procedureNumber IS NOT NULL AND m.gry:procedureNumber LIKE \'%' + search.procedure + '%\''
        }*/

        println qs

        ItemIterable<CmisObject> results = getCmisSession().query(qs, false, context)

        //Return list of CMIS documents
        results.collect {
            final PropertyData<Object> propertyById = it.getPropertyById("cmis:objectId")
            final String objectId = (String) propertyById.getValues().get(0)
            log.info("Get document with objectId ${objectId}")
            getDocumentByIdWithContext(objectId, context)
        }
    }


}
