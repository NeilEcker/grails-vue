

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'vue.UserAccount'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'vue.UserAccountRole'
grails.plugin.springsecurity.authority.className = 'vue.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/application',		access: ['permitAll']],
	[pattern: '/api/login',       access: ['permitAll']],
	[pattern: '/api/logout',      access: ['IS_AUTHENTICATED_FULLY']]
]

grails.plugin.springsecurity.rest.token.storage.jwt.secret = 'qrD6h8K6S9503Q06Y6Rfk21TErImPYqa'

grails.plugin.springsecurity.filterChain.chainMap = [
	//Stateless chain
	[
		pattern: '/**',
		filters: 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'
	],
	//Traditional, stateful chain
	[
		pattern: '/stateful/**',
		filters: 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter'
	]
]