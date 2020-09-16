<template>
    <div>
        <v-sheet tile height="54" color="grey lighten-3" class="d-flex">
            <v-btn icon class="ma-2" @click="$refs.calendar.prev()">
                <v-icon>mdi-chevron-left</v-icon>
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn icon class="ma-2" @click="$refs.calendar.next()">
                <v-icon>mdi-chevron-right</v-icon>
            </v-btn>
        </v-sheet>
        <v-sheet height="72vh">
            <v-calendar
                ref="calendar"
                v-model="value"
                :weekdays="weekday"
                :type="type"
            ></v-calendar>
        </v-sheet>
    </div>
</template>

<script>
export default {
    name: 'DashboardCalander',
    data: () => ({
      type: 'month',
      mode: 'stack',
      weekday: [1, 2, 3, 4, 5, 6, 0],
      value: '',
    }),
    methods: {
    },
  }
</script>

<style></style>
