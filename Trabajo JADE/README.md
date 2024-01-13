# Bus Nocturno
Las rutas de autobuses nocturnas suelen tener bastantes paradas por lo que hay muchas quejas
de los usuarios. El ayuntamiento para evitar las quejas ha decidido introducir una nueva línea
que permita hacer atajos de manera que sea posible evitar parte de las paradas de la línea
principal. Tras la remodelación el gráfico de las paradas y líneas es el siguiente:<br>
<img src="Captura de pantalla 2023-11-21 124829.png" style="width: 100%;" alt="Lineas"><br>
Para facilitar el servicio el ayuntamiento ha decidido proporcionar un planificador que permita
a los usuarios determinar la ruta más corta de tiempo entre dos paradas. Para ello ha planteado
el desarrollo de un sistema multiagente con las siguientes características:
- **AgentUsuario:** Será cada uno de los usuarios que se conecten a la plataforma para
solicitar la ruta más corta, proporcionarán al sistema un origen, un destino y una hora
de salida (estos datos se le solicitarán al usuario). El recorrido se lo solicitarán a un
agente que proporcione un servicio de un tipo en concreto y este le devolverá el
recorrido y lo imprimirá por pantalla (Parada, número de línea a coger y hora de salida,
al llegar a la parada mostrará la parada y la hora de llegada ver notas).
- **AgentRuta:** encargado de tratar las peticiones de los usuarios, deberá de ser capaz de
atender hasta 5 peticiones de manera concurrente de modo que si llegan más peticiones
se irán encolando para atenderlas posteriormente. El agente recuperará de cada una de
las líneas toda la información que consideren necesario como podría ser los horarios de
salida de los autobuses, y las conexiones entre las paradas (según la funcionalidad que
se introduzca en el agente línea se le podría solicitar más información). Cada línea tendrá
una agente que gestiona la información de la salida de los buses, las conexiones entre
paradas y el tiempo que se tarda entre conexiones (esta información se recoge en la
imagen). Una vez que el agente ruta tenga toda la información de las líneas procederá
a realizar el cálculo de la ruta y se la devolverá al usuario.
- **AgentLinea:** hay una agente por cada línea que conoce la información sobre el horario
de salida de los autobuses desde la parada inicial, las paradas y el orden de cada línea y
el tiempo estimado para ir de una parada a otra (representado en el gráfico). Los agentes
aceptarán peticiones de información y devolverán los datos que el desarrollador
considere oportuno.<br><br>
A partir de los requisitos anteriores hay que implementar un sistema multiagente con JADE que
permita realizar el cálculo de las rutas.

## Creacion de agentes:
```
-gui
Usuario:es.usal.pa.agent.AgentUsuario;Rutas:es.usal.pa.agent.AgentRuta;LineaA:es.usal.pa.agent.AgentLinea;LineaB:es.usal.pa.agent.AgentLinea
```

## Realizado por:
<table>
   <td align="center"><a href="https://github.com/rubenangber"><img src="https://avatars.githubusercontent.com/u/70805470?v=4" width="100px;" alt=""/><br /><sub><b>Rubén Angoso</b></sub></a><br /> 
   <td align="center"><a href="https://www.instagram.com/martaadcl_/"><img src="https://avatars.githubusercontent.com/u/71805470?v=3" width="100px;" alt=""/><br /><sub><b>Marta de Castro</b></sub></a><br /> 
</table>