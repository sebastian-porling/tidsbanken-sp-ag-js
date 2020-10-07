<template>
<div>
<v-sheet
      v-if="isLoading"
      class="pa-8"
    >
    <v-layout
          justify-center
          align-center 
        >
    <v-row justify="center">
      <v-skeleton-loader
        v-bind="attrs"
        class="mx-auto"
        min-width="600"
        min-height="1200"
        type="avatar, card-heading, list-item, list-item, list-item, actions"
      ></v-skeleton-loader>
      </v-row>
      </v-layout>
    </v-sheet>
   

    <transition name="fade">
      <v-row v-if="!isLoading">
      <v-card max-width="600px" style="padding: 10px; margin: 10px;">
        <v-card-text align="center" justify="center">
          <v-avatar color="indigo" size="120" v-model="user.profile_pic">
            <span v-if="!user.profile_pic" class="white--text headline">
              {{ user.full_name | initials }}
            </span>
            <img v-if="user.profile_pic" :src="user.profile_pic" alt="profilePic" />
          </v-avatar>

          <change-profile-picture-modal :active="activateModal" :user="user" @closeModal="closeModal" />

          <br />
          <v-btn color="default" @click="launchModal" style="margin: 4%" text>Change Picture</v-btn>
          <v-form v-model="valid">
            <v-row>
              <v-col cols="12">
                <v-text-field v-model="user.full_name" label="Name" required :rules="nameRules"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field v-model="user.email" label="Email" required :rules="emailRules"></v-text-field>
              </v-col>
              <br />
              <v-col cols="12">
                <v-text-field
                  type="password"
                  v-model="password"
                  :rules="passwordRules"
                  label="Password"
                  required
                ></v-text-field>
              </v-col>
              <!-- <v-col cols="10">
                <v-text-field
                  type="password"
                  v-model="confirm"
                  :rules="confirmRules"
                  label="Confirm Password"
                  required
                ></v-text-field>
              </v-col>-->
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-2" text @click="openTwoFactorModal">New 2FA</v-btn>
            <v-progress-circular
        v-if="isLoading"
        indeterminate
        color="green"
        ></v-progress-circular>
        <v-btn v-if="!isLoading" color="green darken-1" text @click="submit" :disabled="!valid">Save Changes</v-btn>
        </v-card-actions>
      </v-card>
      
      
      </v-row>
      
    </transition>
    
    </div>
</template>

<script>
import ChangeProfilePictureModal from "./ChangeProfilePictureModal"
export default {
  name: "UserProfileForm",
  components: {
    "change-profile-picture-modal": ChangeProfilePictureModal
  },
  inject: {
    theme: {
      default: { isDark: false },
    },
  },
  data: () => ({
    isLoading: false || true,
    valid: true,
    activateModal: false,
    nameRules: [
      (v) => !!v || "Name is required",
      (v) => (v && v.length <= 100) || "Name must be less than 100 characters",
    ],
    emailRules: [
      (v) => !!v || "E-mail is required",
      (v) => /.+@.+\..+/.test(v) || "E-mail must be valid",
    ],
    password: "",
    passwordRules: [
      (v) => !!v || "Password is required",
      (v) => (v && v.length >= 6) || "Password must be more than 6 characters",
    ],
    /* confirm: "",
    confirmRules: [
      (v) => (v && v === this.password) || "Passwords does not match"
    ], */
  }),
    created() {
    this.$store.dispatch("retrieveAllUsers").then(() => {
      setTimeout(() => (this.isLoading = false), 500);
    });
  },
  computed: {
    loggedIn() {
      return this.$store.getters.loggedIn;
    },
    user: {
      get() {
        return this.$store.getters.getCurrentUser;
        
      },
    },
  },
  methods: {
    twoFactorAuthenticationSettings() {
      this.$refs.form.twoFactorAuthenticationSettings();
    },
    launchModal() {
      this.activateModal = true;
    },
    closeModal() {
      this.activateModal = false;
    },
    submit() {
      this.isLoading = true;
      this.$store
        .dispatch("updateUser", {
          id: this.user.id,
          full_name: this.user.full_name,
          email: this.user.email,
        })
        .then(() => {
          setTimeout(() => {
              this.isLoading = false;
          this.changePassword();
          }, 500)
        })
        .catch(() => {
          setTimeout(() => {
            this.isLoading = false;
            }, 500)
        });
    },
    changePassword() {
      if (this.password !== "" && this.password !== null) {
        this.$store
          .dispatch("changePassword", this.user.id, this.password)
          .then(() => {
          })
          .catch(() => {
          });
      }
    },
    openTwoFactorModal() {
      this.$emit('openTwoFactorModal');
    }
  },
  filters: {
      initials: (data) => {
            if (!data) return '😁';
            data = data.toString();
            data = data.split(' ');
            if(data.length < 2) return '😁';
            return  data[0].charAt(0).toUpperCase() + 
                    data[1].charAt(0).toUpperCase();
        },
    }
};
</script>

<style>
</style>