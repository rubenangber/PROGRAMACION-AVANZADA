with Ada.Real_Time;
use Ada.Real_Time;
with Ada.Real_Time.Timing_Events;
use Ada.Real_Time;
with Text_IO;

package body Coordinadora is
   task body Tarea_Coordinadora(Reactor_Ptr: access Reactores.Reactor) is
      Intervalo: Duration := 2.0;
      Next_Time: Time;
   begin
      Next_Time := Clock + Intervalo;
      loop
         select
            accept Recibir_Mensaje;
         or
            delay 3.0;
               Text_IO.Put_Line("¡ALERTA! No se recibió mensaje de reactor: " & Reactor_Ptr.all'Img);
         end select;
         delay until Next_Time;
         Next_Time := Clock + Intervalo;
      end loop;
   end Tarea_Coordinadora;
end Coordinadora;
