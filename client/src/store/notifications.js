import io from "socket.io-client";
import { SOCKET_URL } from "@/constants/"

export default {
    state: {
        socket: null,
        notifications: [],
    },
    getters: {
        getSocket(state) {
            return state.socket;
        },
        getNotifications(state) {
            return state.notifications;
        }
    },
    mutations: {
        setSocket(state, socket) {
            state.socket = socket;
        },
        addNotification(state, notification) {
            state.notifications = [notification, ...state.notifications];
        },
        setNotifications(state, notifications) {
            state.notifications = notifications;
        },
        removeNotification(state, notificationId) {
            state.notifications = state.notifications.filter(n => n.id !== notificationId);
        },
        removeAllNotifications(state) {
            state.notifications = [];
        },
        replaceNotification(state, notification) {
            state.notifications = state.notifications.map(n => n.id === notification.id ? notification : n);
        },
        removeSocket(state) {
            state.socket = null;
        }
    },
    actions: {
        /**
         * Establish a new client socket connection
         * @param {Object} context Store context 
         */
        establishClientSocket({commit, rootGetters}) {
            if (rootGetters.getToken === null) return; 
            const socket = io(SOCKET_URL, {
                transports: ["websocket"],
                query: { token: rootGetters.getToken},
                secure: false,
            });
            socket.on('notification', (notification) => {
                commit("setResponse", notification.message);
                commit("setIsAlert", true);
                commit('addNotification', notification);
            })
            socket.on('notifications', (notifications) => {
                commit('setNotifications', notifications);
            })
            socket.emit('all');
            commit('setSocket', socket);
        },
        /**
         * Deletes a notification by id owned by current user
         * @param {Object} context Store context 
         * @param {Number} notificationId notification id
         */
        deleteNotification({commit, rootGetters}, notificationId) {
            const socket = rootGetters.getSocket;
            socket.emit('delete', notificationId);
            socket.on('deleted', (id) => {
                commit('removeNotification', id);
            });
        },
        /**
         * Dispatches delete all user vacations for the current user
         * @param {Object} context Store context 
         */
        deleteAllUserNotifications({commit, rootGetters}) {
            const socket = rootGetters.getSocket;
            socket.emit('deleteAll');
            socket.on('deletedAll', () => {
                commit('removeAllNotifications');
            });
        },
        /**
         * Closes the connection with socket
         * @param {Object} context Store context 
         */
        closeSocket({commit, rootGetters}) {
            rootGetters.getSocket.close();
            commit('removeSocket');
        }
    }
}