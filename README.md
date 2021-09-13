# Log Parser

## Features
#### Search between dates 
* The date format is: yyyy-MM-dd HH:mm:ss.SSS
* Example:
http://localhost:8080/log/search?from=2021-01-01%2017%3A19%3A58.694&to=2021-01-30%2017%3A19%3A58.694

#### Search by log level
* The options are: DEBUG | INFO | WARN | ERROR
* Example:
http://localhost:8080/log/search?level=WARN

#### Search by text
* Searches for messages that contain the provided string
* Example:
http://localhost:8080/log/search?message=HikariPool%24HouseKeeper

#### A combo of the search options
* Example: 
http://localhost:8080/log/search?from=2021-01-01%2017%3A19%3A58.694&to=2021-01-30%2017%3A19%3A58.694&level=INFO&text=hikari

## Assumptions
* Handling a considerably small log file.
* "to" and "from" are given together. Not only "to" or only "from". This is an easy change, but the exercise required the given date to be between the two.
* Log lines who don't match the criteria (like exception stack trace), are considered as the tail of a previous log line.

## TODO
* Read file in chunks, using BufferedInputStream or other methods.
* Insert log entries in batches, using Repository.saveAll(List<LogEntry>)
* Add the ability to specify a shorter date format, like from=2020-12-31&to=2021-01-30
