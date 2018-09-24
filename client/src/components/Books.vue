<template>
  <div>
    <h1>Books</h1>
    <b-table striped hover :items="books"></b-table>
    
    <b-btn v-b-modal.addBook>Add Book</b-btn>

    <!-- Modal Component -->
    <b-modal id="addBook" ref="addBook" hide-footer title="Bootstrap-Vue">
      <b-form @submit="addBook" @reset="onReset">
      <b-form-group id="titleGroup"
                    label="Title:"
                    label-for="title">
        <b-form-input id="title"
                      type="text"
                      v-model="form.title"
                      required
                      placeholder="Enter title">
        </b-form-input>
      </b-form-group>
      <b-form-group id="authorGroup"
                    label="Author:"
                    label-for="author">
        <b-form-input id="author"
                      type="text"
                      v-model="form.author"
                      required
                      placeholder="Enter author">
        </b-form-input>
      </b-form-group>
      <b-form-group id="pagesGroup"
                    label="Pages:"
                    label-for="pages">
        <b-form-input id="pages"
                      type="number"
                      v-model="form.pages"
                      required
                      placeholder="Enter # of pages">
        </b-form-input>
      </b-form-group>
      <b-button type="submit" variant="primary">Submit</b-button>
      <b-button type="reset" variant="danger">Reset</b-button>
    </b-form>
    </b-modal>
  </div>
</template>

<script>
export default {
  name: 'Books',
  data () {
    return {
      form: {
        title: '',
        author: '',
        pages: ''
      }
    }
  },
  computed: {
    books () {
      return this.$store.getters.books
    }
  },
  created () {
    this.$store.dispatch('retrieveBooks')
  },
  methods: {
    addBook () {
      this.$store.dispatch('addBook', {
        title: this.form.title,
        author: this.form.author,
        pages: this.form.pages
      })
      this.$refs.addBook.hide()
      this.onReset()
    },
    onReset () {
      this.form.title = this.form.author = this.form.pages = ''
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
