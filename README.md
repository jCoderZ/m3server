### IDE ###

Eclipse 3.8M5
Plugins:
* m2e Plugin - via Eclipse Update Site
* subclipse Plugin - via http://subclipse.tigris.org/update_1.8.x/
* Pax Runner - via https://github.com/tux2323/org.ops4j.pax.runner/tree/master/pax-runner-eclipse

### Legacy ###

Use Felix as an OSGi Framework in Eclipse:
http://team.ops4j.org/wiki/display/paxrunner/Pax+Runner

Update to work with Eclipse Version Helios and above:
http://musingsofaprogrammingaddict.blogspot.com/2011/10/using-pax-runner-with-eclipse-36.html
https://github.com/tux2323/org.ops4j.pax.runner/tree/master/pax-runner-eclipse

SLF4J Cyclic Dependecy => Resolved in Eclipse 3.8M5
http://bugzilla.slf4j.org/show_bug.cgi?id=75
http://ekkescorner.wordpress.com/2009/09/04/osgi-logging-part-3-slf4j-logback-as-osgi-bundles/



### Ideas ###
MediaServerCore


BrowseEngine - display a hierarchical structure 
  - FileSystem
  - Shoutcast
SearchEngine - Search a hierarchical structure


Types of plugins:
* Menu Contributors / Backend
* Adaptors
* ...

ProtocolAdaptor
* REST
* UPnP/DLNA
* HTTP
* ...

A ProtocolAdaptor opens a port to provide protocol access to a client.


LibraryAdaptor
* FileSystem
* GoogleMusic
* iTunes
* ...

A library adapter is an adpater for a local or remote set of media files.


SearchAdaptor
* Lucene
* ...

A search adaptor is 

Misc
* Playlist???
* Feeds???
* Shoutcast
    http://code.google.com/p/streamscraper/

Plugin:
   list<???> search(term)

