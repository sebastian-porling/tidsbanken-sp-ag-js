<template>
    <v-dialog v-model="dialog" width="600px">
      <template v-slot:activator="{ on, attrs }">
          <v-btn  
          v-bind="attrs"
          v-on="on">
            Edit User
          </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="headline">Edit user</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="10">
                <v-text-field label="Name*" required></v-text-field>
              </v-col>
              <v-col cols="10">
                <v-text-field label="Email*" required></v-text-field>
              </v-col>
              <v-col cols="10">
                <v-text-field label="Password*" required></v-text-field>
              </v-col>
        <br>
              <v-col cols="10">
             <v-text-field label="Number of vacation days" required></v-text-field>
              </v-col>
               <p>Admin Privilages</p>
               <v-radio-group v-model="row" row :mandatory="false">
      <v-radio label="Yes" value="yes"></v-radio>
      <v-radio label="No" value="no"></v-radio>
    </v-radio-group>

            </v-row>
          </v-container>
          <small>*indicates required field</small>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="dialog = false">Cancel</v-btn>
          <v-btn color="green darken-1" text @click="dialog = false">Edit</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>

<script>
export default {
    name: 'EditUserModal',
    data() {
        return {
            dialog: false
        }
    },
}
</script>

<style>

</style>