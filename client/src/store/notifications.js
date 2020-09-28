import io from "socket.io-client";
const API_URL = "http://localhost:4121";

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
        establishClientSocket({commit, rootGetters}) {
            if (rootGetters.getToken === null) return; 
            const socket = io(API_URL, {
                transports: ["websocket"],
                query: { token: rootGetters.getToken}
            });
            socket.on('notification', (notification) => {
                commit('addNotification', notification);
            })
            socket.on('notifications', (notifications) => {
                commit('setNotifications', notifications);
            })
            socket.emit('all');
            commit('setSocket', socket);
        },
        deleteNotification({commit, rootGetters}, notificationId) {
            const socket = rootGetters.getSocket;
            socket.emit('delete', notificationId);
            socket.on('deleted', (id) => {
                commit('removeNotification', id);
            });
        },
        deleteAllUserNotifications({commit, rootGetters}) {
            const socket = rootGetters.getSocket;
            socket.emit('deleteAll');
            socket.on('deletedAll', () => {
                commit('removeAllNotifications');
            });
        },
        markRead({commit, rootGetters}, notificationId) {
            const socket = rootGetters.getSocket;
            socket.emit('mark', notificationId);
            socket.on('marked', (notification) => {
                commit('replaceNotification', notification);
            });
        },
        closeSocket({commit, rootGetters}) {
            rootGetters.getSocket.close();
            commit('removeSocket');
        }
    }
}