/*  Users (1 admin, 2 regular)   */
INSERT INTO app_user (email, full_name, password, is_admin,
                profile_pic, two_factor_auth, vacation_days,
                used_vacation_days, is_active, created_at,
                modified_at)
                VALUES ('Selma@tidsbanken.se', 'Selma Nilsson', '123456',
                false, null, false, 30, 0, true, TO_DATE('15/09/2020', 'DD/MM/YYYY'),
                TO_DATE('15/09/2020', 'DD/MM/YYYY'));

INSERT INTO app_user (email, full_name, password, is_admin,
                profile_pic, two_factor_auth, vacation_days,
                used_vacation_days, is_active, created_at,
                modified_at)
                VALUES ('stefan@tidsbanken.se', 'Stefan Svensson', '123456',
                true, null, false, 30, 0, true, TO_DATE('15/09/2020', 'DD/MM/YYYY'),
                TO_DATE('15/09/2020', 'DD/MM/YYYY'));

INSERT INTO app_user (email, full_name, password, is_admin,
                profile_pic, two_factor_auth, vacation_days,
                used_vacation_days, is_active, created_at,
                modified_at)
                VALUES ('gunnar@tidsbanken.se', 'Gunnar Andersson', '123456',
                false, null, false, 30, 0, true, TO_DATE('15/09/2020', 'DD/MM/YYYY'),
                TO_DATE('15/09/2020', 'DD/MM/YYYY'));

/* Status */
INSERT INTO status (status) VALUES ('Pending');
INSERT INTO status (status) VALUES ('Approved');
INSERT INTO status (status) VALUES ('Denied');

/* VacationRequest (User:Nr | 1:2, 2:3, 3:1) */
INSERT INTO vacation_request (created_at, moderation_date, modified_at,
                period_end, period_start, title, moderator_user_id,
                owner_user_id, request_status_status_id)
                VALUES (TO_DATE('15/09/2020', 'DD/MM/YYYY'), null,
                TO_DATE('15/09/2020', 'DD/MM/YYYY'),
                TO_DATE('31/12/2020', 'DD/MM/YYYY'),
                TO_DATE('15/12/2020', 'DD/MM/YYYY'),
                'Christmas Vacation', null, 1, 1);

INSERT INTO vacation_request (created_at, moderation_date, modified_at,
                period_end, period_start, title, moderator_user_id,
                owner_user_id, request_status_status_id)
                VALUES (TO_DATE('01/08/2020', 'DD/MM/YYYY'),
                TO_DATE('11/09/2020', 'DD/MM/YYYY'),
                TO_DATE('11/09/2020', 'DD/MM/YYYY'),
                TO_DATE('25/10/2020', 'DD/MM/YYYY'),
                TO_DATE('15/10/2020', 'DD/MM/YYYY'),
                'Sports Week Vacation', 2, 1, 2);

INSERT INTO vacation_request (created_at, moderation_date, modified_at,
                period_end, period_start, title, moderator_user_id,
                owner_user_id, request_status_status_id)
                VALUES (TO_DATE('15/09/2020', 'DD/MM/YYYY'), null,
                TO_DATE('15/09/2020', 'DD/MM/YYYY'),
                TO_DATE('05/01/2021', 'DD/MM/YYYY'),
                TO_DATE('22/12/2020', 'DD/MM/YYYY'),
                'End of the Year Thailand Trip', null, 3, 1);

INSERT INTO vacation_request (created_at, moderation_date, modified_at,
                period_end, period_start, title, moderator_user_id,
                owner_user_id, request_status_status_id)
                VALUES (TO_DATE('01/05/2020', 'DD/MM/YYYY'),
                TO_DATE('25/05/2020', 'DD/MM/YYYY'),
                TO_DATE('25/05/2020', 'DD/MM/YYYY'),
                TO_DATE('20/06/2020', 'DD/MM/YYYY'),
                TO_DATE('10/06/2020', 'DD/MM/YYYY'),
                'Mallorca!!', 2, 3, 3);

INSERT INTO vacation_request (created_at, moderation_date, modified_at,
                period_end, period_start, title, moderator_user_id,
                owner_user_id, request_status_status_id)
                VALUES (TO_DATE('15/08/2020', 'DD/MM/YYYY'),
                TO_DATE('15/09/2020', 'DD/MM/YYYY'),
                TO_DATE('15/09/2020', 'DD/MM/YYYY'),
                TO_DATE('16/11/2020', 'DD/MM/YYYY'),
                TO_DATE('12/11/2020', 'DD/MM/YYYY'),
                'Long Weekend with Family', 2, 3, 2);

