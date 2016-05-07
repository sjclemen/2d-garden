2D Garden: A Desktop Comics Database
====================================

Goals
-----

Managing a large comics collection on both a desktop computer (in CBR/CBZ
format) and on shelves has long been problematic, with no way to keep track
of which comics one has, what condition they're in, who they're written by,
your review of each of them, etc. There are applications which handle only
physical collections, and some mediocre ones to handle digital collections,
but none which do both, and 2D Garden's objective is to make keeping track
of a collection simple.

How is it built?
----------------

2D Garden is a crossplatform desktop Java application, using SWT, JFace,
Hibernate and Apache Derby to make a responsive application. Given that
storing a digital collection in the cloud would cause potential copyright
issues, I have chosen to make this a desktop application.

Present status
--------------

The application will index directories, looking for comics and populate the
GUI with them. Tags can be created (within a tag category) and attached to
a comic. Thumbnails can be seen. No other functionality exists, though
automatic tagfilling is planned next.

Getting started
---------------

2D Garden can be built with Maven, but requires manually pulling in some
third party libraries, notably JFace. For now, make sure to have the entire
Eclipse RCP platform libraries if you'd like to build 2D Garden.

