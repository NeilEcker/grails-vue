package vue

class BootStrap {

    def init = { servletContext ->

        def adminRole = new Role(authority: 'ROLE_ADMIN').save()
        def userRole = new Role(authority: 'ROLE_USER').save()

        def testUser = new UserAccount(username: 'user', password: 'user').save()
        def adminUser = new UserAccount(username: 'admin', password: 'admin').save()

        UserAccountRole.create testUser, userRole
        UserAccountRole.create adminUser, adminRole

        UserAccountRole.withSession {
            it.flush()
            it.clear()
        }

        assert UserAccount.count() == 2
        assert Role.count() == 2
        assert UserAccountRole.count() == 2

        new Book(title: "The Stand", author: "Stephen King", pages: 500).save(failOnError: true)

    }
    def destroy = {
    }
}
