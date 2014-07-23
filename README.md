Spring Batch scaling strategies - sample application
===
http://www.ontheserverside.com/blog/2014/07/23/horizontal-and-vertical-scaling-strategies-for-batch-applications/

This is sample application developed for ["Horizontal and Vertical Scaling Strategies for Batch Applications"](http://www.ontheserverside.com/blog/2014/07/23/horizontal-and-vertical-scaling-strategies-for-batch-applications/) article. 

Application is responsible for processing Elixir0 messages. Elixir0 is a country-domestic Polish bank message format that represents standard credit transfer transactions. All payments contained by Elixir0 are screened against OFAC's Specially Designated Nationals List. Screening algorithm performs fuzzy matching of beneficiary and ordering party name, address and alternate name against every SDN entity. If algorithm detects that sanctions might be imposed on ordering or receiving party, placement of the particular transaction is suspended and waits for manual approval. 

Please bear in mind that the implementation of payment sanction screening mechanism used here is not the most optimal and performant algorithm for this task. Also, it should not be considered as a complete solution.

## How to run

Application is using Maven as a dependency management system, so all required libraries should be downloaded automatically. FFMQ is used as a JMS broker, Jetty is used as a web container and H2 running in in-memory mode is used as a database. All these services are configured automatically and no action from user is required to set them up.

```
$ git clone git@github.com:pdyraga/spring-batch-samples.git
$ cd spring-batch-samples/spring-batch-scalability/
$ mvn clean jetty:run
```


## How to use

Go to [http://localhost:8080](http://localhost:8080) and generate sample Elixir0 message first. To do so, please enter number of transactions that the generated file should contain (e.g. `500`) and desired location of the output file (e.g. `/tmp/elixir0.500`). Next, please click on `Generate` and wait for Elixir0 file to be created. When file generation process is complete, you should see `Elixir0 message created` information. Also, input file path is automatically filled with location of file that has just been created. In the menu drop down next to the input file location you can choose scaling strategy that should be used for performing payment sanctions screening. After choosing some scaling strategy, please click on `Run job` button and the import process should start immediately:

![image](https://cloud.githubusercontent.com/assets/4712360/3423638/cddb7082-ffa1-11e3-975e-af98ea53980e.png)

You can access database web interface by going to [http://localhost:8082](http://localhost:8082). The username is `sa` and password is `sa`. The most important job outcome is list of transactions that have been suspended together with sanctions matched for particular transaction. You can observe this list by querying the `SANCTION_PREVIEW` view: `SELECT * FROM SANCTION_PREVIEW `. Each row in this view represents suspended transaction. View displays all data from Elixir0 transactions that were analysed by fuzzy string matching algorithm, that is, beneficiary name, beneficiary address, ordering party name and ordering party address. Columns starting with `SDN_` prefix are data from entity on the OFAC's SDN list that could be potentially involved in the particular transaction. For instance, the first row represents transaction that has been suspended because the beneficiary name (`MOLDTRANSAVIA SRL`) is matching SDN entity with the same name.

Please visit the article for more information: http://www.ontheserverside.com/blog/2014/07/23/horizontal-and-vertical-scaling-strategies-for-batch-applications/

![image](https://cloud.githubusercontent.com/assets/4712360/3423640/d5b10236-ffa1-11e3-9c54-93a11beb55e9.png)
