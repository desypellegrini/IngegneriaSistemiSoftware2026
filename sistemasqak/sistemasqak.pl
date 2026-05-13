%====================================================================================
% sistemasqak description   
%====================================================================================
dispatch( eval, arg(V) ).
%====================================================================================
context(ctxsistemas, "localhost",  "TCP", "8010").
 qactor( sistemas, ctxsistemas, "it.unibo.sistemas.Sistemas").
 static(sistemas).
