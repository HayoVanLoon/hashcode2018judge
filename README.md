Unofficial Google Hash Code 2018 Judge
======================================

This is an (unofficial) AppEngine for judging and scoring solutions to the
Online Qualification Round of Google's 2018 Hash Code contest. 

Problem description and data sets provided here by courtesy of the Google Hash 
Code team. All rights reserved to them.


## Maven
Works like any other AppEngine.

### Running locally

    mvn appengine:run

### Deploying

    mvn appengine:deploy

## Application Settings
Settings are managed through the ```settings.yaml``` resource 
file.

### Limiting Access
The settings file is used to create a very basic filter on the host name of the 
user's e-mail address (from their Google Account).

I will assume the example snippet below self-explanatory; it illustrates all 
filter types. All are optional (so they need not overlap like here).

```
email-filter:
  by-host: example.com
  by-regex: ".*@example\\.com"
  exact:
    - test@example.com
    - alice@example.com
    - bob@example.com
```

### Securing High Scores
(Personal) high scores are stored in Datastore with entity kind 
```hashcodejudge_score```. Tampering with scores is limited through a simple 
hash verification with a secret key. On tampering, the entity will be 
reinitialised and the score(s) wiped. 

The secret is stored in the ```score-secret``` 
attribute. If absent, verification will be skipped. 
