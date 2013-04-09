
id2210 Decentralized Search Project, vt 2013
===
There are 3 main modules, cyclon, tman, and search, as well as a common module.
You can run experiments on the cyclon module or the search module.

Build instructions:
===
Install git, maven and a java SDK.

I recommend you fork this repo on github. But if you don't want to, then just clone it:
git clone https://github.com/jimdowling/id2210-vt13.git

cd id2210-vt13
mvn clean install

The search module specifies a kompics experiment in the main experiment.
You can execute the main for the search program using:

cd search
mvn exec:java -Dexec.mainClass="search.main.Main" -Dexec.args=""


The REST API for the search module contains 2 operations:

1. Search for an entry
* http://127.0.1.1:9999/1/1/search-XXX     where XXX is the search string

2. Add an entry
* http://127.0.1.1:9999/1/1/add-XXX-YYY     where XXX is the new entry and YYY is the id of the new entry

Note that the IP address in the URL might be slightly different - it might be localhost or 127.0.0.1 depending on your OS.
Check in the first lines printed out when running the program.
