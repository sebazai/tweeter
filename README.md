# Tweeter

[![CircleCI](https://circleci.com/gh/sebazai/tweeter.svg?style=svg)](https://circleci.com/gh/sebazai/tweeter)
[![codecov](https://codecov.io/gh/sebazai/tweeter/branch/master/graph/badge.svg)](https://codecov.io/gh/sebazai/tweeter)

School project for Java Spring Web-palvelinohjelmointi.
[Details in finnish](https://web-palvelinohjelmointi-s19.mooc.fi/projekti)

Project running at [Heroku](https://serene-cove-90561.herokuapp.com/)

Done:
- Can register / login
- Can post | comment and like photos and posts as follower
- Can follow / unfollow / block
- Can add/delete photos (max 100Kb)
- Change profilepic
- Search user
- Mock system/integration tests

To do:
- Limit and order comments and posts straight from database with Pageable
- Fix database N+1 problem in Spring
- Validations for posts, comments properly
- Unit / integration tests
- If blocked, can't see users wall
- Refactor code
- Search to not be case sensitive
- ... Much more :)
