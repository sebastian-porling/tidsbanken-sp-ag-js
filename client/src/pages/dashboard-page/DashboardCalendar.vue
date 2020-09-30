<template>
  <v-row class="fill-height">
    
    <v-col>
      <v-sheet height="64">
        <v-toolbar flat color="white">
          <v-btn text class="mr-4" color="grey darken-2" @click="setToday">
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
      <view-request-modal v-if="!rerender" :active="activateModal === 'request'" :request="vacationRequest" @closeModal="closeModal"/>
      <edit-ip-modal v-if="!rerender" :active="activateModal === 'ip'" :ineligible="ineligiblePeriod" @closeModal="closeModal"/>
  </v-row>
</template>

<script>
 import ViewRequestModal from '@/components/shared/ViewRequestModal'
 import EditIpModal from './EditIpModal'

  export default {
    name: "DashboardCalander",
    components: {
       'view-request-modal': ViewRequestModal,
       'edit-ip-modal': EditIpModal,
     },
    data: () => ({
      focus: '',
      type: 'month',
      events: [],
      colors: ['red', 'orange', 'green'],
      names: [],
      activateModal: '',
      ineligiblePeriod: null,
      vacationRequest: null,
      rerender: false
    }),
    
   created() {
      this.$store.dispatch('retrieveAllRequests')
      this.$store.dispatch('retrieveIneligiblePeriods')
    },
    computed: {
      requests: {
        get() {
          return this.$store.getters.getAllRequests;
        }
      },
      ineligibles: {
        get() {
          return this.$store.getters.getIneligiblePeriods;
        }
      },
      isAdmin: {
        get() {
          return this.$store.getters.isAdmin;
        }
      }
    },
    watch: {
      requests() {
        this.updateRange();
      },
      ineligibles() {
        this.updateRange();
      }
    },
  
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
            this.ineligibles.forEach((ineligible, index) => {
                events.push({
                    name: "Ineligible Period",
                    start: ineligible.start,
                    end: ineligible.end,
                    color: 'grey',
                    type: 'ip',
                    index
                })
            })

           this.requests.forEach((request, index) => {
                events.push({
                    name: request.owner.full_name + ": " + request.title,
                    start: request.start,
                    end: request.end,
                    color: this.getColor(request.status.status),
                    type: 'request',
                    index
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
          if ( this.isAdmin && value.event.type === 'ip') {
            this.ineligiblePeriod = this.ineligibles[value.event.index]
            this.activateModal = value.event.type;
          } else if (value.event.type !== 'ip') {
            this.vacationRequest = this.requests[value.event.index];
            this.activateModal = value.event.type;
          }
          this.rerender = false;
      },
      closeModal() {
        this.activateModal = '';
        setTimeout(() => {
          this.rerender = true;
        }, 500);
      }
    },
  }
</script>

<style></style>
