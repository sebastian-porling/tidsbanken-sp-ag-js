<template>
  <v-row class="fill-height">
    <v-col>
      <v-sheet height="64">
        <v-toolbar flat color="white">
          <v-btn outlined class="mr-4" color="grey darken-2" @click="setToday">
            Today
          </v-btn>
          <v-btn fab text small color="grey darken-2" @click="prev">
            <v-icon small>mdi-chevron-left</v-icon>
          </v-btn>
          <v-btn fab text small color="grey darken-2" @click="next">
            <v-icon small>mdi-chevron-right</v-icon>
          </v-btn>
          <v-toolbar-title v-if="$refs.calendar">
            {{ $refs.calendar.title }}
          </v-toolbar-title>
          <v-spacer></v-spacer>
          <v-menu bottom right>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                outlined
                color="grey darken-2"
                v-bind="attrs"
                v-on="on"
              >
                <span>{{ typeToLabel[type] }}</span>
                <v-icon right>mdi-menu-down</v-icon>
              </v-btn>
            </template>
            <v-list>
              <v-list-item @click="type = 'day'">
                <v-list-item-title>Day</v-list-item-title>
              </v-list-item>
              <v-list-item @click="type = 'week'">
                <v-list-item-title>Week</v-list-item-title>
              </v-list-item>
              <v-list-item @click="type = 'month'">
                <v-list-item-title>Month</v-list-item-title>
              </v-list-item>
              <v-list-item @click="type = '4day'">
                <v-list-item-title>4 days</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </v-toolbar>
      </v-sheet>
      <v-sheet height="600">
        <v-calendar
          ref="calendar"
          v-model="focus"
          color="primary"
          :events="events"
          :event-color="getEventColor"
          :type="type"
          @click:event="launchModal"
          @click:more="viewDay"
          @click:date="viewDay"
          @change="updateRange"
        ></v-calendar>
      </v-sheet>
    </v-col>
      <view-request-modal :active="activateModal" :request="request" @closeModal="closeModal"/>
  </v-row>
</template>

<script>
import response from '../../../mock_data/get_request';
import ipResponse from '../../../mock_data/get_ineligibles';

 import ViewRequestModal from '../../components/shared/ViewRequestModal'

  export default {
    name: "DashboardCalander",
    components: {
       'view-request-modal': ViewRequestModal
     },
    data: () => ({
        requests: response.data,
        ineligiblePeriods: ipResponse.data,
      focus: '',
      type: 'month',
      typeToLabel: {
        month: 'Month',
        week: 'Week',
        day: 'Day',
        '4day': '4 Days',
      },
      events: [],
      colors: ['red', 'orange', 'green'],
      names: [],
        activateModal: false,
        request: 0
    }),
    mounted () {
      this.$refs.calendar.checkChange()
    },
    methods: {
      viewDay ({ date }) {
        this.focus = date
        this.type = 'day'
      },
      getEventColor (event) {
        return event.color
      },
      setToday () {
        this.focus = ''
      },
      prev () {
        this.$refs.calendar.prev()
      },
      next () {
        this.$refs.calendar.next()
      },
      updateRange () {
        const events = []

            this.ineligiblePeriods.forEach(period => {
                events.push({
                    name: "Ineligible Period",
                    start: period.period_start,
                    end: period.period_end,
                    color: 'grey',
                })
            })

            this.requests.forEach(request => {
                events.push({
                    name: request.owner.name + ": " + request.title,
                    start: request.period_start,
                    end: request.period_end,
                    color: this.getColor(request.status),
                }) 
            }); 

        this.events = events
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
        launchModal(value) {
        this.request = value.request_id;
        this.activateModal = true;
      },
      closeModal() {
        this.request = 0;
        this.activateModal = false;
      }
    },
  }
</script>

<style></style>
