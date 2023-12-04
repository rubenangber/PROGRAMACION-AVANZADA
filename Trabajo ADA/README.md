# CENTRAL NUCLEAR
Se va a simular el comportamiento de una central ficticia mediante con un comportamiento
simulado.<br>
En una central nuclear se tienen 3 reactores nucleares. Cada reactor tiene que controlar de
modo independiente la temperatura del núcleo para que no sobrepase unos determinados
límites para lo que abre una compuerta de agua para refrigerar el reactor. El funcionamiento
detallado de cada reactor es el siguiente:<br>
- La temperatura de cada reactor se muestrea cada 2 segundos. Al finalizar cada
muestreo se manda un mensaje a una tarea coordinadora para indicar que está vivo.
- Si la temperatura es inferior a 1500º no se hace nada. La lectura de la temperatura
tarda como máximo una décima de segundo (simular por código que tarde este
intervalo de tiempo).
- Si la temperatura es superior o igual a 1500º se abre una compuerta que baja la
temperatura a razón de 50º por segundo. El actuador tarda 1 décima de segundo
como máximo en actuar (simular este comportamiento de igual modo que para el
sensor para abrir y cerrar).
- La compuerta se mantiene abierta mientras la temperatura sea superior a los 1500º.
- Si se supera la temperatura de 1750º se imprime un mensaje y se mantiene la
compuerta abierta.

Para controlar que cada uno de los reactores está funcionando correctamente, hay una tarea
coordinadora por cada reactor que imprime un mensaje de alerta si no recibe un mensaje de
alguna de las tareas que controla un reactor pasado 3 segundos.<br>
Para simular el funcionamiento de la temperatura, se creará una tarea que a razón de 1 vez
cada 2 segundos sube la temperatura de uno de los reactores 150 grados, si siempre le toca al
mismo reactor de manera evidente el reactor superará los 1750º en poco tiempo.<br>
La práctica hay que hacerla con Ada y hay que mantener los mecanismos necesarios para
controlar el acceso a recursos compartidos.<br>
Notas:
- El instante de inicio de todas las tareas y los períodos asociados es el instante inicial de
ejecución de las tareas.
- La temperatura de inicio de los reactores es de 1450º.
- Si el actuador se para antes de cumplir el segundo no bajaría la temperatura.
- Se deben de crear los mecanismos de sincronización que se estimen oportunos para
controla el acceso a los datos.
- Se recomienda implementar un sensorlector y un actuador que permitan acceder y
modificar la producción de cada una de las centrales.
- Se recomienda seguir el ejemplo dado en SensorLectorP y ActuadorEscritorP para la
realización de la práctica.
- Si se dice que una acción ocurre cada determinado período, ocurre cada ese
determinado período con independencia de lo que se tarde en procesar los datos que
hay antes o después.

## Realizado por:
<table>
   <td align="center"><a href="https://github.com/rubenangber"><img src="https://avatars.githubusercontent.com/u/70805470?v=4" width="100px;" alt=""/><br /><sub><b>Rubén Angoso</b></sub></a><br /> 
   <td align="center"><a href="https://www.instagram.com/martaadcl_/"><img src="https://avatars.githubusercontent.com/u/71805470?v=3" width="100px;" alt=""/><br /><sub><b>Marta de Castro</b></sub></a><br /> 
</table>