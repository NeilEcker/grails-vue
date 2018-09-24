import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)
axios.defaults.baseURL = 'http://localhost:8080/api'

export const store = new Vuex.Store({
  state: {
    token: localStorage.getItem('access_token') || null,
    username: localStorage.getItem('username') || null,
    roles: localStorage.getItem('roles') || null,
    filter: 'all',
    todos: [],
    books: []
  },
  getters: {
    loggedIn (state) {
      return state.token !== null
    },
    username (state) {
      return state.username
    },
    roles (state) {
      return state.roles
    },
    isAdmin (state) {
      if (state.roles) {
        return state.roles.includes('ROLE_ADMIN')
      } else {
        return false
      }
    },
    remaining (state) {
      return state.todos.filter(todo => !todo.completed).length
    },
    anyRemaining (state, getters) {
      return getters.remaining !== 0
    },
    books (state) {
      return state.books
    },
    todosFiltered (state) {
      if (state.filter === 'all') {
        return state.todos
      } else if (state.filter === 'active') {
        return state.todos.filter(todo => !todo.completed)
      } else if (state.filter === 'completed') {
        return state.todos.filter(todo => todo.completed)
      }
      return state.todos
    },
    showClearCompletedButton (state) {
      return state.todos.filter(todo => todo.completed).length > 0
    }
  },
  mutations: {
    addTodo (state, todo) {
      state.todos.push({
        id: todo.id,
        title: todo.title,
        completed: false,
        editing: false
      })
    },
    updateTodo (state, todo) {
      const index = state.todos.findIndex(item => item.id === todo.id)
      state.todos.splice(index, 1, {
        'id': todo.id,
        'title': todo.title,
        'completed': todo.completed,
        'editing': todo.editing
      })
    },
    deleteTodo (state, id) {
      const index = state.todos.findIndex(item => item.id === id)
      state.todos.splice(index, 1)
    },
    checkAll (state, checked) {
      state.todos.forEach(todo => (todo.completed = checked))
    },
    updateFilter (state, filter) {
      state.filter = filter
    },
    clearCompleted (state) {
      state.todos = state.todos.filter(todo => !todo.completed)
    },
    retrieveTodos (state, todos) {
      state.todos = todos
    },
    retrieveBooks (state, books) {
      state.books = books
    },
    retrieveToken (state, token) {
      state.token = token
    },
    setUsername (state, username) {
      state.username = username
    },
    setRoles (state, roles) {
      state.roles = roles
    },
    destroyToken (state) {
      console.log('destroyToken')
      state.token = null
      state.username = null
      state.roles = null
    },
    clearTodos (state) {
      state.todos = []
    },
    addBook (state, book) {
      state.books.push({
        id: book.id,
        title: book.title,
        author: book.author,
        pages: book.pages
      })
    }
  },
  actions: {
    retrieveName (context) {
      axios.defaults.headers.common['Authorization'] = 'Bearer ' + context.state.token

      return new Promise((resolve, reject) => {
        axios.get('/user')
          .then(response => {
            resolve(response)
          })
          .catch(error => {
            reject(error)
          })
      })
    },
    clearTodos (context) {
      context.commit('clearTodos')
    },
    register (context, data) {
      return new Promise((resolve, reject) => {
        axios.post('/register', {
          name: data.name,
          email: data.email,
          password: data.password
        })
          .then(response => {
            resolve(response)
          })
          .catch(error => {
            reject(error)
          })
      })
    },
    destroyToken (context) {
      if (context.getters.loggedIn) {
        localStorage.removeItem('access_token')
        context.commit('destroyToken')
      }
    },
    retrieveToken (context, credentials) {
      return new Promise((resolve, reject) => {
        axios.post('/login', {
          username: credentials.username,
          password: credentials.password
        })
          .then(response => {
            const token = response.data.access_token
            const username = response.data.username
            const roles = response.data.roles
            console.log(response.data)

            localStorage.setItem('access_token', token)
            context.commit('retrieveToken', token)
            localStorage.setItem('username', username)
            context.commit('setUsername', username)
            localStorage.setItem('roles', roles)
            context.commit('setRoles', roles)
            resolve(response)
          })
          .catch(error => {
            console.log(error)
            reject(error)
          })
      })
    },
    retrieveTodos (context) {
      axios.defaults.headers.common['Authorization'] = 'Bearer ' + context.state.token

      axios.get('/todos')
        .then(response => {
          context.commit('retrieveTodos', response.data)
        })
        .catch(error => {
          console.log(error)
        })
    },
    addTodo (context, todo) {
      axios.post('/todos', {
        title: todo.title,
        completed: false
      })
        .then(response => {
          context.commit('addTodo', response.data)
        })
        .catch(error => {
          console.log(error)
        })
    },
    updateTodo (context, todo) {
      axios.patch('/todos/' + todo.id, {
        title: todo.title,
        completed: todo.completed
      })
        .then(response => {
          context.commit('updateTodo', response.data)
        })
        .catch(error => {
          console.log(error)
        })
    },
    deleteTodo (context, id) {
      axios.delete('/todos/' + id)
        .then(response => {
          context.commit('deleteTodo', id)
        })
        .catch(error => {
          console.log(error)
        })
    },
    checkAll (context, checked) {
      axios.patch('/todosCheckAll', {
        completed: checked
      })
        .then(response => {
          context.commit('checkAll', checked)
        })
        .catch(error => {
          console.log(error)
        })
    },
    updateFilter (context, filter) {
      context.commit('updateFilter', filter)
    },
    clearCompleted (context) {
      const completed = context.state.todos
        .filter(todo => todo.completed)
        .map(todo => todo.id)

      axios.delete('/todosDeleteCompleted', {
        data: {
          todos: completed
        }
      })
        .then(response => {
          context.commit('clearCompleted')
        })
        .catch(error => {
          console.log(error)
        })
    },
    retrieveBooks (context) {
      axios.defaults.headers.common['Authorization'] = 'Bearer ' + context.state.token

      axios.get('/book')
          .then(response => {
            console.log(response.data)
            context.commit('retrieveBooks', response.data)
          })
          .catch(error => {
            console.log(error)
          })
    },
    addBook (context, book) {
      axios.post('/book', {
        title: book.title,
        author: book.author,
        pages: book.pages
      })
        .then(response => {
          context.commit('addBook', response.data)
        })
        .catch(error => {
          console.log(error)
        })
    }
  }
})
