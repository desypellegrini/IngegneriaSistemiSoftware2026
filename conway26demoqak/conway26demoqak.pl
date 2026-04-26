%====================================================================================
% conway26demoqak description   
%====================================================================================
mqttBroker("localhost", "1883", "lifegameIn").
event( start, start(X) ).
event( stop, stop(X) ).
event( clear, clear(X) ).
%====================================================================================
context(ctxgame, "localhost",  "TCP", "8010").
 qactor( lifegame, ctxgame, "it.unibo.lifegame.Lifegame").
 static(lifegame).
  qactor( inputmock, ctxgame, "it.unibo.inputmock.Inputmock").
 static(inputmock).
