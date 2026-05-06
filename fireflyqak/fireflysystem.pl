%====================================================================================
% fireflysystem description   
%====================================================================================
event( emitlight, light(V) ).
%====================================================================================
context(ctxfirefly, "localhost",  "TCP", "8020").
 qactor( firefly, ctxfirefly, "it.unibo.firefly.Firefly").
 static(firefly).
