<template>
  <div id="app">
    <b-navbar toggleable="md" type="dark" variant="info">

      <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>

      <b-navbar-brand :to="{ name: 'Hello' }">App</b-navbar-brand>

      <b-collapse is-nav id="nav_collapse">

        <b-navbar-nav>
            <b-nav-item v-if="isAdmin" :to="{ name: 'Secured' }">Secured</b-nav-item>
            <b-nav-item v-if="loggedIn" :to="{ name: 'Books' }">Books</b-nav-item>
        </b-navbar-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">

          <b-nav-form v-if="loggedIn">
            <b-form-input size="sm" class="mr-sm-2" type="text" placeholder="Search"/>
            <b-button size="sm" class="my-2 my-sm-0" type="submit">Search</b-button>
          </b-nav-form>

          <b-nav-item-dropdown right v-if="loggedIn">
            <!-- Using button-content slot -->
            <template slot="button-content">
              <em>{{username}}</em>
            </template>
            <b-dropdown-item href="#">Profile</b-dropdown-item>
            <b-dropdown-item :to="{ name: 'Logout' }">Logout</b-dropdown-item>
          </b-nav-item-dropdown>

          <b-nav-item v-if="!loggedIn" :to="{ name: 'Login' }">Login</b-nav-item>

        </b-navbar-nav>

      </b-collapse>
    </b-navbar>

    <router-view></router-view>
  </div>
</template>

<script>
export default {
  computed: {
    username () {
      return this.$store.getters.username
    },
    loggedIn () {
      return this.$store.getters.loggedIn
    },
    isAdmin () {
      return this.$store.getters.isAdmin
    }
  }
}
</script>
