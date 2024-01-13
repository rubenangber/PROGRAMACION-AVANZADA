with Ada.Real_Time;
with Ada.Text_IO; use Ada.Text_IO;
with Reactores; use Reactores;
with SensorLectorP; use SensorLectorP;
with ActuadorEscritorP; use ActuadorEscritorP;
with Ada.Calendar;
with Ada.Numerics;
with Ada.Real_Time;
with Ada.Numerics.Discrete_Random;
use Ada.Calendar;
with Text_IO;

procedure main is
   type Reactores is array (1..3) of aliased Reactor;
   subtype reactorAleatorio is Integer range 1..3;
   package Aleatorio is new Ada.Numerics.Discrete_Random(reactorAleatorio);

   -- Aumentar la temperatura
   task type Temperatura (r: access Reactores);
   task body Temperatura is
      intervalo: Duration := 2.0;
      nextTime: Time;
      seed: Aleatorio.Generator;
      temp: reactorAleatorio;


   begin
      Aleatorio.Reset(seed);
      nextTime := Clock + intervalo;
      loop
         -- Incremento aleatorio
         --temp := Aleatorio.Random(1);

         temp := 1;
         r(temp).incrementar(150);

         -- Eliminar comprobacion
         --Text_IO.Put_Line("Simulación: Incrementando temperatura en Reactor " & temp'Img);
         delay until nextTime;
         nextTime := Clock + intervalo;
      end loop;
   end Temperatura;

   -- Controlador
   task type Controlador is
      entry avisarme;
   end Controlador;

   task body Controlador is
   begin
      loop
         select
            accept avisarme do
               --Text_IO.Put_Line("Controlador: Recibido aviso de una tarea");
               null;
            end avisarme;
         or
            delay 3.0;
            Text_IO.Put_Line("Controlador: Alerta - No se recibió respuesta en 3 segundos");
         end select;
      end loop;
   end Controlador;

   -- Tarea Reactor
	task type reactorTask (r: access Reactor; id: Integer; act: access Actuador; sen: access Sensor);
	task body reactorTask is
   	con: Controlador;
   	intervalo: Duration := 2.0; -- Muestreo de la temperatura cada 2s
   	nextTimer: Time;
   	inicioIntervalo: Duration := 0.01; --1 decima de segundo
   	inicio: Time;
   	temperatura: Integer;
	begin
   		inicio := inicioIntervalo + Clock;
  		delay until inicio;
   		nextTimer := intervalo + Clock;

   		loop
      		sen.leer(temperatura);

      		Text_IO.Put_Line("Reactor " & id'Img & ": Lectura de temperatura: " & temperatura'Img);
      			if temperatura >= 1500 then
            			act.abrir;
            			--Text_IO.Put_Line("Reactor " & id'Img & ": Abriendo compuerta");
         			if temperatura > 1750 then
            				Text_IO.Put_Line("Reactor " & id'Img & ": Temperatura superior a 1750º - Manteniendo compuerta abierta");

         			end if;
      			elsif temperatura < 1500 then
            			act.cerrar;
            			--Text_IO.Put_Line("Reactor " & id'Img & ": Cerrando compuerta");

      			end if;
      		con.avisarme;
      		delay until nextTimer;
      		nextTimer := intervalo + Clock;
   		end loop;
	end reactorTask;

	-- Inicio
   procedure inicio is

   rs: aliased Reactores;
   t: Temperatura(rs'Access);
   -- Declaraciones
   a1: aliased ActuadorEscritorP.Actuador (rs(1)'Access, 1);
   s1: aliased SensorLectorP.Sensor(rs(1)'Access);
   r1: reactorTask(rs(1)'Access, 1, a1'Access, s1'Access);

   a2: aliased ActuadorEscritorP.Actuador (rs(2)'Access, 2);
   s2: aliased SensorLectorP.Sensor(rs(2)'Access);
   r2: reactorTask(rs(2)'Access, 2, a2'Access, s2'Access);

   a3: aliased ActuadorEscritorP.Actuador (rs(3)'Access, 3);
   s3: aliased SensorLectorP.Sensor(rs(3)'Access);
   r3: reactorTask(rs(3)'Access, 3, a3'Access, s3'Access);
begin
   null;
end inicio;

begin
   Text_IO.Put_Line("SIMULACIÓN INICIADA:...");
   Text_IO.Put_Line("Temperatura inicial:  1450");
   inicio;
end main;
