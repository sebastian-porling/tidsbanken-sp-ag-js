export default {
    status: 200,
    message: "Ok",
    data: [
        {
            request_id: 1,
            title: "Berlin",
            period_start: "2020-10-01",
            period_end: "2020-10-08",
            status: "Pending",
            owner: {
                user_id: 2,
                name: "Sven Torbjörn",
                profile_pic: "https://www.booksie.com/files/profiles/22/mr-anonymous.png"
            }
        },
        {
            request_id: 2,
            title: "Paris",
            period_start: "2020-10-04",
            period_end: "2020-10-14",
            status: "Rejected",
            owner: {
                user_id: 4,
                name: "Svea Lindqvist",
                profile_pic: "https://www.booksie.com/files/profiles/22/mr-anonymous.png"
            }
        },
        {
            request_id: 3,
            title: "Be with the kids",
            period_start: "2020-10-06",
            period_end: "2020-10-19",
            status: "Accepted",
            owner: {
                user_id: 5,
                name: "Karin Lövgren",
                profile_pic: "https://www.booksie.com/files/profiles/22/mr-anonymous.png"
            }
        },
        {
            request_id: 4,
            title: "Training camp",
            period_start: "2020-10-10",
            period_end: "2020-10-17",
            status: "Pending",
            owner: {
                user_id: 6,
                name: "Torsten Sixtensson",
                profile_pic: "https://www.booksie.com/files/profiles/22/mr-anonymous.png"
            }
        },
    ]
}