INSERT INTO vacation_request (created_at, moderation_date, modified_at,
                period_end, period_start, title, moderator_user_id,
                owner_user_id, request_status_status_id)
                VALUES (TO_DATE('15/09/2020', 'DD/MM/YYYY'), null,
                TO_DATE('15/09/2020', 'DD/MM/YYYY'),
                TO_DATE('30/01/2021', 'DD/MM/YYYY'),
                TO_DATE('15/01/2021', 'DD/MM/YYYY'),
                'Two Week China Trip', null, 3, 1);

/* Comments */

INSERT INTO comment (created_at, message, modified_at, request_request_id, user_user_id)
                VALUES (TO_DATE('16/09/2020', 'DD/MM/YYYY'), 'Two week christmas ' ||
                 'vacation seems kinda long does it not? I can give you 7 days. We ' ||
                  'need you at work.', TO_DATE('16/09/2020', 'DD/MM/YYYY'), 1, 2);

INSERT INTO comment (created_at, message, modified_at, request_request_id, user_user_id)
                VALUES (TO_DATE('01/08/2020', 'DD/MM/YYYY'), 'I am going to the alpes with my boyfriends family for ' ||
                 '10 days.',
                TO_DATE('01/08/2020',
                'DD/MM/YYYY'), 2, 1);

INSERT INTO comment (created_at, message, modified_at, request_request_id, user_user_id)
                VALUES (TO_DATE('11/09/2020', 'DD/MM/YYYY'), 'That sounds great! Have fun!',
                TO_DATE('11/09/2020',
                'DD/MM/YYYY'), 2, 2);

INSERT INTO comment (created_at, message, modified_at, request_request_id, user_user_id)
                VALUES (TO_DATE('15/09/2020', 'DD/MM/YYYY'), 'Celebrating New Years in Bangkok, what could be better?',
                TO_DATE('15/09/2020',
                'DD/MM/YYYY'), 3, 3);

INSERT INTO comment (created_at, message, modified_at, request_request_id, user_user_id)
                VALUES (TO_DATE('25/05/2020', 'DD/MM/YYYY'), 'The deadline for your assignment is the week after. I ' ||
                 'feel like this is not appropriate.',
                TO_DATE('25/05/2020',
                'DD/MM/YYYY'), 4, 2);

INSERT INTO comment (created_at, message, modified_at, request_request_id, user_user_id)
                VALUES (TO_DATE('15/08/2020', 'DD/MM/YYYY'), 'It is my moms 60th birthday and we have invited the ' ||
                 'whole family to a nice cottage in Storuman. I just need friday and monday of for preparations.',
                TO_DATE('15/08/2020',
                'DD/MM/YYYY'), 5, 3);

INSERT INTO comment (created_at, message, modified_at, request_request_id, user_user_id)
                VALUES (TO_DATE('15/09/2020', 'DD/MM/YYYY'), 'That sounds lovely! Say hi to Anette for me!',
                TO_DATE('15/09/2020',
                'DD/MM/YYYY'), 5, 2);

INSERT INTO comment (created_at, message, modified_at, request_request_id, user_user_id)
                VALUES (TO_DATE('16/09/2020', 'DD/MM/YYYY'), 'I will! Thank you!',
                TO_DATE('16/09/2020',
                'DD/MM/YYYY'), 5, 3);

INSERT INTO comment (created_at, message, modified_at, request_request_id, user_user_id)
                VALUES (TO_DATE('15/09/2020', 'DD/MM/YYYY'), 'So far I am the only admin.. Who will approve this? I ' ||
                 'need to hire someone..',
                TO_DATE('15/09/2020',
                'DD/MM/YYYY'), 6, 2);


/* IneligiblePeriod */
INSERT INTO ineligible_period (created_at, modified_at, period_end, period_start, moderator_user_id)
                VALUES (TO_DATE('15/09/2020', 'DD/MM/YYYY'), TO_DATE('15/09/2020', 'DD/MM/YYYY'), TO_DATE('30/06/2020',
                'DD/MM/YYYY'), TO_DATE('10/09/2020',
                'DD/MM/YYYY'), 2);

INSERT INTO ineligible_period (created_at, modified_at, period_end, period_start, moderator_user_id)
                VALUES (TO_DATE('15/09/2020', 'DD/MM/YYYY'), TO_DATE('15/09/2020', 'DD/MM/YYYY'), TO_DATE('14/09/2020',
                'DD/MM/YYYY'), TO_DATE('09/10/2020',
                'DD/MM/YYYY'), 2);

INSERT INTO ineligible_period (created_at, modified_at, period_end, period_start, moderator_user_id)
                VALUES (TO_DATE('15/09/2020', 'DD/MM/YYYY'), TO_DATE('15/09/2020', 'DD/MM/YYYY'), TO_DATE('01/11/2020',
                'DD/MM/YYYY'), TO_DATE('10/11/2020',
                'DD/MM/YYYY'), 2);

