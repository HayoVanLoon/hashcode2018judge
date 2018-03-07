Unofficial Google Hash Code 2018 Judge
======================================

This is an (unofficial) AppEngine for judging and scoring solutions to the
Online Qualification Round of Google's 2018 Hash Code contest. 

Problem description and data sets have not been added (yet, pending an
outstanding question with Google's organisers regarding licenses).


## Maven
Works like any other AppEngine.

### Running locally

    mvn appengine:run

### Deploying

    mvn appengine:deploy

## Limiting Access
Access to the AppEngine is managed through the ```auth.yaml``` resource file. It
features a very basic filter on the host name of the user's e-mail address.

The default value is 'example.com'. If no such filter is required, an empty file
should limit access to authenticated by their Google account. Removing the file 
entirely will grant access to anyone.