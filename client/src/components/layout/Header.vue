<template>
    <div>
        <v-navigation-drawer v-model="drawer" app temporary v-if="loggedIn">
            <v-list dense>
                <v-list-item router-link :to="{ name: 'Dashboard' }">
                    <v-list-item-action>
                        <v-icon>mdi-view-dashboard</v-icon>
                    </v-list-item-action>
                    <v-list-item-content>
                        <v-list-item-title>Dashboard</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item
                    router-link
                    :to="{
                        name: 'RequestHistory',
                        params: { id: user.id.toString() }
                    }"
                >
                    <v-list-item-action>
                        <v-icon>mdi-history</v-icon>
                    </v-list-item-action>
                    <v-list-item-content>
                        <v-list-item-title>Request History</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <v-list-item
                    router-link
                    :to="{ name: 'Admin' }"
                    v-if="user && user.is_admin"
                >
                    <v-list-item-action>
                        <v-icon>mdi-head-minus</v-icon>
                    </v-list-item-action>
                    <v-list-item-content>
                        <v-list-item-title>Admin</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>

        <v-app-bar app color="indigo" dark>
            <v-app-bar-nav-icon
                @click.stop="drawer = !drawer"
                v-if="loggedIn"
            ></v-app-bar-nav-icon>
            <v-toolbar-title>Tidsbanken</v-toolbar-title>
            <v-spacer></v-spacer>
            <header-notifications v-if="loggedIn" />
            <v-btn
                v-if="loggedIn"
                text
                router-link
                :to="{ name: 'UserProfile' }"
            >
                <v-avatar color="light-blue" size="34">
                    <span v-if="!user.profile_pic" class="white--text headline">
                        {{ user.full_name | initials }}
                    </span>
                    <img
                        v-if="user.profile_pic"
                        :src="user.profile_pic"
                        alt="profilePic"
                    />
                </v-avatar>
                <strong style="margin-left: 5px">
                    {{ user.full_name | shorten }}
                </strong>
            </v-btn>
            <v-btn v-if="!loggedIn" router-link :to="{ name: 'Login' }" text>
                Login
            </v-btn>
            <v-btn v-if="loggedIn" @click="signOut" text> Sign Out </v-btn>
        </v-app-bar>
    </div>
</template>

<script>
import HeaderNotifications from "./HeaderNotifications";

export default {
    name: "Header",
    components: {
        "header-notifications": HeaderNotifications,
    },
    data: () => ({
        drawer: null
    }),
    /**
     * Fetches if the user is logged in and then the current user
     */
    computed: {
        loggedIn() {
            return this.$store.getters.loggedIn;
        },
        user: {
            get() {
                return this.$store.getters.getCurrentUser;
            }
        }
    },
    methods: {
        /**
         * Initialises sign out for the user
         */
        signOut() {
            this.$store.dispatch("closeSocket");
            return this.$store.dispatch("destroyToken").then(() => {
                this.$router.push("/login");
            });
        }
    },
    filters: {
        /**
        * Generates a profile image placeholder if no name exists
        * or if name is less than 2 characters
        */
        initials: data => {
            if (!data) return "😁";
            data = data.toString();
            data = data.split(" ");
            if (data.length < 2) return "😁";
            return (
                data[0].charAt(0).toUpperCase() +
                data[1].charAt(0).toUpperCase()
            );
        },
        /**
         * 
         */
        shorten: data => {
            if (window.innerWidth < 440) return "";
            return data;
        }
    }
};
</script>

<style></style>
