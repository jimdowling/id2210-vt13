
id2210 Decentralized Search Project, vt 2013
===
There are 3 main modules, cyclon, tman, and search, as well as a common module.
You can run experiments on the cyclon module or the search module.


The search module specifies a kompics experiment in the main experiment.


The REST API for the search module contains 2 operations:

1. Search for an entry
* http://localhost:9999/1/1/search-XXX     where XXX is the search string

2. Add an entry
* http://localhost:9999/1/1/add-XXX-YYY     where XXX is the new entry and YYY is the id of the new entry

