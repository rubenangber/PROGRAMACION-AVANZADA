-- ActuadorEscritorP.ads
with Ada.Real_Time;
with Reactores; use Reactores;

package ActuadorEscritorP is
   task type Actuador (r: access Reactor; id: Integer) is
      entry abrir;
      entry cerrar;
   end Actuador;
end ActuadorEscritorP;
