<template>
  <v-card v-if="request.status">
        <v-card-title>
            <span class="headline">{{ request.title }}</span>
            <v-chip class="ma-2" :color="getColor(request.status.status)" text-color="white">
                    {{ request.status.status }}
                </v-chip>
        </v-card-title>
        <v-card-text>
            <v-form v-model="valid">
                <v-row>
                    <v-col cols="12">
                        <v-btn text>
                            <v-avatar color="light-blue" size="36">
                                <span v-if="!request.owner.profile_pic" class="white--text headline">UU</span>
                                <img v-if="request.owner.profile_pic" :src="request.owner.profile_pic" alt="profilePic">
                            </v-avatar>
                            <strong style="margin-left: 5px">
                                {{ request.owner.full_name }}</strong
                            >
                        </v-btn>
                    </v-col>
                     <v-col cols="12">
                        <v-text-field
                            type="text"
                            label="Title"
                            v-model="title"
                            :rules="titleRules"
                        ></v-text-field>
                    </v-col>
                    <!-- Not implemented period start and end -->
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field
                            type="date"
                            label="Start*"
                            v-model="start"
                            :min="today" 
                            :rules="startRules"
                            required
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="6">
                        <v-text-field
                            type="date"
                            label="End*"
                            v-model="end"
                            :min="today" 
                            :rules="endRules"
                            required
                        ></v-text-field>
                    </v-col>
                </v-row>
            </v-form>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="red darken-1" text @click="changeMode"
                >Cancel</v-btn
            >
            <v-btn color="primary darken-1" text @click="submit" :disabled="!valid"
                >Submit</v-btn
            >
        </v-card-actions>
    </v-card>
</template>

<script>
export default {
    name: 'ViewRequestEditForm',
    data () {
        return {
            today: new Date().toJSON().slice(0,10),
            valid: true,
            title: this.request.title,
            titleRules: [
              v => (v && v.length >= 10) || 'Title must be 10 characters or more',
            ],
            start: this.request.start,
            startRules: [
              v => !!v || "Start date is required",
            ],
            end: this.request.end,
            endRules: [
              v => !!v || "Start date is required",
              v => (v && v > this.start) || "End date can not be before start date"
            ]
        }
    },
    props: [
        'request'
    ],
    methods: {
        closeModal() {
            this.$emit('closeModal');
        },
        changeMode() {
            this.$emit('changeMode');
        },
        getColor (status) {
          switch(status) {
            case 'Pending':
                  return 'orange';
            case 'Approved':
                return 'green';
            case 'Denied':
                return 'red';
          }
        },
        submit() {
        if (this.valid) {
          // do stuff
          // If title is "" then use the old title
          alert("You're vacation request has successfully been edited");
          this.dialog = false;
        } else {
          alert("You have to fill out all the required fields")
        }
      }
    },
}
</script>

<style>

</style>