import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: JSON.parse(sessionStorage.getItem('user')) || null
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
      sessionStorage.setItem('user', JSON.stringify(user))
    },
    CLEAR_USER(state) {
      state.user = null
      sessionStorage.removeItem('user')
    }
  },
  actions: {
    login({ commit }, user) {
      commit('SET_USER', user)
    },
    logout({ commit }) {
      commit('CLEAR_USER')
    }
  }
})
