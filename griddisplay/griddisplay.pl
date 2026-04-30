%====================================================================================
% griddisplay description   
%====================================================================================
dispatch( cellstate, cellstate(X,Y,COLOR) ). %commute cell state
event( cellstate, cellstate(X,Y,COLOR) ). %commute cell state
%====================================================================================
context(ctxgrid, "localhost",  "TCP", "8050").
 qactor( griddisplay, ctxgrid, "it.unibo.griddisplay.Griddisplay").
 static(griddisplay).
