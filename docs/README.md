# RIO BurpSuite plugin 
Request Input Output BurpSuite plugin A.K.A RIO - A handy plugin for copying requests/responses directly from Burp, some extra magic included.

## Why ?
TL;DR

* It saves time (on both ends - hacker and customer )
* It speeds up a reporting process (you don't have to format the request manually, copy application specific headers/cookies each time)

### Intro 
How many times you had to copy the request/response from Repeater?  
How many times you had to adjust the output to specific format? 
How many times you were hacking an app with custom headers that had to be included in the report?

If your answer for those question happens to be  999 or more, than I have a solution that will save you some time.

## How ? 
1. Define a template
2. Use this template with a request`
3. Copy the output from RIO window
4. Paste it to the PoC section of report
5. Done. 

### Few words on response output
* The plugin was created with Markdown  as an desired output format but you can use whatever output format you want 

```text
<request>
<target>
_target_
</target>
<url>
_url_
</url>
</request>
```
becomes
```xml
<request>
<target>
normandy.cdn.mozilla.net:443 (https)
</target>
<url>
https://normandy.cdn.mozilla.net:443/api/v1/
</url>
</request>
```
### Few words on performance
RIO creates a new window for each Repeater tab that you have, so if you have 100 tabs it will take some time to load the plugin .


### Few words on specials characters
Plugin utilizes BurpSuite MessageEditor for output display, therefore the output will support the same character charset as burp. 
It means that you can't display special characters from some languages i.e `ąść` from Polish alphabet. 

### Few words on BurpSuite MessageEditor tab
In some cases you may experience following exception
```
Cannot invoke "burp.il2.a(burp.ad0)" because "<parameter1>" is null
```
This issue is known to PortSwigger as I reported it [here](https://forum.portswigger.net/thread/burpextenderapi-java-lang-nullpointerexception-cannot-invoke-burp-ehv-a-burp-ikb-because-parameter1-is-null-a81e3915)

# TODOs

* Set a limit on size of the response 
* Implement whitelist for allowed response headers 
