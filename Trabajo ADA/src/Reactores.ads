-- Control.ads
with Ada.Real_Time.Timing_Events;

package Reactores is
   protected type Reactor is
      procedure leer(temp: out Integer);
      procedure incrementar(incremento: Integer);
      procedure decrementar(decremento: Integer);
      procedure abrirPuerta;
      procedure cerrarPuerta;
      procedure Timer(event: in out Ada.Real_Time.Timing_Events.Timing_Event);
   private
      temperatura: Integer := 1450;
      bajarJitterControl: Ada.Real_Time.Timing_Events.Timing_Event;
      bajarPeriodo: Ada.Real_Time.Time_Span := Ada.Real_Time.Milliseconds(10); --1 decima de segundo
      nextTime: Ada.Real_Time.Time;
   end Reactor;
end Reactores;
