with Ada.Real_Time;
with Reactores; use Reactores;

package SensorLectorP is
   task type Sensor(r: access Reactor) is
      entry leer(result: out Integer);
   end Sensor;
end SensorLectorP;
