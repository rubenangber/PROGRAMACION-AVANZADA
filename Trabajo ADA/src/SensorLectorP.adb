with Ada.Text_IO;
with Ada.Real_Time;

package body SensorLectorP is
   task body Sensor is
      use type Ada.Real_Time.Time;
      use type Ada.Real_Time.Time_Span;

      intervalo: constant Ada.Real_Time.Time_Span := Ada.Real_Time.Milliseconds(100); -- 1 segundo
      nextTime: Ada.Real_Time.Time;

   begin
      loop
         select
            accept leer(result: out Integer) do
               nextTime := Ada.Real_Time.Clock + intervalo;
               r.leer(result);
               delay until nextTime;
            end leer;
         or
            terminate;
         end select;
      end loop;
   end Sensor;
end SensorLectorP;
