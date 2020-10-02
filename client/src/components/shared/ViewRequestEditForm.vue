<template>
  <v-card>
    <v-card-title>
      <span class="headline">{{ request.title }}</span>
      <v-chip
        class="ma-2"
        :color="getColor(request.status.status)"
        text-color="white"
      >{{ request.status.status }}</v-chip>
    </v-card-title>
    <v-card-text>
      <v-form v-model="valid">
        <v-row>
          <v-col cols="12">
            <v-btn text>
              <v-avatar color="light-blue" size="36">
                <span v-if="!request.owner.profile_pic" class="white--text headline">{{ request.owner.full_name | initials }}</span>
                <img
                  v-if="request.owner.profile_pic"
                  :src="request.owner.profile_pic"
                  alt="profilePic"
                />
              </v-avatar>
              <strong style="margin-left: 5px">{{ request.owner.full_name }}</strong>
            </v-btn>
          </v-col>
          <v-col cols="12">
            <v-text-field type="text" label="Title" v-model="request.title" :rules="titleRules"></v-text-field>
          </v-col>
          <v-col cols="12" sm="6" md="6">
            <v-text-field
              type="date"
              label="Start*"
              v-model="request.start"
              :min="today"
              :rules="startRules"
              required
            ></v-text-field>
          </v-col>
          <v-col cols="12" sm="6" md="6">
            <v-text-field
              type="date"
              label="End*"
              v-model="request.end"
              :min="today"
              :rules="endRules"
              required
            ></v-text-field>
          </v-col>
          <v-col cols="12" sm="6" md="6" v-if="currentUser.is_admin && currentUser.id !== request.owner.owner_id">
            <v-radio-group v-model="selectedStatus">
              <v-radio 
              color="warning"
              :label="status[0].status" 
              :value="status[0].id"
              ></v-radio>
              <v-radio 
              color="success"
              :label="status[1].status" 
              :value="status[1].id"
              ></v-radio>
              <v-radio 
              color="error"
              :label="status[2].status" 
              :value="status[2].id"
              ></v-radio>
            </v-radio-group>
          </v-col>
        </v-row>
      </v-form>
    </v-card-text>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn color="red darken-1" text @click="changeMode">Cancel</v-btn>
      <v-btn color="primary darken-1" text @click="submit" :disabled="!valid">Submit</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: "ViewRequestEditForm",
  data() {
    return {
      today: new Date().toJSON().slice(0, 10),
      valid: true,
      selectedStatus: this.request.status.id,
      titleRules: [
        (v) => (v && v.length >= 10) || "Title must be 10 characters or more",
      ],
      startRules: [
        (v) => !!v || "Start date is required",
        (v) =>
          (this.checkifDateInPeriod(v) && this.checkPeriod(v, this.request.end)) ||
          "Period is ineligible for vacation request",
      ],
      endRules: [
        (v) => !!v || "Start date is required",
        (v) => (v && v > this.request.start) || "End date can not be before start date",
        (v) =>
          (this.checkifDateInPeriod(v) && this.checkPeriod(this.request.start, v)) ||
          "Period is ineligible for vacation request",
      ],
    };
  },
  props: ["request"],
  created() {
    this.$store.dispatch("retrieveIneligiblePeriods");
    this.$store.dispatch("retrieveStatus");
  },
  computed: {
    ineligiblePeriods: {
      get() {
        return this.$store.getters.getIneligiblePeriod;
      },
    },
    currentUser() {
      return this.$store.getters.getCurrentUser;
    },
    status: {
      get() {
        return this.$store.getters.getStatus;
      }
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
  },
  methods: {
    closeModal() {
      this.$emit("closeModal");
    },
    changeMode() {
      this.$emit("changeMode");
    },
    getColor(status) {
      switch (status) {
        case "Pending":
          return "orange";
        case "Approved":
          return "green";
        case "Denied":
          return "red";
      }
    },
    submit() {
      if (this.valid) {
        this.$store
          .dispatch("patchRequest", {
            id: this.request.id,
            title: this.request.title,
            start: this.request.start,
            end: this.request.end,
            status: this.status.filter(s => s.id === this.selectedStatus)[0]
          })
          .then(() => {
            this.changeMode();
            this.closeModal();
          })
          .catch(() => {
          });
      } else {
        alert("You have to fill out all the required fields");
      }
    },
    checkifDateInPeriod(date) {
      let isValid = true;
      if (this.ineligiblePeriods) {
        this.ineligiblePeriods.forEach((period) => {
          if (date >= period.start && date <= period.end) {
            isValid = false;
          }
        });
      } else {
        isValid = true;
      }
      return isValid;
    },
    checkPeriod(start, end) {
      let isValid = true;
      if (this.ineligiblePeriods) {
        this.ineligiblePeriods.forEach((period) => {
          if (
            start < period.start &&
            start < period.end &&
            end > period.start &&
            end > period.end
          ) {
            isValid = false;
          }
        });
      } else {
        isValid = true;
      }
      return isValid;
    },
  },
};
</script>

<style>
</style>