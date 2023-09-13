# platkmframework-boot

Framework para el desarrollo de aplicación para la plataforma java. 
Utiliza invsersión del control e incluye la libraria que implenta JPA framework-jpa

Algunas de las configuraciones y funcionalidades:

# application.properties
```
org.platkmframework.security.type=<nombre para el tag de seguridad>
org.platkmframework.server.name=<servidor>
org.platkmframework.server.port=<puerto> 
org.platkmframework.server.patterns=<patterns de la url>
org.platkmframework.server.public.paths=<api path para funciones públicas>
org.platkmframework.server.appname=<nombre de la aplicación>
org.platkmframework.server.stopkey=<token para detener el servicio>
org.platkmframework.server.maxthreads=<cantidad maxima de hijos>
org.platkmframework.server.active.queue.pool=<activar o no el pull de clases>
org.platkmframework.configuration.custom.properties.file=<camino de ficheros<camino de ficheros de configuración adicionales>
org.platkmframework.configuration.package.prefix=<paquete raiz para el proceso de IC >
org.platkmframework.configuration.custom.properties.file=<ficheros de propiedades adicionales y/o de otras librerías>
##CORS
System_Access-Control-Allow-Origin=<cors origin>
System_Access-Control-Allow-Methods=<access method>
System_Access-Control-Allow-Headers=<access header>
platform.format.time=HH:mm
platform.format.date=dd-MM-yyyy
platform.format.datetime=dd-MM-yyyy:HH:mm
```

# pom.xml
```
...
...

<repositories>
    ...
    <repository>
	<id>platkmframework</id>
	<name>Releases</name>
	<url>https://nexus.platkmframework.io/repository/platkmframework-releases/</url>
   </repository>
    ...
</repositories>
  <build>   
    <plugins>
      <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.0</version>
        <configuration>
          <release>20</release>
        </configuration>
      </plugin>
		<plugin>
			<artifactId>maven-assembly-plugin</artifactId>
	        <version>3.4.2</version>
	        <configuration>
				 <archive>
		            <manifest>
		                <mainClass>
		                    org.platkmframework.boot.server.runner.PlatkmFrameworkApplication
		                </mainClass>
		            </manifest>
		        </archive>
	          <descriptorRefs>
	            <descriptorRef>jar-with-dependencies</descriptorRef>
	          </descriptorRefs>
	        </configuration>
	        <executions>
	          <execution>
	            <id>make-assembly</id>  
	            <phase>package</phase>
	            <goals>
	              <goal>single</goal>
	            </goals>
	          </execution>
	        </executions>
		</plugin>    
    </plugins>
  </build>
...
 <dependencies>
 	...
 	...
   	<dependency>
  		<groupId>org.platkmframework</groupId>
  		<artifactId>plakmframework-boot</artifactId>
  		<version>0.1.0-alpha</version>
  	</dependency> 
  	...
  	...
 </dependencies>
...
```
