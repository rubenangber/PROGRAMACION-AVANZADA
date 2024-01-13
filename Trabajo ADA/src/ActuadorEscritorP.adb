with Ada.Calendar;
with Ada.Numerics;
with Ada.Real_Time;
with Ada.Numerics.Discrete_Random;
use Ada.Calendar;
with Text_IO;


package body ActuadorEscritorP is
   task body Actuador is
      moverPuertaIntervalo:Duration:=0.1; --1 decima de segundo
      movidaTimer:Time;
      cerrado:Boolean :=true;

   begin
      loop
         select
            accept abrir do
               begin
                  if cerrado = true then

                     --Eliminar comprobacion
                     --Text_IO.Put_Line("Abriendo compuerta reactor: " & id'Img);
                     movidaTimer := Clock + moverPuertaIntervalo;
                     delay until movidaTimer;
                     r.abrirPuerta;
                     --Text_IO.Put_Line("Abierta compuerta reactor: "&id'Img);
                     cerrado:= false;
                  end if;
               end;
            end abrir;
         or
            accept cerrar  do
               begin
                  if cerrado = false then
                     --Text_IO.Put_Line("Cerrando compuerta del reactor: "& id'Img);
                     movidaTimer:= Clock + moverPuertaIntervalo;
                     delay until movidaTimer;
                     r.cerrarPuerta;
                     --Text_IO.Put_Line("Compuerta del reactor "& id'Img & "cerrada");
                     cerrado:=true;
                  end if;
               end;
            end cerrar;
         or
            terminate;
         end select;
      end loop;
   end Actuador;
end ActuadorEscritorP;
