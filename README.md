###
WARNING!!! Not implemented. Status: in progress

## About 
**PortTo** - tools fot expose localhost port to developer office network or internet.

**Motivation:** I  want to use @SpringBootTest and Selenium Grid. 
Test run  from Gitlab-CI in docker container. Browser (selenium)  run in docker container.
All this run in office network. 

I wont annotation @PortTo,which gives me "host:port" reachable from internet.

P.S. 

serveo.net - my inspiration. not self hosted version.  
ngrok - beautiful and paid service.  
forwardhq.com - paid service.  
 

**My work environment:**

![deployment](deployment.png)



## How it's work?

Client library opens **ssh tunnel** to **PortTo Server** and use "remote port forwarding".
WebBrowser  send requests to <portToServer:forwardingPort>. PortTo Server forward request to client localhost interface, from opened tunnel.   